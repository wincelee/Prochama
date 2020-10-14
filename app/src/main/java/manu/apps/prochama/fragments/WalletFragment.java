package manu.apps.prochama.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.GlobalVariables;
import manu.apps.prochama.viewmodels.WalletViewModel;

public class WalletFragment extends Fragment implements View.OnClickListener {

    private WalletViewModel walletViewModel;
    MaterialToolbar walletToolBar;
    MaterialButton btnAddMoney, btnWithdrawMoney;

    NavController navController;

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

        walletToolBar = view.findViewById(R.id.wallet_tool_bar);

        navController = Navigation.findNavController(view);

        btnAddMoney = view.findViewById(R.id.btn_add_money);
        btnWithdrawMoney = view.findViewById(R.id.btn_withdraw_money);

//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//
//        NavigationUI.setupWithNavController(
//                walletToolBar, navController, appBarConfiguration);

        walletToolBar.setTitle("Good Morning " + GlobalVariables.currentUser.getFirstName());

        ((AppCompatActivity) getActivity()).setSupportActionBar(walletToolBar);

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

        btnAddMoney.setOnClickListener(this);
        btnWithdrawMoney.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_money:
                addMoney();
                break;
            case R.id.btn_withdraw_money:
                break;
        }
    }

    private void addMoney(){

        Dialog addMoneyDialog = new Dialog(getActivity());
        addMoneyDialog.setContentView(R.layout.layout_add_money);
        addMoneyDialog.show();

        Button btnMpesa = addMoneyDialog.findViewById(R.id.btn_mpesa);
        Button btnCheque = addMoneyDialog.findViewById(R.id.btn_cheque);

        btnMpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Setting dialog background to transparent
        addMoneyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Setting size of the dialog
        addMoneyDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

    }
}