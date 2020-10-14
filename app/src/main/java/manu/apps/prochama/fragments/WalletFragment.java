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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.Config;
import manu.apps.prochama.classes.CustomTextWatcher;
import manu.apps.prochama.classes.GlobalVariables;
import manu.apps.prochama.classes.ThousandTextWatcher;
import manu.apps.prochama.viewmodels.WalletViewModel;

public class WalletFragment extends Fragment implements View.OnClickListener {

    private WalletViewModel walletViewModel;
    MaterialToolbar walletToolBar;
    MaterialButton btnAddMoney, btnWithdrawMoney;

    NavController navController;

    Daraja daraja;

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

        daraja = Daraja.with("PAh0e0qaAR3XbwAAMHghF9HGtTwFnmsx", "JuGnn67udzbFwxzd", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {

                Log.wtf("AccessToken++++++++++++++=================== :  ", accessToken.getAccess_token());
                /**Code below is used to display the tokens*/
                //Toast.makeText(MainActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {

                Log.wtf("AccessToken++++++++++++++===================  :  ", error);

            }
        });

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

        btnAddMoney.setOnClickListener(this);
        btnWithdrawMoney.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_add_money:

                addMoney();

                break;

            case R.id.btn_withdraw_money:

                break;
        }
    }

    private void addMoney() {

        final Dialog addMoneyDialog = new Dialog(getActivity());
        addMoneyDialog.setContentView(R.layout.layout_add_money);
        addMoneyDialog.show();

        Button btnMpesa = addMoneyDialog.findViewById(R.id.btn_mpesa);
        Button btnCheque = addMoneyDialog.findViewById(R.id.btn_cheque);

        btnMpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMoneyDialog.dismiss();

                final Dialog stkPushDialog = new Dialog(getActivity());
                stkPushDialog.setContentView(R.layout.layout_add_money_mpesa_dialog);
                stkPushDialog.setCancelable(false);
                stkPushDialog.show();

                final TextInputLayout tilAddMoneyPhoneNumber = stkPushDialog.findViewById(R.id.til_add_money_phone_number);
                final TextInputLayout tilAmount = stkPushDialog.findViewById(R.id.til_amount);

                final TextInputEditText etAddMoneyPhoneNumber = stkPushDialog.findViewById(R.id.et_add_money_phone_number);
                final EditText etAmount = stkPushDialog.findViewById(R.id.et_amount);

                etAddMoneyPhoneNumber.addTextChangedListener(new CustomTextWatcher(tilAddMoneyPhoneNumber));
                etAmount.addTextChangedListener(new CustomTextWatcher(tilAmount));

                etAmount.addTextChangedListener(new ThousandTextWatcher(etAmount));

                final MaterialButton btnDeposit = stkPushDialog.findViewById(R.id.btn_deposit);
                final MaterialButton btnCancel = stkPushDialog.findViewById(R.id.btn_cancel);
                final ProgressBar pbDeposit = stkPushDialog.findViewById(R.id.pb_deposit);

                btnDeposit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String stkPushPhoneNumber = etAddMoneyPhoneNumber.getText().toString().trim();
                        String amount = ThousandTextWatcher.trimCommaOfString(etAmount.getText().toString().trim());

                        if (TextUtils.isEmpty(stkPushPhoneNumber)) {
                            tilAddMoneyPhoneNumber.setError("Phone Number is required");
                            etAddMoneyPhoneNumber.requestFocus();
                        }

                        if (TextUtils.isEmpty(amount)) {
                            tilAmount.setError("Amount is required");

                            if (!etAddMoneyPhoneNumber.hasFocus()) {
                                etAmount.requestFocus();
                            }

                        } else {

                            btnDeposit.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            pbDeposit.setVisibility(View.VISIBLE);

                            //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
                            LNMExpress lnmExpress = new LNMExpress(
                                    "174379",
                                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",//https://developer.safaricom.co.ke/test_credentials
                                    TransactionType.CustomerPayBillOnline,
                                    amount,
                                    stkPushPhoneNumber,
                                    "174379",
                                    stkPushPhoneNumber,
                                    "http://mpesa-requestbin.herokuapp.com/1aw7lsj1",
                                    "Prochama",
                                    "Deposit"
                            );

                            daraja.requestMPESAExpress(lnmExpress,
                                    new DarajaListener<LNMResult>() {
                                        @Override
                                        public void onResult(@NonNull LNMResult lnmResult) {

                                            Log.wtf("STK Push ResponseCode ++++================: ", lnmResult.CustomerMessage);
                                            Log.wtf("STK Push ResponseDescription ++++================: ", lnmResult.ResponseDescription);
                                            Log.wtf("STK Push CustomerMessage ++++================: ", lnmResult.CustomerMessage);


                                            stkPushDialog.dismiss();
                                            Config.showSnackBar(getActivity(), "Processing Deposit");
                                        }

                                        @Override
                                        public void onError(String error) {

                                            Log.wtf("STK Push Request Error ++++================: ", error);

                                            stkPushDialog.dismiss();
                                            Config.showSnackBar(getActivity(), "We encountered an error");

                                        }
                                    }
                            );

                        }

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        stkPushDialog.dismiss();

                    }
                });

                // Setting dialog background to transparent
                stkPushDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Setting size of the dialog
                stkPushDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
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