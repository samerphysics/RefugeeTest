package com.example.samr.refugeetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestListActivity";

    ProgressBar progressBar;

    private List<PojoRequest> mRequestList;
    private RequestListAdapter requestListAdapter;

    //views
    private ListView lvRequests;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_requests, container, false);

        requestListAdapter = new RequestListAdapter(getContext(), new ArrayList<PojoRequest>());
        lvRequests = rootView.findViewById(R.id.rl_lv_requests);
        lvRequests.setAdapter(requestListAdapter);

        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //data
        DatabaseReference mDatabaseRequestServiceRef = FirebaseDatabase.getInstance().
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
                requestListAdapter = new RequestListAdapter(getContext(), mRequestList);
                lvRequests.setAdapter(requestListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 10/21/18
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnServices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ServicesFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.screen_area, fragment, "ServicesFragment");
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.btnRequests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new RequestsFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.screen_area, fragment, "RequestsFragment");
                fragmentTransaction.commit();
            }
        });
    }
}