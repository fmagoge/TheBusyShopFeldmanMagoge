package com.ikhokha.techcheck.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.db.FirebaseRealTimeDB;
import com.ikhokha.techcheck.model.Item;
import com.ikhokha.techcheck.presenter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static FragmentManager fragmentManager;

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

                recyclerViewAdapter = new RecyclerViewAdapter(hashMapList,HomeActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(HomeActivity.this, "Add to Cart", Toast.LENGTH_SHORT ).show();
                        recyclerViewAdapter.callBack(position);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        callCartFragmentFragment(savedInstanceState);
    }

    private void callCartFragmentFragment(Bundle savedInstanceState){
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null){
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CartFragment signupFragment = new CartFragment();
        fragmentTransaction.add(R.id.cartFrameLayout, signupFragment, null);
        fragmentTransaction.commit();
    }

}
