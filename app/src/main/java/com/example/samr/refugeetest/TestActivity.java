package com.example.samr.refugeetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    UserInformation2 userInformation2;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        userInformation2 = new UserInformation2();

        listView = findViewById(R.id.listView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.user_request, R.id.tvUserRequest, arrayList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // getting all the children inside Users

                    userInformation2 = dataSnapshot1.getValue(UserInformation2.class);

                    if (userInformation2 != null) {
                        arrayList.add(userInformation2.getRequest_user_id());
                    }
                }

                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}