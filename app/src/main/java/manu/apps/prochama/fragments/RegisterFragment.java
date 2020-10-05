package manu.apps.prochama.fragments;

import androidx.lifecycle.ViewModel;
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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.Config;
import manu.apps.prochama.viewmodels.RegisterViewModel;

public class RegisterFragment extends Fragment implements View.OnClickListener {

   private ViewModel registerViewModel;
   private TextInputLayout tilFirstName, tilLastName, tilEmail, tilPhoneNumber,
    tilPassword;

   private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etPassword;
   private ProgressBar pbRegister;
   private Button btnRegister;
   private TextView tvLogin;

   private FirebaseAuth firebaseAuth;
   private DatabaseReference databaseReference;

   NavController navController;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        etPassword = view.findViewById(R.id.et_password);

        tilFirstName = view.findViewById(R.id.til_first_name);
        tilLastName = view.findViewById(R.id.til_last_name);
        tilEmail = view.findViewById(R.id.til_email);
        tilPhoneNumber = view.findViewById(R.id.til_phone_number);
        tilPassword = view.findViewById(R.id.til_password);

        tvLogin = view.findViewById(R.id.tv_login);

        btnRegister = view.findViewById(R.id.btn_register);
        pbRegister = view.findViewById(R.id.pb_register);

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_register:

                final String firstName = etFirstName.getText().toString().trim();
                final String lastName = etLastName.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String phoneNumber = etPhoneNumber.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)){
                    tilFirstName.setError("First name is required");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    tilLastName.setError("Last name is required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    tilEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    tilPhoneNumber.setError("Phone number is required");
                    return;
                }

                if (password.length() < 8) {
                    tilPassword.setError("Password is less than 8 characters");
                    return;
                }else {

                    btnRegister.setVisibility(View.GONE);
                    pbRegister.setVisibility(View.VISIBLE);

//                if (firebaseAuth.getCurrentUser() != null) {
//                    Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
//                    //Navigation.findNavController(v).navigate(R.id.wallet_fragment);
//                }//else {

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String userId = firebaseUser.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("userId", userId);
                                hashMap.put("firstName", firstName);
                                hashMap.put("lastName", lastName);
                                hashMap.put("email", email);
                                hashMap.put("phoneNumber", phoneNumber);
                                hashMap.put("displayPic", "default");

                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Log.i("User Creation Log =====", "createUserWithEmail:success");


                                            btnRegister.setVisibility(View.VISIBLE);
                                            pbRegister.setVisibility(View.GONE);

                                            final Dialog registerDialog = new Dialog(getActivity());
                                            registerDialog.setContentView(R.layout.layout_register_dialog);
                                            registerDialog.show();
                                            registerDialog.setCancelable(false);

                                            // Setting dialog background to transparent
                                            registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            // Setting size of the dialog
                                            registerDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                                            TextView tvRegistrationMessage = registerDialog.findViewById(R.id.tv_registration_message);
                                            MaterialButton btnProceed = registerDialog.findViewById(R.id.btn_proceed);

                                            tvRegistrationMessage.setText("Your account was created successfully" + " " + firstName);

                                            btnProceed.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    navController.navigate(R.id.action_register_to_wallet_fragment);
                                                    registerDialog.dismiss();
                                                }
                                            });

                                        }else {
                                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            Config.showSnackBar(getActivity(), "We encountered an error while registering");
                                        }
                                    }
                                });

                            } else {

                                btnRegister.setVisibility(View.VISIBLE);
                                pbRegister.setVisibility(View.GONE);

                                Config.showSnackBar(getActivity(), "We encountered an error while registering");

                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

                break;
            case R.id.tv_login:

                navController.navigateUp();

                break;
            default:
                break;
        }

    }
}
