package manu.apps.prochama.fragments;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import manu.apps.prochama.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {

   private ViewModel registerViewModel;
   private TextInputLayout tilFirstName, tilLastName, tilEmail, tilPhoneNumber,
    tilPassword;

   private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etPassword;
   private ProgressBar pbRegister;
   private Button btnRegister;
   private TextView tvLogin;

   FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        etPassword = view.findViewById(R.id.et_password);
        btnRegister = view.findViewById(R.id.btn_register);
        tvLogin = view.findViewById(R.id.tv_register);
        pbRegister = view.findViewById(R.id.pb_register);

        btnRegister.setOnClickListener(this);
        //tvLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_register:

                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)){
                    etFirstName.setError("First name is required");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    etLastName.setError("Last name is required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    etPhoneNumber.setError("Phone number is required");
                    return;
                }

                if (password.length() < 8) {
                    etPassword.setError("Password is less than 8 characters");
                    return;
                }

                btnRegister.setVisibility(View.GONE);
                pbRegister.setVisibility(View.VISIBLE);

//                if (firebaseAuth.getCurrentUser() != null) {
//                    Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
//                    //Navigation.findNavController(v).navigate(R.id.home_fragment);
//                }//else {

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            btnRegister.setVisibility(View.VISIBLE);
                            pbRegister.setVisibility(View.GONE);

                            Log.i("User Creation Log =====", "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            Toast.makeText(getActivity(), "User account has been created", Toast.LENGTH_SHORT).show();

                            Navigation.findNavController(v).navigate(R.id.login_fragment);

                        } else {
                            btnRegister.setVisibility(View.VISIBLE);
                            pbRegister.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.tv_login:
                break;
            default:
                break;
        }

    }
}
