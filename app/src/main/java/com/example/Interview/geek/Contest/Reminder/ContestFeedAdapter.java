package com.example.Interview.geek.Contest.Reminder;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interview.geek.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContestFeedAdapter extends RecyclerView.Adapter<ContestFeedAdapter.ContestViewHolder> {

    ArrayList<UpcomingContest> list;
    Context context;

    public ContestFeedAdapter(ArrayList<UpcomingContest> list, Context context){
        this.list = list;
        this.context = context;
    }

    class ContestViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView contestName;
        TextView contestWebsite;
        TextView contestStatus;
        TextView contestStartDate, contestEndDate;
        TextView contestStartMonth, contestEndMonth;
        TextView contestStartTime, contestEndTime;

        ContestViewHolder(View itemView) {

            super(itemView);

            contestName = (TextView)itemView.findViewById(R.id.contestName);
            contestWebsite = (TextView)itemView.findViewById(R.id.contestWebsite);
            contestStatus = itemView.findViewById(R.id.contestStatus);

            contestStartDate = (TextView)itemView.findViewById(R.id.contestStartDate);   // big read
            contestEndDate = (TextView)itemView.findViewById(R.id.contestEndDate);

            contestStartMonth = itemView.findViewById(R.id.contestStartMonth);
            contestEndMonth = itemView.findViewById(R.id.contestEndMonth);

            contestStartTime = itemView.findViewById(R.id.contestStartTime);
            contestEndTime = itemView.findViewById(R.id.contestEndTime);
        }
    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ContestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_contest_card, viewGroup, false);
        ContestViewHolder cvh = new ContestViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ContestViewHolder cvh, final int i) {
        cvh.contestName.setText(list.get(i).getName());
        cvh.contestWebsite.setText(list.get(i).getWebsite());
        String[] date1 = new String[3];
        String[] date2 = new String[3];

        try {
            date1 = parseTime(list.get(i).getStart());
            date2 = parseTime(list.get(i).getEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cvh.contestStartDate.setText(date1[0]);
        cvh.contestEndDate.setText(date2[0]);

        cvh.contestStartMonth.setText(date1[1]);
        cvh.contestEndMonth.setText(date2[1]);


        cvh.contestStartTime.setText(date1[2]);
        cvh.contestEndTime.setText(date2[2]);

        cvh.contestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String href = list.get(i).getLink();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
                context.startActivity(browserIntent);
            }
        });



//                TextView ;
//        TextView ;
//        TextView contestStatus;
//        TextView , contestEndDate;
//        TextView contestStartMonth, contestEndMonth;
//        TextView contestStartTime, contestEndTime;

    }


    private String[] parseTime(String t) throws ParseException {

        // 2019-03-09T19:00:00
        String str[] = new String[3];
        String Month[] = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String parts[] = t.split("T");

        String date[] = parts[0].split("-");
        String time[] = parts[1].split(":");

        for(int i = 0; i<3; i++) str[i] = new String();

        //date, month(from array), year, time

        str[0] = date[2];
        str[1] = Month[Integer.valueOf(date[1])] + " " + date[0];


        str[2] = time[0] + ":" + time[1];

        return str;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}