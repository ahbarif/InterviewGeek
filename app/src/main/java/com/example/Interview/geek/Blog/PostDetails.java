package com.example.Interview.geek.Blog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Interview.geek.R;
import com.example.Interview.geek.Utility.GlideApp;

public class PostDetails extends AppCompatActivity {

    String testUrl = "https://firebasestorage.googleapis.com/v0/b/interview-geek.appspot.com/o/images%2Fd0dc7898-a900-41ae-bcf7-2a300814620a?alt=media&token=1a21e2b8-3fc6-4190-bf4b-1a2c6a157651";
    private LinearLayout layout;

    private TextView blogTitle, blogAuthor, blogDate, blogContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                getSupportActionBar().hide(); // hide the title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_post_details);

        layout = findViewById(R.id.blogPostImageLayout);

        blogAuthor = findViewById(R.id.author_details);
        blogContent = findViewById(R.id.contentDetails);
        blogDate = findViewById(R.id.date_details);
        blogTitle = findViewById(R.id.title_details);

        Blog blog = new Blog();

        blog = (Blog) getIntent().getSerializableExtra("current");

        populate(blog);

        String imageUrls[] = blog.getImgurl().split(" ");
        populateImageView(imageUrls);


        // Adds the view to the layout
    }

    private void populate(Blog blog){

        blogTitle.setText(blog.getTitle().trim());
        blogAuthor.setText(blog.getAuthor().trim());
        blogDate.setText(blog.getDate());
        blogContent.setText(blog.getContent());

    }

    private void populateImageView(String[] urls){
        Display display = getWindowManager().getDefaultDisplay();
        float scale =  getResources().getDisplayMetrics().density;

        for (int i = 0; i < urls.length; i++) {
            ImageView imageView = new ImageView(this);

            int width = 220*(int)scale; // ((display.getWidth()*20)/100)
            int height = 150*(int)scale;// ((display.getHeight()*30)/100)
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            parms.setMargins(0, 0, 50, 0);
            imageView.setLayoutParams(parms);


            GlideApp.with(getApplicationContext())
                    .load(urls[i]).dontAnimate()
                    .override(width, height)
                    .into(imageView);
            layout.addView(imageView);
        }
    }
}

