package com.example.luba.nytimessearch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.luba.nytimessearch.R;

/**
 * Created by luba on 9/22/17.
 */

public class FilterFragment extends DialogFragment{


    public FilterFragment() {
        // empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        Button btnApplyFilter = (Button)v.findViewById(R.id.btn_apply_filter);
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();

            }
        });



        return v;
    }

}
