package com.ikhokha.techcheck.view;

import android.app.Activity;
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
import com.google.gson.internal.LinkedTreeMap;
import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.CartItem;
import com.ikhokha.techcheck.model.db.FirebaseRealTimeDB;
import com.ikhokha.techcheck.model.Item;
import com.ikhokha.techcheck.model.util.Constants;
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
    public static final int REQUEST_CODE = 1;
    private static List<String> mapKeys;
    HashMap<String,Item> hashMap;
    Bundle savedInstanceStateExt;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.savedInstanceStateExt = savedInstanceState;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);

        Button startScanButton = findViewById(R.id.startScanButton);
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ScanningActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

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

                mapKeys = new ArrayList<>();


                for (Map.Entry<String, Item> entry: map.entrySet()){
                    hashMap = new HashMap<>();
                    hashMap.put(entry.getKey(),entry.getValue());
                    mapKeys.add(entry.getKey());
                    hashMapList.add(hashMap);
                }


                recyclerViewAdapter = new RecyclerViewAdapter(hashMapList,HomeActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        recyclerViewAdapter.callBack(position);
                        callCartFragmentFragment(savedInstanceState);

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
        CartFragment cartFragment = new CartFragment();
        fragmentTransaction.add(R.id.cartFrameLayout, cartFragment, null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("barcode");
                // do something with the result

                for (String barcode: mapKeys){
                    if (result.equals(barcode)){
                        Object getRowOnItem = hashMap.get(result);

                        //TODO: Find out why error is happening
                        /*LinkedTreeMap<Object,Object> t = (LinkedTreeMap) getRowOnItem;
                        String description = t.get("description").toString();
                        String price = t.get("price").toString();

                        CartItem cartItem = new CartItem();
                        cartItem.setPrice(Double.parseDouble(price));
                        cartItem.setDescription(description);
                        Constants.constantList.add(cartItem);
                        callCartFragmentFragment(savedInstanceStateExt);*/
                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
