package com.example.Interview.geek.Blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Interview.geek.R;
import com.example.Interview.geek.User.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class BlogWriting extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Button dataUploader;
    private EditText titleField, contentField;
    private ImageView blogPhoto;
    private String downloadUrl = "-1";
    private String blogTitle, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_writing);

        dataUploader = findViewById(R.id.submitButton);
        titleField = findViewById(R.id.blogTitle);
        contentField = findViewById(R.id.contentText);
        blogPhoto = findViewById(R.id.contentImage);
        blogTitle = "";
        content = "";

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        dataUploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blogTitle = titleField.getText().toString().trim();
                content = contentField.getText().toString().trim();

                if (blogTitle.length() == 0 || content.length() == 0) {
                    Toast.makeText(BlogWriting.this, "Title and content cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                uploadImage();
            }
        });


        blogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                blogPhoto.setImageBitmap(bitmap);
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
                            Toast.makeText(BlogWriting.this, downloadUrl, Toast.LENGTH_LONG).show();
                            uploadData();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(BlogWriting.this, "Error Occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void uploadData() {
       // Toast.makeText(BlogWriting.this, "Upload hobe ekhon", Toast.LENGTH_LONG).show();



        fetchData(new MyCallback() {
            @Override
            public void onCallback(User usr) {
                DatabaseReference ref = mdatabase.getReference().child("Blogs");
                String key = ref.push().getKey();

                Blog blog = new Blog();


                blog.setContent(content);
                blog.setTitle(blogTitle);
                blog.setImgurl(downloadUrl);
                blog.setId(key);
                blog.setAuthor(usr.getUsername());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date()); // Now use today date
                String current = sdf.format(c.getTime());
                blog.setDate(current);

                ref.child(key).setValue(blog);

                end();


            }
        });

        return;
    }

    private void end(){
        this.finish();
    }

    private void fetchData(final MyCallback myCallback) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        DatabaseReference ref = mdatabase.getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User usr = snapshot.child("Users").child(user.getUid()).getValue(User.class);
                progressDialog.dismiss();
                myCallback.onCallback(usr);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    private interface MyCallback {
        void onCallback(User usr);
    }
}
