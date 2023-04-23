package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.manager.InventoryManager;
import top.nkchayuan.teadaysbackend.mapper.InventoryMapper;
import top.nkchayuan.teadaysbackend.mapper.StoreMapper;
import top.nkchayuan.teadaysbackend.static_data.ItemStaticData;

import static top.nkchayuan.teadaysbackend.controller.UserController.sessionLookup;


@Slf4j
@RequestMapping("/store")
@RestController
public class StoreController {
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private InventoryMapper inventoryMapper;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer itemId, @RequestParam Integer count){
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            ItemStaticData.Record item = ItemStaticData.lookup.get(itemId);
            int userId = sessionLookup.get(session);
            if (item == null || (item.category != 0 && item.category != 2)) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
            }
            int coin = storeMapper.getMoney(userId);
            int requiredCoin = count * item.price;
            if (coin < requiredCoin) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
            storeMapper.setMoney(userId, coin - requiredCoin);
            InventoryManager inventory = new InventoryManager(inventoryMapper);
            inventory.addTo(userId, itemId, count);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer itemId, @RequestParam Integer count){
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            ItemStaticData.Record item = ItemStaticData.lookup.get(itemId);
            int userId = sessionLookup.get(session);
            if (item == null || item.category != 1) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
            }
            InventoryManager inventory = new InventoryManager(inventoryMapper);
            if (!inventory.existsIn(userId, itemId, count)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
            int coin = storeMapper.getMoney(userId);
            int obtainedCoin = count * item.price;
            storeMapper.setMoney(userId, coin + obtainedCoin);
            inventory.removeFrom(userId, itemId, count);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
