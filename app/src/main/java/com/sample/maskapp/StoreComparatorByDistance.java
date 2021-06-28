package com.sample.maskapp;

import java.util.Comparator;

class StoreComparatorByDistance implements Comparator<Store> {
    private double latitude, longtitude;

    public StoreComparatorByDistance(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }


    @Override
    public int compare(Store o1, Store o2) {
        double distance1 = Util.getDistance(latitude, longtitude, o1.lat, o1.lng);
        double distance2 = Util.getDistance(latitude, longtitude, o2.lat, o2.lng);
        return (int)(distance1 - distance2);
    }
}
