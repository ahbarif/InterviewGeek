package com.example.Interview.geek.Blog;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.Interview.geek.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class BlogFeedActivity extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private FirebaseUser user;
    private ArrayList<Blog> blogList;
    private SearchView searchView;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_feed);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();
        blogList = new ArrayList<>();
        recyclerView = findViewById(R.id.blogFeedRecycler);
        searchView = findViewById(R.id.blogSearchView);

        fetchData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<Blog> list) {
                String ret = "data = ";
                for (int i = 0; i < list.size(); i++) {
                    ret = ret + list.get(i).getTitle();
                    blogList.add(list.get(i));
                }


                Toast.makeText(BlogFeedActivity.this, ret, Toast.LENGTH_LONG).show();

                populateFeed();

            }
        });


    }

    private void populateFeed(){

        // eikhane adapter e array pathabo. simple
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Collections.reverse(blogList);

        final BlogFeedAdapter adapter = new BlogFeedAdapter(blogList, this.getApplicationContext());
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (blogList.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(BlogFeedActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private void fetchData(final MyCallback myCallback) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        DatabaseReference ref = mdatabase.getReference();
        ref.child("Blogs").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Blog> list = new ArrayList<>();

                for (DataSnapshot blogSnapshot : dataSnapshot.getChildren()) {
                    Blog blog = blogSnapshot.getValue(Blog.class);
                    list.add(blog);
                }
                progressDialog.dismiss();

                myCallback.onCallback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BlogFeedActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private interface MyCallback {
        void onCallback(ArrayList<Blog> list);
    }
}
