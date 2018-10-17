package com.example.samr.refugeetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RequestServiceActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_request_image;
    TextView et_request_title;
    TextView et_request_description;
    TextView et_requesting_price;
    TextView et_requesting_time;
    Button btnRequestService;
    ProgressBar progressBar;

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;

    FirebaseAuth mAuth;

    Uri imageHoldUri = null;

    //FIRE BASE DATABASE FIELDS
    DatabaseReference mUserDatabse;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);

        iv_request_image = findViewById(R.id.iv_request_image);
        et_request_title = findViewById(R.id.et_request_title);
        et_request_description = findViewById(R.id.et_request_description);
        et_requesting_price = findViewById(R.id.et_requesting_price);
        et_requesting_time = findViewById(R.id.et_requesting_time);
        btnRequestService = findViewById(R.id.btnRequestService);

        progressBar = findViewById(R.id.progressBar);

        //ASSIGN INSTANCE TO FIRE BASE AUTH
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnRequestService).setOnClickListener(this);

        iv_request_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LOGIC FOR PROFILE PICTURE
                profilePicSelection();
            }
        });

    }

    private void sellServiceUpload() {

        if (imageHoldUri == null) {
            Toast.makeText(this, "Please select service image", Toast.LENGTH_LONG).show();
            return;
        }

        String titleCheck = et_request_title.getText().toString().trim();
        String descriptionCheck = et_request_description.getText().toString().trim();
        String priceCheck = et_requesting_price.getText().toString().trim();
        String timeCheck = et_requesting_time.getText().toString().trim();

        if (titleCheck.isEmpty()) {
            et_request_title.setError("Title is required");
            et_request_title.requestFocus();
            return;
        }

        if (descriptionCheck.isEmpty()) {
            et_request_description.setError("Description is required");
            et_request_description.requestFocus();
            return;
        }

        if (priceCheck.isEmpty()) {
            et_requesting_price.setError("Requesting Price is required");
            et_requesting_price.requestFocus();
            return;
        }

        if (timeCheck.isEmpty()) {
            et_requesting_time.setError("Requesting Time is required");
            et_requesting_time.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        final String request_title = et_request_title.getText().toString().trim();
        final String request_description = et_request_description.getText().toString().trim();
        final String request_price = et_requesting_price.getText().toString().trim();
        final String request_time = et_requesting_time.getText().toString().trim();

        //FIRE BASE DATABASE INSTANCE
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Request_Service").child(mAuth.getCurrentUser().getUid() + request_title);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ///////////////////////////////////////// Save sell service info to fire base db
        if (imageHoldUri != null) {

            StorageReference mChildStorage = mStorageRef.child("request_service_picture").child(imageHoldUri.getLastPathSegment());
//                                String profilePicUrl = imageHoldUri.getLastPathSegment();

            mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri imageUrl = taskSnapshot.getDownloadUrl();

                    mUserDatabse.child("request_title").setValue(request_title);
                    mUserDatabse.child("request_description").setValue(request_description);
                    mUserDatabse.child("request_price").setValue(request_price);
                    mUserDatabse.child("request_time").setValue(request_time);
                    mUserDatabse.child("request_user_id").setValue(mAuth.getCurrentUser().getUid());
                    mUserDatabse.child("request_image_url").setValue(imageUrl.toString());

                    Toast.makeText(getApplicationContext(), "Requesting service have saved successfully!", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);

                }
            });
        }
        ////////////////////////////////////////////////////////////////////////

        Intent intent = new Intent(RequestServiceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    private void profilePicSelection() {


        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                iv_request_image.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRequestService:
                sellServiceUpload();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent Intent = new Intent(RequestServiceActivity.this, MainActivity.class);
        startActivity(Intent);
        finish();
    }

    public void iv_back_arrow(View view) {
        Intent Intent = new Intent(RequestServiceActivity.this, MainActivity.class);
        startActivity(Intent);
        finish();
    }

}