package com.it.mougang.gasmyr.takecare.view.telephony;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it.mougang.gasmyr.takecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelephonyFragment extends Fragment {


    public TelephonyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_telephony, container, false);
    }

}
