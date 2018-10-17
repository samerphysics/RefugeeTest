package com.example.samr.refugeetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ////////////////////// if the user is logged in start MainActivity else Start First Activity
        if(FirebaseAuth.getInstance().getCurrentUser()== null){
            // the user is not logged in

        }else {
            // the user is logged in
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////


    }

    public void btn_register(View view) {
        Intent Intent = new Intent(this, RegisterActivity.class);
        startActivity(Intent);
        finish();
    }

    public void btn_login(View view) {
        Intent Intent = new Intent(this, LoginActivity.class);
        startActivity(Intent);
        finish();
    }

    public void btn_skip(View view) {
        Intent Intent = new Intent(this, MainActivity.class);
        startActivity(Intent);
        finish();
    }
}
