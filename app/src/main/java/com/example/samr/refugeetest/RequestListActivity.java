package com.example.samr.refugeetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestListActivity extends AppCompatActivity {
    private static final String TAG = "RequestListActivity";

    ProgressBar progressBar;

    //data
    private DatabaseReference mDatabaseRequestServiceRef;
    private List<PojoRequest> mRequestList;
    private RequestListAdapter requestListAdapter;

    //views
    private ListView lvRequests;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        requestListAdapter = new RequestListAdapter(getApplicationContext(), new ArrayList());
        lvRequests = findViewById(R.id.rl_lv_requests);
        lvRequests.setAdapter(requestListAdapter);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.VISIBLE);

        mDatabaseRequestServiceRef = FirebaseDatabase.getInstance().
                getReference("Request_Service");
        mDatabaseRequestServiceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PojoRequest> newRequestList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        PojoRequest pojoRequest = new PojoRequest(ds);
                        newRequestList.add(pojoRequest);

                        progressBar.setVisibility(View.GONE);

                    } catch (Exception e) {
                        Log.d(TAG, "onDataChange: Exception caught", e);
                    }
                }
                mRequestList = newRequestList;
                requestListAdapter = new RequestListAdapter(getApplicationContext(), mRequestList);
                lvRequests.setAdapter(requestListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 10/21/18
            }
        });
    }
}
