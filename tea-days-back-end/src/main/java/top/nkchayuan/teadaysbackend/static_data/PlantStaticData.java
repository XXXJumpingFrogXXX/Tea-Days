package top.nkchayuan.teadaysbackend.static_data;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantStaticData {
    public static class Record {
        public int id;
        public String name;
        public int time;
        public int maxYield;
        public int minYield;
        public int itemId;
        public int yieldItemId;

        public Record(int id, String name, int time, int min, int max, int itemId, int yieldItemId) {
            this.id = id;
            this.name = name;
            this.time = time;
            this.maxYield = max;
            this.minYield = min;
            this.itemId = itemId;
            this.yieldItemId = yieldItemId;
        }
    }

    public static ArrayList<Record> data;
    public static HashMap<Integer, Record> lookup;
    public static HashMap<Integer, Record> lookupByItem;

    static {
        data = new ArrayList<>();
        lookup = new HashMap<>();
        lookupByItem = new HashMap<>();
        data.add(new Record(1, "long-jing", 48, 10, 20, 8, 1));
        data.add(new Record(2, "guan-yin", 72, 18, 28, 9, 2));
        data.add(new Record(3, "pu-er", 99, 25, 30, 10, 3));
        data.add(new Record(4, "bi-luo-chun", 120, 30, 35, 11, 4));
        data.add(new Record(5, "tian-shan-bai", 100, 25, 30, 12, 5));
        data.add(new Record(6, "lu-yuan", 80, 10, 50, 13, 6));
        data.add(new Record(7, "feng-huang", 105, 20, 30, 14, 7));

        for (Record r : data) {
            lookup.put(r.id, r);
        }
        for (Record r : data) {
            lookupByItem.put(r.itemId, r);
        }
    }
}
