package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.domain.Item;
import top.nkchayuan.teadaysbackend.mapper.InventoryMapper;
import top.nkchayuan.teadaysbackend.static_data.ItemStaticData;

import java.util.ArrayList;
import java.util.HashMap;

import static top.nkchayuan.teadaysbackend.controller.UserController.sessionLookup;

@Slf4j
@RequestMapping("/inventory")
@RestController
public class InventoryController {
    @Autowired
    InventoryMapper inventoryMapper;

    @PostMapping("list")
    public ResponseEntity<?> getAllItems(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            int userId = sessionLookup.get(session);
            ArrayList<Item> items = inventoryMapper.selectAll(userId);
            HashMap<Integer, Boolean> bitset = new HashMap<>();
            for (Item item : items) {
                bitset.put(item.item_id, true);
            }
            for (ItemStaticData.Record record : ItemStaticData.data) {
                if (!bitset.containsKey(record.id)) {
                    Item item = new Item();
                    item.count = 0;
                    item.item_id = record.id;
                    item.user_id = userId;
                    items.add(item);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(items);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("query_sampling")
    public ResponseEntity<?> querySampling(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            ArrayList<Integer> itemId = inventoryMapper.getUserSampling(sessionLookup.get(session));
            return ResponseEntity.status(HttpStatus.OK).body(itemId.isEmpty() ? 0 : itemId.get(0));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("select_sampling")
    public ResponseEntity<?> selectSampling(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer sampling) {
        ItemStaticData.Record record = ItemStaticData.lookup.get(sampling);
        if (record == null || record.category != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            int userId = sessionLookup.get(session);
            ArrayList<Integer> itemId = inventoryMapper.getUserSampling(userId);
            if (itemId.isEmpty()) {
                inventoryMapper.insertUserSampling(userId, sampling);
            } else {
                inventoryMapper.updateUserSampling(userId, sampling);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
