package com.example.Interview.geek.User;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Interview.geek.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class OtherInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_info, container, false);
    }

    // txtView.setText(Html.fromHtml("<u>underlined</u> text"));
}
