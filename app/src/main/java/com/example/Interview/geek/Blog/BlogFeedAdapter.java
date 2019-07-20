package com.example.Interview.geek.Blog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.Interview.geek.R;
import com.example.Interview.geek.Utility.GlideApp;

import java.util.ArrayList;

class BlogFeedAdapter extends RecyclerView.Adapter<BlogFeedAdapter.BlogViewHolder> implements Filterable {

    class BlogViewHolder extends RecyclerView.ViewHolder {

        private TextView title, author, date, content;
        private ImageView blogImage;

        BlogViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.blogDate);
            content = itemView.findViewById(R.id.blogSummary);
            blogImage = itemView.findViewById(R.id.blogImage);
        }
    }

    ArrayList<Blog> list, itemsFiltered;
    Context context;

    public BlogFeedAdapter(ArrayList<Blog> list, Context context) {
        this.list = list;
        this.context = context;
        itemsFiltered = new ArrayList<>();
        itemsFiltered = list;


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_view_card, viewGroup, false);
        BlogViewHolder blogViewHolder = new BlogViewHolder(v);
        return blogViewHolder;
    }

    @Override
    public void onBindViewHolder(BlogViewHolder blogViewHolder, final int i) {
        blogViewHolder.title.setText(itemsFiltered.get(i).getTitle());
        blogViewHolder.content.setText(summarize(itemsFiltered.get(i).getContent()));
        blogViewHolder.date.setText(itemsFiltered.get(i).getDate());
        blogViewHolder.author.setText(itemsFiltered.get(i).getAuthor());

        GlideApp.with(context)
                .load(itemsFiltered.get(i).getImgurl())
                .override(300, 200)
                .into(blogViewHolder.blogImage);

        blogViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetails.class);
                intent.putExtra("current", itemsFiltered.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                ArrayList<Blog> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = list;
                } else {
                    for (Blog blog : list) {
                        if (blog.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            filtered.add(blog);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                itemsFiltered = (ArrayList<Blog>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    private String summarize(String ret) {
        int l = 100;
        String back = "";
        if (ret.length() > l) back = "....";
        int x = Math.min(ret.length(), l - 1);
        return ret.substring(0, x - 1) + back;
    }

}

