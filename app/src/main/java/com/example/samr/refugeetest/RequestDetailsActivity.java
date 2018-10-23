package com.example.samr.refugeetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.StringJoiner;

public class RequestDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RequestDetailsActivity";
    //Data Ref
    private String requestId;
    private String userId;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseRequestRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        Bundle extras = getIntent().getExtras();
        requestId = extras.get("requestId").toString();
        userId = extras.get("userId").toString();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (requestId !=null && !requestId.isEmpty()) {
            mDatabaseRequestRef = FirebaseDatabase
                    .getInstance()
                    .getReference("/Request_Service/" + requestId);
            mDatabaseRequestRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: mDatabaseRequestRef | updating data");
                    PojoRequest request = new PojoRequest(dataSnapshot);
                    if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
                        final ImageView ivRequestImage = findViewById(R.id.iv_ard_request_image);
                        Picasso.with(getApplicationContext())
                                .load(request.getImageUrl())
                                .fit()
                                .into(ivRequestImage);
                    }

                    if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                        final TextView tvRequestTitle = findViewById(R.id.tv_ard_request_title);
                        tvRequestTitle.setText(request.getTitle());
                    }

                    if (request.getPrice() != null && !request.getPrice().isEmpty()) {
                        final TextView tvRequestPrice = findViewById(R.id.tv_ard_price);
                        tvRequestPrice.setText("JD " + request.getPrice());
                    }

                    if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                        final TextView tvRequestDescription = findViewById(R.id.tv_ard_request_description);
                        tvRequestDescription.setText(request.getDescription());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (databaseError != null && databaseError.toException() != null) {
                        Log.d(TAG, "onCancelled: mDatabaseRequestRef | exception caught", databaseError.toException());
                    } else {
                        Log.d(TAG, "onCancelled: mDatabaseRequestRef | cancelled without exception");
                    }
                }
            });

            mDatabaseUserRef = FirebaseDatabase
                    .getInstance()
                    .getReference("/Users/" + userId);
            mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: mDatabaseUserRef | updating data");
                    String firstName = dataSnapshot.child("first_name").getValue().toString();
                    String lastName = dataSnapshot.child("last_name").getValue().toString();
                    String name;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        StringJoiner nameBuilder = new StringJoiner(" ");
                        nameBuilder.add(firstName).add(lastName);
                        name = nameBuilder.toString();
                    } else {
                        name = (firstName + " ") + lastName;
                    }
                    final TextView tvUserName = findViewById(R.id.tv_ard_user_name);
                    tvUserName.setText(name);

                    String imageUrl = dataSnapshot.child("image_url").getValue().toString();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        final ImageView ivUserAvatar = findViewById(R.id.iv_ard_user_avatar);
                        Picasso.with(getApplicationContext()).load(imageUrl).fit().into(ivUserAvatar);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (databaseError != null && databaseError.toException() != null) {
                        Log.d(TAG, "onCancelled: mDatabaseUserRef | exception caught", databaseError.toException());
                    } else {
                        Log.d(TAG, "onCancelled: mDatabaseUserRef | cancelled without exception");
                    }
                }
            });
        }
    }
}