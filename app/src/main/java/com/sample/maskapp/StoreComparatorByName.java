package com.sample.maskapp;

import java.util.Comparator;

class StoreComparatorByName implements Comparator<Store> {
    @Override
    public int compare(Store o1, Store o2) {
        return o1.name.compareTo(o2.name);
    }
}
