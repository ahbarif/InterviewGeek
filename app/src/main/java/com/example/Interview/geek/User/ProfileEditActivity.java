package com.example.Interview.geek.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interview.geek.Blog.BlogWriting;
import com.example.Interview.geek.R;
import com.example.Interview.geek.Utility.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText pName, pIns, pBio, pEmail, pPhone, pAddress, pDob;
    private EditText pCF, pHR, pTC, pUva, pCodechef, pGit, pLinked;
    User currentUser;
    private FirebaseDatabase mdatabase;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private String downloadUrl = "-1";
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Button button;
    private ImageView profilePic;
    private boolean hasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide(); // hide the title bar
//
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_profile_edit);

        currentUser = new User();
        currentUser = (User) getIntent().getSerializableExtra("current");

        Toast.makeText(ProfileEditActivity.this, "paisi = " + currentUser.getName(), Toast.LENGTH_LONG).show();

        pName = findViewById(R.id.eTName);
        pIns = findViewById(R.id.eTInstitution);
        pBio = findViewById(R.id.eTBio);
        pEmail = findViewById(R.id.eTEmail);
        pPhone = findViewById(R.id.eTPhone);
        pAddress = findViewById(R.id.eTAddress);
        pDob = findViewById(R.id.eTDOB);
        button = findViewById(R.id.updateButton);

        pCF = findViewById(R.id.etCodeforces);
        pHR = findViewById(R.id.etHackerrank);
        pTC = findViewById(R.id.etTopcoder);
        pCodechef = findViewById(R.id.etCodechef);
        pUva = findViewById(R.id.etUva);

        pGit = findViewById(R.id.etGithub);
        pLinked = findViewById(R.id.etLinkedin);


        downloadUrl = currentUser.getImgurl();

        profilePic = findViewById(R.id.eTprofile_image);

        mdatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (downloadUrl != null && downloadUrl.equals("N/A") == false) {
            GlideApp.with(getApplicationContext())
                    .load(currentUser.getImgurl()).dontAnimate()
                    .override(100, 100)
                    .into(profilePic);
        }

        populate();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Posting...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());


            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileEditActivity.this, downloadUrl, Toast.LENGTH_LONG).show();
                            updateProfile();
                            hasChanged = true;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileEditActivity.this, "Error Occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Updating " + (int) progress + "%");
                        }
                    });
        } else {
            updateProfile();
        }
    }


    private void populate() {
        if (currentUser.getName() != null)
            pName.setText(currentUser.getName(), TextView.BufferType.EDITABLE);
        if (currentUser.getInstitution() != null) pIns.setText(currentUser.getInstitution());
        if (currentUser.getBio() != null) pBio.setText(currentUser.getBio());
        if (currentUser.getEmail() != null) pEmail.setText(currentUser.getEmail());
        if (currentUser.getPhone_number() != null) pPhone.setText(currentUser.getPhone_number());
        if (currentUser.getAddress() != null) pAddress.setText(currentUser.getAddress());
        if (currentUser.getDob() != null) pDob.setText(currentUser.getDob());


        pCF.setText(currentUser.getCf_handle());
        pCodechef.setText(currentUser.getCodechef_handle());
        pHR.setText(currentUser.getHackerrank_handle());
        pUva.setText(currentUser.getUhunt_handle());
        pTC.setText(currentUser.getTop_coder_handle());

        pGit.setText(currentUser.getGithub());
        pLinked.setText(currentUser.getLinkedin());
    }

    private void updateProfile() {

        String name = pName.getText().toString().trim();
        String ins = pIns.getText().toString().trim();
        String bio = pBio.getText().toString().trim();
        String email = pEmail.getText().toString().trim();
        String phone = pPhone.getText().toString().trim();
        String address = pAddress.getText().toString().trim();
        String DOB = pDob.getText().toString().trim();

        String codeforces = pCF.getText().toString().trim();
        String codechef = pCodechef.getText().toString().trim();
        String hackerrank = pHR.getText().toString().trim();
        String UVa = pUva.getText().toString().trim();
        String topcoder = pTC.getText().toString().trim();

        String gitLink = pGit.getText().toString().trim();
        String linkedinLink = pLinked.getText().toString().trim();


        currentUser.setName(name);
        currentUser.setInstitution(ins);
        currentUser.setBio(bio);
        currentUser.setEmail(email);
        currentUser.setPhone_number(phone);
        currentUser.setAddress(address);
        currentUser.setDob(DOB);

        currentUser.setCf_handle(codeforces);
        currentUser.setCodechef_handle(codechef);
        currentUser.setHackerrank_handle(hackerrank);
        currentUser.setTop_coder_handle(topcoder);
        currentUser.setUhunt_handle(UVa);

        currentUser.setGithub(gitLink);
        currentUser.setLinkedin(linkedinLink);


        Toast.makeText(ProfileEditActivity.this, "do = " + downloadUrl, Toast.LENGTH_LONG).show();
        currentUser.setImgurl(downloadUrl);

        DatabaseReference ref = mdatabase.getReference().child("Users");
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.child(key).setValue(currentUser);
        this.finish();
    }


}