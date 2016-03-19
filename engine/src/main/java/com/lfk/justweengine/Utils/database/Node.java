package com.lfk.justweengine.Utils.database;

import java.util.ArrayList;

/**
 * Created by liufengkai on 16/3/18.
 */
public class Node {
    public ArrayList<Object> arrayList;

    public long key;

    public Node(Object... obj) {
        arrayList = new ArrayList<>();
        for (int i = 0; i < obj.length; i++) {
            arrayList.add(obj[i]);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            builder.append(arrayList.get(i).toString());
        }
        return builder.toString();
    }
}
