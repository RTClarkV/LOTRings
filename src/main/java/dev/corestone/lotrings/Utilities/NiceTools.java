package dev.corestone.lotrings.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class NiceTools {
    public static <K, T> HashMap<K, T> castHashMap(K key, T thing){
        HashMap<K, T> hashMap = new HashMap<>();
        hashMap.put(key, thing);
        return hashMap;
    }
    public static <T> ArrayList<T> castArray(T thing){
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.add(thing);
        return arrayList;
    }
}
