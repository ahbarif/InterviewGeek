package com.example.Interview.geek.Contest.Reminder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interview.geek.R;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import static java.security.AccessController.getContext;

public class UpcomingContestActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_contest);

        new RetrieveFeedTask().execute();

        recyclerView = (RecyclerView) findViewById(R.id.upComingContestList);
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Pre execution", Toast.LENGTH_SHORT).show();
        }

        protected String doInBackground(Void... urls) {

            try {

	// code of time generation here
                String link = "https://clist.by:443/api/v1/xml/contest/?username=Ahb_arif&api_key="...................api key here"&end__gte=2019-04-25T00%3A00%3A00&end__lte=2019-04-28T00%3A00%3A00";

                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
            try {
                setData(response);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(String response) throws ParserConfigurationException, SAXException, IOException {

        ContestXMLStringParser parser = new ContestXMLStringParser();

        ArrayList<UpcomingContest> list = parser.getContestList(response);


        ArrayList<UpcomingContest> allowed = new ArrayList<>();

        for (UpcomingContest contest : list) {
            String site = contest.getWebsite();
            if (site.equals("codechef.com") || site.equals("topcoder.com") || site.equals("codeforces.com") || site.equals("hackerrank.com")) {
                allowed.add(contest);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        ContestFeedAdapter adapter = new ContestFeedAdapter(allowed, this.getApplicationContext());
        recyclerView.setAdapter(adapter);


    }


}
