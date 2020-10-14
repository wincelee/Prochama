package manu.apps.prochama.fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.GlobalVariables;
import manu.apps.prochama.classes.User;
import manu.apps.prochama.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginViewModel loginViewModel;
    private ProgressBar pbLogin;
    private TextInputLayout tilEmail, tilPassword;
    private EditText etEmail, etPassword;
    private TextView tvRegister;

    private Button btnLogin;

    private FirebaseAuth firebaseAuth;

    private NavController navController;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);


        tilEmail = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        tvRegister = view.findViewById(R.id.tv_register);
        pbLogin = view.findViewById(R.id.pb_login);

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        //checkInternetConnection();


    }

    @Override

    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.btn_login:

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    tilEmail.setError("Email is required");
                }

                if (TextUtils.isEmpty(password)){
                    tilPassword.setError("Password is required");
                }

                if (password.length() < 8) {
                    etPassword.setText("Password must be greater that 8 characters");
                }else {

                    btnLogin.setVisibility(View.GONE);
                    pbLogin.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Log.i("User Login Log =====", "loginUserWithEmail:success");

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                GlobalVariables.currentUser = snapshot.getValue(User.class);

                                                Toast.makeText(getActivity(), "Login is successful", Toast.LENGTH_SHORT).show();

                                                navController.navigate(R.id.action_login_to_wallet_fragment);

                                                btnLogin.setVisibility(View.VISIBLE);
                                                pbLogin.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                            } else {

                                btnLogin.setVisibility(View.VISIBLE);
                                pbLogin.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Error !" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                break;

            case R.id.tv_register:

//                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                tvRegister.setTextColor(getResources().getColor(R.color.colorCasey));
                Navigation.findNavController(getView()).navigate(R.id.action_login_to_register_fragment);

                break;
            default:
                break;

        }

    }


    private void checkInternetConnection() {

        Snackbar snackbar;


        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if (activeNetwork.getType() == connectivityManager.TYPE_WIFI) {
                snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                        "Wifi Enabled", Snackbar.LENGTH_SHORT);
                View snackView = snackbar.getView();
                /**Setting background color to the snack bar*/
                snackView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorCasey));
                snackbar.show();
            }
            if (activeNetwork.getType() == connectivityManager.TYPE_MOBILE) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                        "Data Network Enabled", Snackbar.LENGTH_SHORT).show();

            }
        } else {
            snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                    "No internet connection", Snackbar.LENGTH_LONG);
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorCasey));
            snackbar.show();
        }
    }


}
