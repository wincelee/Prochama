package manu.apps.prochama.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.GlobalVariables;
import manu.apps.prochama.viewmodels.WalletViewModel;

public class WalletFragment extends Fragment {

    private WalletViewModel walletViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.wallet_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);

        Log.wtf("++++++++++++++++++++==========================First Name: ", GlobalVariables.currentUser.getFirstName());
        Log.wtf("++++++++++++++++++++==========================Last Name: ", GlobalVariables.currentUser.getLastName());
        Log.wtf("++++++++++++++++++++==========================Email: ", GlobalVariables.currentUser.getEmail());
        Log.wtf("++++++++++++++++++++==========================Phone Number: ", GlobalVariables.currentUser.getPhoneNumber());

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
//        builder1.setMessage("First Name: " + GlobalVariables.currentUser.getFirstName());
//        builder1.setMessage("Last Name: " + GlobalVariables.currentUser.getLastName());
//        builder1.setMessage("Email: " + GlobalVariables.currentUser.getEmail());
//        builder1.setMessage("Phone Number: " + GlobalVariables.currentUser.getPhoneNumber());
//        builder1.setCancelable(true);
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();
    }
}