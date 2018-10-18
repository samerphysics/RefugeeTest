package com.example.samr.refugeetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_R_ProfileImage;
    EditText et_R_FirstName;
    EditText et_R_LastName;
    EditText et_R_Age;
    EditText et_R_Email;
    EditText et_R_Password;
    CheckBox cbTerms;

    ProgressBar progressBar;

    private RadioGroup radioGroup;

    String gender;

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;

    FirebaseAuth mAuth;

    Uri imageHoldUri = null;

    //FIRE BASE DATABASE FIELDS
    DatabaseReference mUserDatabase;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.my_action_bar);
        setSupportActionBar(toolbar);

        iv_R_ProfileImage = findViewById(R.id.iv_R_ProfileImage);
        et_R_FirstName = findViewById(R.id.et_R_FirstName);
        et_R_LastName = findViewById(R.id.et_R_LastName);
        et_R_Age = findViewById(R.id.et_R_Age);
        et_R_Email = findViewById(R.id.et_R_Email);
        et_R_Password = findViewById(R.id.et_R_Password);
        cbTerms = findViewById(R.id.cbTerms);

        progressBar = findViewById(R.id.progressBar);

        //ASSIGN INSTANCE TO FIRE BASE AUTH
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnRegister).setOnClickListener(this);

        radioGroup = findViewById(R.id.radioGroup);

        /////////////////////////////////////////////////////////// USER IMAGE VIEW ONCLICK LISTENER
        iv_R_ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LOGIC FOR PROFILE PICTURE
                profilePicSelection();

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    private void registerUser() {

        if (imageHoldUri == null) {
            Toast.makeText(this, "Please select profile image", Toast.LENGTH_LONG).show();
            return;
        }

        String firstNameCheck = et_R_FirstName.getText().toString().trim();
        final String lastNameCheck = et_R_LastName.getText().toString().trim();
        String ageCheck = et_R_Age.getText().toString().trim();

        if (firstNameCheck.isEmpty()) {
            et_R_FirstName.setError("First name is required");
            et_R_FirstName.requestFocus();
            return;
        }

        if (lastNameCheck.isEmpty()) {
            et_R_LastName.setError("Last name is required");
            et_R_LastName.requestFocus();
            return;
        }

        if (ageCheck.isEmpty()) {
            et_R_Age.setError("Age is required");
            et_R_Age.requestFocus();
            return;
        }

        String email = et_R_Email.getText().toString().trim();
        String password = et_R_Password.getText().toString().trim();

        if (email.isEmpty()) {
            et_R_Email.setError("Email is required");
            et_R_Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_R_Email.setError("Please enter a valid email");
            et_R_Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_R_Password.setError("Password is required");
            et_R_Password.requestFocus();
            return;
        }

        if (password.length() < 8) {
            et_R_Password.setError("Minimum length of password should be 8");
            et_R_Password.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            cbTerms.setError("");
            Toast.makeText(RegisterActivity.this, "You have to agree on the terms and conditions!", Toast.LENGTH_LONG).show();
            return;
        }

        ////////////////////////////////////////////////// get selected radio button from radioGroup
        RadioButton radioButton;

        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = findViewById(selectedId);

        gender = (String) radioButton.getText();
        ////////////////////////////////////////////////////////////////////////////////////////////

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).

                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            //FIRE BASE DATABASE INSTANCE
                            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                            mStorageRef = FirebaseStorage.getInstance().getReference();

                            ///////////////////////////////////////// Save user info to fire base db
                            if (imageHoldUri != null) {

                                final String firstName = et_R_FirstName.getText().toString().trim();
                                final String lastName = et_R_LastName.getText().toString().trim();
                                final String age = et_R_Age.getText().toString().trim();

                                StorageReference mChildStorage = mStorageRef.child("profile_picture").child(Objects.requireNonNull(imageHoldUri.getLastPathSegment()));
//                                String profilePicUrl = imageHoldUri.getLastPathSegment();

                                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        final Uri imageUrl = taskSnapshot.getDownloadUrl();

                                        mUserDatabase.child("first_name").setValue(firstName);
                                        mUserDatabase.child("last_name").setValue(lastName);
                                        mUserDatabase.child("age").setValue(age);
                                        mUserDatabase.child("gender").setValue(gender);
                                        mUserDatabase.child("user_id").setValue(mAuth.getCurrentUser().getUid());
                                        assert imageUrl != null;
                                        mUserDatabase.child("image_url").setValue(imageUrl.toString());

                                    }
                                });
                            }
                            ////////////////////////////////////////////////////////////////////////

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong, try again please!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
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
                iv_R_ProfileImage.setImageURI(imageHoldUri);

            }
//            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Toast.makeText(RegisterActivity.this, (CharSequence) error, Toast.LENGTH_LONG).show();
//            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent Intent = new Intent(RegisterActivity.this, FirstActivity.class);
        startActivity(Intent);
        finish();
    }

    public void iv_back_arrow(View view) {
        Intent Intent = new Intent(RegisterActivity.this, FirstActivity.class);
        startActivity(Intent);
        finish();
    }

}