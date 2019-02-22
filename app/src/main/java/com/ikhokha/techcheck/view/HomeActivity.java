package com.ikhokha.techcheck.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.FirebaseRealTimeDB;
import com.ikhokha.techcheck.model.Item;
import com.ikhokha.techcheck.presenter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);

        Button startScanButton = findViewById(R.id.startScanButton);
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        FirebaseRealTimeDB realTimeDB = new FirebaseRealTimeDB();

        realTimeDB.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Item str =  dataSnapshot.getValue(Item.class);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(dataSnapshot.getValue());
                ArrayList<HashMap<String, Item>> hashMapList = new ArrayList<>();
                Map<String,Item> map = new HashMap<>();
                map = (HashMap<String,Item>) gson.fromJson(jsonStr, map.getClass());

                HashMap<String,Item> hashMap;
                for (Map.Entry<String, Item> entry: map.entrySet()){
                    hashMap = new HashMap<>();
                    hashMap.put(entry.getKey(),entry.getValue());
                    hashMapList.add(hashMap);
                }





                if (map != null){
                    recyclerViewAdapter = new RecyclerViewAdapter(hashMapList,HomeActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }

                List<HashMap<String,Item>> mapArrayList = new ArrayList<>();
                mapArrayList.add((HashMap<String, Item>) map);
                System.out.println(mapArrayList.toString());
                ////List<String> mapKeys = (List<String>) map.keySet();
                //System.out.println(mapArrayList);
                //ArrayList<HashMap<String,Item>> testArrayListMap = mapArrayList;
                //.out.println(mapKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
