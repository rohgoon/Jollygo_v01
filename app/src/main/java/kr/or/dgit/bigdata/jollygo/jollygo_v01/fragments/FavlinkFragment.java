package kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;

public class FavlinkFragment extends Fragment {



    public FavlinkFragment() {
        // Required empty public constructor
    }


    public static FavlinkFragment newInstance(String param1, String param2) {
        FavlinkFragment fragment = new FavlinkFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favlink, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


}
