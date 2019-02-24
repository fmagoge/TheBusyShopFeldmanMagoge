package com.ikhokha.techcheck.model.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRealTimeDB {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://the-busy-shop.firebaseio.com/");

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
