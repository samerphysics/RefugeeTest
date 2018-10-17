package com.example.samr.refugeetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    ImageView ivNavBarUserProfileImage;

    //add Fire base Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private String first_name;
    private String last_name;
    private String age;
    private String gender;
    private String image_url;
    private String email;

    private TextView tvFirstName;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //////////////////////////////////////////////////////////// To hide app name in the toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        ////////////////////////////////////////////////////////////////////////////////////////////

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    // the user is not logged in
                    Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // the user is logged in
                    Fragment ServicesFragment = getSupportFragmentManager().findFragmentByTag("ServicesFragment");
                    if (ServicesFragment != null && ServicesFragment.isVisible()) {
                        // add your code here
                        Intent intent = new Intent(MainActivity.this, SellServiceActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(MainActivity.this, RequestServiceActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //////////////////////////////////////////////////////////////////////// Start Main Fragment
        Fragment fragment = new ServicesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screen_area, fragment, "ServicesFragment");
        fragmentTransaction.commit();
        ////////////////////////////////////////////////////////////////////////////////////////////


//        View headerView = navigationView.getHeaderView(0);
//        tvFirstName = headerView.findViewById(R.id.tvFirstName);
//        tvEmail = headerView.findViewById(R.id.etEmail);

//        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);
//        ivNavBarUserProfileImage = hView.findViewById(R.id.ivNavBarUserProfileImage);

        //        TextView tv = (TextView)hView.findViewById(R.id.tvFirstName);
//        imgvw .setImageResource();
//        tv.settext("new text");


        ////////////////////////////////////////////////////// check if the user is logged in or not
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // the user is not logged in
            hideItems();

        } else {

            // the user is logged in
            //declare the database reference object. This is what we use to access the database.
            //NOTE: Unless you are signed in, this will not be usable.
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();
            firebaseUser = mAuth.getCurrentUser();
            userID = firebaseUser.getUid();

//            Toast.makeText(this, userID, Toast.LENGTH_LONG).show();

            email = firebaseUser.getEmail();

//            Toast.makeText(this, email, Toast.LENGTH_LONG).show();

//            tvFirstName.setText(first_name);
//            tvEmail.setText(email);


//        ivNavBarUserProfileImage.setImageDrawable();

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
//                    showData(dataSnapshot);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            UserInformation uInfo = new UserInformation();
            uInfo.setFirst_name(ds.child(userID).getValue(UserInformation.class).getFirst_name()); //set the first name
            uInfo.setLast_name(ds.child(userID).getValue(UserInformation.class).getLast_name()); //set the last name
            uInfo.setAge(ds.child(userID).getValue(UserInformation.class).getAge()); //set the age

            uInfo.setGender(ds.child(userID).getValue(UserInformation.class).getGender()); //set the gender
            uInfo.setImage_url(ds.child(userID).getValue(UserInformation.class).getImage_url()); //set the image url

            first_name = uInfo.getFirst_name();
            last_name = uInfo.getLast_name();
            age = uInfo.getAge();
            gender = uInfo.getGender();
            image_url = uInfo.getImage_url();

            ////////////////////////////////////////////////////////////// set nav text to user name

            Picasso.with(MainActivity.this).load(image_url).placeholder(R.drawable.default_profile_image).into(ivNavBarUserProfileImage);

//            Toast.makeText(this, first_name, Toast.LENGTH_LONG).show();

//                tvFirstName.setText(first_name);
//                tvEmail.setText(email);
//                tvFirstName.setText(first_name + " " + last_name);

            ////////////////////////////////////////////////////////////////////////////////////////

            //display all the information
//            Log.d(TAG, "showData: name: " + uInfo.getName());
//            Log.d(TAG, "showData: email: " + uInfo.getEmail());
//            Log.d(TAG, "showData: phone_num: " + uInfo.getPhone_num());
//
//            ArrayList<String> array  = new ArrayList<>();
//            array.add(uInfo.getName());
//            array.add(uInfo.getEmail());
//            array.add(uInfo.getPhone_num());
//            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
//            mListView.setAdapter(adapter);
        }
    }

    private void hideItems() {
        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_user_profile).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_services) {

            Fragment fragment = new ServicesFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screen_area, fragment, "ServicesFragment");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_requests) {

            Fragment fragment = new RequestsFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screen_area, fragment, "RequestsFragment");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_testActivity) {
            Intent intent = new Intent(MainActivity.this, Retrieve.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_sell_service) {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // the user is not logged in
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(MainActivity.this, RequestServiceActivity.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.nav_request_service) {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // the user is not logged in
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(MainActivity.this, RequestServiceActivity.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.nav_user_profile) {

            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_exit) {

            finish();

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // the user is not logged in
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();

            } else {
                // the user is logged in
                Toast.makeText(MainActivity.this, "An error happened try again please!", Toast.LENGTH_LONG).show();
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}