package com.sample.maskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView storeList;
    private StoreAdapter storeAdapter;
    private SwipeRefreshLayout refreshLayout;
    private double latitude, longitude;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storeList = findViewById(R.id.store_list);
        storeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        storeList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));   // 리스트 구분하는 선

        Intent intent = getIntent();
       latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        
        fetchStoreSales(latitude, longitude, 5000);
        storeAdapter = new StoreAdapter(latitude, longitude);
        RadioGroup sortRadioGroup = findViewById(R.id.sort_radio_group);
        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sort_distance:
                        storeAdapter.sortByDistance();
                        break;
                    case R.id.sort_name:
                        storeAdapter.sortByName();
                        break;
                    case R.id.sort_stat:
                        storeAdapter.sortByStat();
                        break;
                }
            }
        });
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStoreSales(latitude, longitude, 5000);

            }
        });
    }

    private void fetchStoreSales(double latitude, double longitude, int meter) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://8oi9s0nnth.apigw.ntruss.com").addConverterFactory(GsonConverterFactory.create()).build();
        MaskApi maskApi = retrofit.create(MaskApi.class);
        maskApi.getStoresByGeo(latitude, longitude, meter).enqueue(new Callback<StoreSaleResult>() {
            @Override
            public void onResponse(Call<StoreSaleResult> call, Response<StoreSaleResult> response) {
                if(response.code()==200) {
                    StoreSaleResult result = response.body();
                    Log.i(TAG,"count = " + result.count);
                    StoreAdapter storeAdapter = new StoreAdapter(result.stores);
                    storeList.setAdapter(storeAdapter);
                }
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<StoreSaleResult> call, Throwable t) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }
}