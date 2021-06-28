package com.sample.maskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {
    private List<Store> storeList;
    private double latitude, longtitude;

    public StoreAdapter(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public void setList(List<Store>storeList) {
        this.storeList = storeList;
        Collections.sort(this.storeList, new StoreComparatorByStat());      // 재고현황 정렬
        notifyDataSetChanged();
    }

    public void sortByDistance() {
        Collections.sort(this.storeList, new StoreComparatorByDistance(latitude, longtitude));  // 거리순 정렬
        notifyDataSetChanged();
    }

    public void sortByStat() {
        Collections.sort(this.storeList, new StoreComparatorByStat());      // 재고현황 정렬
        notifyDataSetChanged();

    }

    public void sortByName() {
        Collections.sort(this.storeList, new StoreComparatorByName());      // 이름순 정렬
        notifyDataSetChanged();
    }

    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_store, parent, false);
        StoreViewHolder holder = new StoreViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        double distance = Util.getDistance(latitude, longtitude, store.lat, store.lng);
        String distanceAsText = Util.getDistanceAsText(distance);
        holder.bind(store, distanceAsText);
    }

    @Override
    public int getItemCount() {
        if(storeList!=null) {
            return storeList.size();
        }
        return 0;
    }
}
