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

class InterviewSlotAdapter extends RecyclerView.Adapter<InterviewSlotAdapter.SlotViewHolder> {

    class SlotViewHolder extends RecyclerView.ViewHolder {

        private TextView topic, date, time;
        TextView deleteButton;

        SlotViewHolder(View itemView) {
            super(itemView);

            topic = itemView.findViewById(R.id.slotTopic);
            deleteButton = itemView.findViewById(R.id.slotDelete);
            date = itemView.findViewById(R.id.slotDate);
            time = itemView.findViewById(R.id.slotTime);

        }
    }

    ArrayList<InterviewShoutout> list;
    Context context;

    public InterviewSlotAdapter(ArrayList<InterviewShoutout> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SlotViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_card, viewGroup, false);
        SlotViewHolder blogViewHolder = new SlotViewHolder(v);
        return blogViewHolder;
    }

    @Override
    public void onBindViewHolder(SlotViewHolder blogViewHolder, final int i) {
        blogViewHolder.time.setText(list.get(i).getTime());
        blogViewHolder.date.setText(list.get(i).getDate());
        blogViewHolder.topic.setText(list.get(i).getTopic());

        blogViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete from here

                deleteNode(list.get(i).getCircularID(), i);
            }
        });
    }

    private void deleteNode(String id, int i) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Circulars");
        ref.child(uid).child(id).removeValue();
        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
        list.remove(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public int getItemCount() {
        return list.size();
    }


}

