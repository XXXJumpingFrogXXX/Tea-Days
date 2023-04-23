package top.nkchayuan.teadaysbackend.static_data;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemStaticData {
    public static class Record {
        public int id;
        public String name;
        public int category;  // 0: sampling, 1: products, 2: tools
        public int price;

        public Record(int id, String name, int category, int price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
        }
    }

    public static ArrayList<Record> data;
    public static HashMap<Integer, Record> lookup;

    static {
        data = new ArrayList<>();
        lookup = new HashMap<>();
        data.add(new Record(1, "long-jing-leaf", 1, 8));
        data.add(new Record(2, "guan-yin-leaf", 1, 4));
        data.add(new Record(3, "pu-er-leaf", 1, 10));
        data.add(new Record(4, "bi-luo-chun-leaf", 1, 12));
        data.add(new Record(5, "tian-shan-bai-leaf", 1, 15));
        data.add(new Record(6, "lu-yuan-leaf", 1, 16));
        data.add(new Record(7, "feng-huang-leaf", 1, 20));
        data.add(new Record(8, "long-jing-sampling", 0, 100));
        data.add(new Record(9, "guan-yin-sampling", 0, 50));
        data.add(new Record(10, "pu-er-sampling", 0, 150));
        data.add(new Record(11, "bi-luo-chun-sampling", 0, 200));
        data.add(new Record(12, "tian-shan-bai-sampling", 0, 150));
        data.add(new Record(13, "lu-yuan-sampling", 0, 130));
        data.add(new Record(14, "feng-huang-sampling", 0, 210));
        data.add(new Record(15, "water-drop", 2, 25));
        data.add(new Record(16, "fertilizer", 2, 200));
        for (Record r : data) {
            lookup.put(r.id, r);
        }
    }
}
