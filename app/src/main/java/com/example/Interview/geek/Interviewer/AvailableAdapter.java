package com.example.Interview.geek.Interviewer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.Interview.geek.R;
import com.example.Interview.geek.Utility.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class AvailableAdapter extends RecyclerView.Adapter<AvailableAdapter.SlotViewHolder> implements Filterable{

    class SlotViewHolder extends RecyclerView.ViewHolder {

        private TextView topic, date, time, mail;
        Button requestButton;

        SlotViewHolder(View itemView) {
            super(itemView);

            topic = itemView.findViewById(R.id.slotTopic);
            requestButton = itemView.findViewById(R.id.slot_request);
            date = itemView.findViewById(R.id.slotDate);
            time = itemView.findViewById(R.id.slotTime);
            mail = itemView.findViewById(R.id.slotEmail);

        }
    }

    ArrayList<InterviewShoutout> list, itemsFiltered;
    Context context;

    public AvailableAdapter(ArrayList<InterviewShoutout> list, Context context) {
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
    public SlotViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.availabe_card, viewGroup, false);
        SlotViewHolder blogViewHolder = new SlotViewHolder(v);
        return blogViewHolder;
    }

    @Override
    public void onBindViewHolder(SlotViewHolder blogViewHolder, final int i) {
        blogViewHolder.time.setText(itemsFiltered.get(i).getTime());
        blogViewHolder.date.setText(itemsFiltered.get(i).getDate());
        blogViewHolder.topic.setText(itemsFiltered.get(i).getTopic());
        blogViewHolder.mail.setText(itemsFiltered.get(i).getEmail());

        blogViewHolder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request(list.get(i).getEmail(), i);
            }
        });
    }

    private void Request(String id, int i) {
        Intent intent = new Intent(context, SendEmailActivity.class);
        intent.putExtra("shout", list.get(i));
        context.startActivity(intent);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                ArrayList<InterviewShoutout> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = list;
                } else {
                    for (InterviewShoutout blog : list) {
                        if (blog.getTopic().toLowerCase().contains(query.toLowerCase())) {
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
                itemsFiltered = (ArrayList<InterviewShoutout>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }


}

