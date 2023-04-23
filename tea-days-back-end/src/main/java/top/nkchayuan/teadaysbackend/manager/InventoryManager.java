package top.nkchayuan.teadaysbackend.manager;

import top.nkchayuan.teadaysbackend.domain.Item;
import top.nkchayuan.teadaysbackend.mapper.InventoryMapper;

import java.util.ArrayList;

public class InventoryManager {
    private final InventoryMapper mapper;

    public InventoryManager(InventoryMapper mapper) {
        this.mapper = mapper;
    }

    public void addTo(int userId, int itemId, int count) {
        ArrayList<Item> prevItem = this.mapper.selectItem(userId, itemId);
        if (prevItem.isEmpty()) {
            this.mapper.createItem(userId, itemId, count);
        } else {
            this.mapper.modifyCount(userId, itemId, prevItem.get(0).count + count);
        }
    }

    public boolean removeFrom(int userId, int itemId, int count) {
        ArrayList<Item> prevItem = this.mapper.selectItem(userId, itemId);
        if (prevItem.isEmpty() || prevItem.get(0).count < count) {
            return false;
        } else {
            this.mapper.modifyCount(userId, itemId, prevItem.get(0).count - count);
            return true;
        }
    }

    public boolean existsIn(int userId, int itemId, int count) {
        ArrayList<Item> prevItem = this.mapper.selectItem(userId, itemId);
        return !prevItem.isEmpty() && prevItem.get(0).count >= count;
    }

    public ArrayList<Item> getAll(int userId) {
        return this.mapper.selectAll(userId);
    }
}
