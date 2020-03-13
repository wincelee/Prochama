package manu.apps.prochama.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import manu.apps.prochama.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositsFragment extends Fragment {

    public DepositsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deposits, container, false);
    }
}
