package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.domain.*;
import top.nkchayuan.teadaysbackend.manager.InventoryManager;
import top.nkchayuan.teadaysbackend.manager.PlantManager;
import top.nkchayuan.teadaysbackend.mapper.InventoryMapper;
import top.nkchayuan.teadaysbackend.mapper.PlantMapper;
import top.nkchayuan.teadaysbackend.mapper.UserMapper;
import top.nkchayuan.teadaysbackend.static_data.PlantStaticData;

import java.util.ArrayList;
import java.util.Random;

import static top.nkchayuan.teadaysbackend.controller.UserController.sessionLookup;

@Slf4j
@RequestMapping("/game")
@RestController
public class PlantController {
    @Autowired
    private PlantMapper plantMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InventoryMapper inventoryMapper;

    @PostMapping("/all")
    public ResponseEntity<?> stageAll(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            PlantManager manager = new PlantManager(plantMapper);
            int userId = sessionLookup.get(session);
            for (int i = 1; i <= 30; ++i)
                manager.updateSoil(userId, i);
            ArrayList<Soil> plants = plantMapper.readAll(userId);
            PlantCheckInfo[] ret = new PlantCheckInfo[30];
            for (int i = 1; i <= 30; ++i) {
                ret[i - 1] = new PlantCheckInfo();
                ret[i - 1].species = 0;
                ret[i - 1].stage = 0;
            }
            for (Soil soil : plants) {
                int species = soil.getPlant_species();
                if (species != 0) {
                    ret[soil.getSoil_id() - 1].species = soil.getPlant_species();
                }
                if (species > 0) {
                    int t = soil.getProgress() * 2 / PlantStaticData.lookup.get(species).time;
                    if (t > 2) t = 2;
                    ret[soil.getSoil_id() - 1].stage = t;
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(ret);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/plant")
    public ResponseEntity<?> plant(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer soilId, @RequestParam Integer itemId) {
        if (soilId < 0 || soilId > 30)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session) && userMapper.queryRole(sessionLookup.get(session)) != 0) {
            int userId = sessionLookup.get(session);
            PlantManager manager = new PlantManager(plantMapper);
            if (manager.isOccupied(userId, soilId)) {
                return ResponseEntity.status(HttpStatus.IM_USED).build();
            }
            PlantStaticData.Record record = PlantStaticData.lookupByItem.get(itemId);
            if (record == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
            InventoryManager inventoryManager = new InventoryManager(inventoryMapper);
            if (inventoryManager.existsIn(userId, itemId, 1)) {
                inventoryManager.removeFrom(userId, itemId, 1);
                manager.plant(userId, soilId, record.id);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/harvest")
    public ResponseEntity<?> harvest(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer soilId) {
        if (soilId < 0 || soilId > 30)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session) && userMapper.queryRole(sessionLookup.get(session)) != 0) {
            int userId = sessionLookup.get(session);
            PlantManager manager = new PlantManager(plantMapper);
            manager.updateSoil(userId, soilId);
            int speciesId = manager.getSpeciesId(userId, soilId);
            if (speciesId <= 0) {
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
            }
            if (manager.getStage(userId, soilId) != 2) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
            PlantStaticData.Record record = PlantStaticData.lookup.get(speciesId);
            InventoryManager inventoryManager = new InventoryManager(inventoryMapper);
            int count = new Random().nextInt(record.minYield, record.maxYield);
            inventoryManager.addTo(userId, record.yieldItemId, count);
            manager.plant(userId, soilId, -1);
            HarvestInfo harvestInfo = new HarvestInfo();
            harvestInfo.count = count;
            harvestInfo.itemId = record.yieldItemId;
            return ResponseEntity.status(HttpStatus.OK).body(harvestInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/water")
    public ResponseEntity<?> water(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer soilId) {
        if (soilId < 0 || soilId > 30)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session) && userMapper.queryRole(sessionLookup.get(session)) != 0) {
            int userId = sessionLookup.get(session);
            PlantManager manager = new PlantManager(plantMapper);
            manager.updateSoil(userId, soilId);
            int speciesId = manager.getSpeciesId(userId, soilId);
            if (speciesId <= 0) {
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
            }
            if (manager.getStage(userId, soilId) == 2) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
            InventoryManager inventoryManager = new InventoryManager(inventoryMapper);
            if (inventoryManager.existsIn(userId, 15, 1)) {
                inventoryManager.removeFrom(userId, 15, 1);
                manager.water(userId, soilId);
            } else {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/check")
    public ResponseEntity<?> check(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer soilId) {
        if (soilId < 0 || soilId > 30)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session) && userMapper.queryRole(sessionLookup.get(session)) != 0) {
            int userId = sessionLookup.get(session);
            PlantManager manager = new PlantManager(plantMapper);
            manager.updateSoil(userId, soilId);
            PlantCheckInfo plantCheckInfo = new PlantCheckInfo();
            plantCheckInfo.species = manager.getSpeciesId(userId, soilId);
            plantCheckInfo.stage = 0;
            if (plantCheckInfo.species > 0) {
                PlantStaticData.Record record = PlantStaticData.lookup.get(plantCheckInfo.species);
                plantCheckInfo.maxProgress = record.time;
                plantCheckInfo.progress = manager.getProgress(userId, soilId);
                plantCheckInfo.stage = manager.getStage(userId, soilId);
            }
            return ResponseEntity.status(HttpStatus.OK).body(plantCheckInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/remove")
    public ResponseEntity<?> remove(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer soilId) {
        if (soilId < 0 || soilId > 30)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session) && userMapper.queryRole(sessionLookup.get(session)) != 0) {
            int userId = sessionLookup.get(session);
            PlantManager manager = new PlantManager(plantMapper);
            manager.updateSoil(userId, soilId);
            manager.plant(userId, soilId, 0);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}