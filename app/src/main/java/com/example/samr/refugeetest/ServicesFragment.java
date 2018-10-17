package com.example.samr.refugeetest;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {


    //add Fire base Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private ListView mListView;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        view.findViewById(R.id.btnServices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ServicesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.screen_area, fragment, "ServicesFragment");
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.btnRequests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new RequestsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.screen_area, fragment, "RequestsFragment");
                fragmentTransaction.commit();
            }
        });

/*
        mListView = (ListView) view.findViewById(R.id.listView);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be usable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showSellServiceData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/
    }

    private void showSellServiceData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            UserInformation uInfo = new UserInformation();

//            uInfo.setSellImageUrl(ds.child("Sell_Service").getValue(UserInformation.class).getSellImageUrl()); //set the name
//            uInfo.setSellTitle(ds.child("Sell_Service").getValue(UserInformation.class).getSellTitle()); //set the email
//            uInfo.setSellDescription(ds.child("Sell_Service").getValue(UserInformation.class).getSellDescription()); //set the phone_num
//            uInfo.setSellPrice(ds.child("Sell_Service").getValue(UserInformation.class).getSellPrice()); //set the phone_num
//            uInfo.setSellTime(ds.child("Sell_Service").getValue(UserInformation.class).getSellTime()); //set the phone_num

//            Toast.makeText(getActivity(), "Text!", Toast.LENGTH_SHORT).show();


//            //display all the information
//            Log.d(TAG, "showData: name: " + uInfo.getName());
//            Log.d(TAG, "showData: email: " + uInfo.getEmail());
//            Log.d(TAG, "showData: phone_num: " + uInfo.getPhone_num());

//            ArrayList<String> array  = new ArrayList<>();
//            array.add(uInfo.getSellImageUrl());
//            array.add(uInfo.getSellTitle());
//            array.add(uInfo.getSellDescription());
//            array.add(uInfo.getSellPrice());
//            array.add(uInfo.getSellTime());
//
//            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,array);
//            mListView.setAdapter(adapter);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

}