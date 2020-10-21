package manu.apps.prochama.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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

    TextView tvWalletBalance, tvRealTime;

    FirebaseAuth firebaseAuth;

    String globalUserId;
    double globalWalletBalance;

    DatabaseReference databaseReference;

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
        tvWalletBalance = view.findViewById(R.id.tv_wallet_balance);
        tvRealTime = view.findViewById(R.id.tv_real_time);

        firebaseAuth = FirebaseAuth.getInstance();

        //databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        navController = Navigation.findNavController(view);

        MaterialToolbar walletToolBar = view.findViewById(R.id.wallet_tool_bar);

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


        Bundle arguments = getArguments();

        if (arguments != null && arguments.containsKey("SERIALIZABLE")) {

            String parsedFirstName = getArguments().getString("firstName");
            String parsedUserId = getArguments().getString("userId");
            walletToolBar.setTitle("Good Morning " + parsedFirstName);

//            new AlertDialog.Builder(getActivity())
//                    .setTitle("User ID")
//                    .setMessage(parsedUserId)
//                    .show();

            tvWalletBalance.setText("Ksh " + "0");

            globalUserId = parsedUserId;


        } else {

            walletToolBar.setTitle("Good Morning " + GlobalVariables.currentUser.getFirstName());

            tvWalletBalance.setText("Ksh " + GlobalVariables.currentUser.getWalletBalance());

            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            assert firebaseUser != null;
            String userId = firebaseUser.getUid();

//            new AlertDialog.Builder(getActivity())
//                    .setTitle("User ID")
//                    .setMessage(userId)
//                    .show();

            globalUserId = userId;

        }

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
        int id = v.getId();
        if (id == R.id.btn_add_money) {
            addMoney();
        }
        if (id == R.id.btn_withdraw_money) {
            //
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
                        final String amount = ThousandTextWatcher.trimCommaOfString(etAmount.getText().toString().trim());

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
                                    "https://mpesa-requestbin.herokuapp.com/1d11kpx1",
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

                                            double calculateWalletBalance = globalWalletBalance;

                                            calculateWalletBalance = calculateWalletBalance + Double.parseDouble(amount);

                                            updateWalletBalance(calculateWalletBalance);

                                            stkPushDialog.dismiss();

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

    private void readData() {

        // Remember to document how we are getting the user id
        databaseReference.child(globalUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    String firstName = (String) map.get("firstName");
                    String lastName = (String) map.get("lastName");
                    String phoneNumber = (String) map.get("phoneNumber");
                    double walletBalance = (Long) map.get("walletBalance");


                    new AlertDialog.Builder(getActivity())
                            .setTitle("Read Data")
                            .setMessage("FirstName: " + firstName + "\n" + "lastName: " + lastName + "\n" +
                                    "phoneNumber: " + phoneNumber + "\n" + "walletBalance: " + walletBalance)
                            .show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // References
    // Android Firebase - 10 - How Update Data in Firebase Realtime Database. - YouTube
    // Youtube Link - https://youtu.be/0HLyJNuyhSo

    private void updateWalletBalance(final double walletBalance) {

        // Deleting every detail for a current user and adding new fields
//        double walletBalance = 50.0;
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("walletBalance", walletBalance);
//
//        databaseReference.child(globalUserId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Config.showSnackBar(getActivity(), "Updated Successfully");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Config.showSnackBar(getActivity(), "Failed Please Try Again");
//            }
//        });

        final ProgressDialog updateBalanceDialog = new ProgressDialog(getActivity());
        updateBalanceDialog.setMessage("Updating..........");
        updateBalanceDialog.setCancelable(false);

        HashMap<String, Object> updateHashMap = new HashMap<>();
        updateHashMap.put("walletBalance", walletBalance);

        databaseReference.child(globalUserId).updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                tvWalletBalance.setText(String.valueOf(walletBalance));

                Config.showSnackBar(getActivity(), "Success");

                updateBalanceDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Config.showSnackBar(getActivity(), "We encountered an error try again");

                updateBalanceDialog.dismiss();

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        // Document Getting RealTime
        databaseReference.child(globalUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                double walletBalance = (Long) map.get("walletBalance");
                tvWalletBalance.setText("Ksh " + walletBalance);

                globalWalletBalance = walletBalance;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}