package manu.apps.prochama.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.Config;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginViewModel loginViewModel;
    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText tietUsername, tietPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar pbLogin;

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
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilUsername = view.findViewById(R.id.til_username);
        tilPassword = view.findViewById(R.id.til_password);

        tietUsername = view.findViewById(R.id.tiet_username);
        tietPassword = view.findViewById(R.id.tiet_password);

        btnLogin = view.findViewById(R.id.btn_login);

        tvRegister = view.findViewById(R.id.tv_register);

        pbLogin = view.findViewById(R.id.pb_login);

        // TextView Workings
        tvRegister.setOnClickListener(this);

        // Button workings
        btnLogin.setOnClickListener(this);

        // Progress bar workings
        pbLogin.setVisibility(View.GONE);

        //Register workings
        tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tietUsername.addTextChangedListener(new MyTextWatcher((tietUsername)));
        tietPassword.addTextChangedListener(new MyTextWatcher(tietPassword));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:

                String userName = tietUsername.getText().toString().trim();
                String password = tietPassword.getText().toString().trim();

                login(userName, password);

                //Navigation.findNavController(v).navigate(R.id.action_login_to_deposits_fragment);

                break;
            case R.id.tv_register:

//                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                Navigation.findNavController(v).navigate(R.id.action_login_to_register_fragment);

                break;
            default:
                break;

        }

    }

    private void login(final String userName, final String password){
        pbLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        StringRequest loginRequest = new StringRequest(Request.Method.POST, Config.CUSTOMERS_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {
                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String username = object.getString("username").trim();
                                    String email = object.getString("email").trim();
                                    //String password = object.getString("password").trim();
                                    String id = object.getString("id").trim();

                                    Navigation.findNavController(getView()).navigate(R.id.action_login_to_deposits_fragment);


//                                    userSessionManager.createSession(username, email,id);

//                                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                                    intent.putExtra("username",username);
//                                    intent.putExtra("email", email);
//                                    startActivity(intent);
//                                    getActivity().finish();

                                    pbLogin.setVisibility(View.GONE);
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            pbLogin.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Json Error", Toast.LENGTH_SHORT).show();
                            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), e.toString(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbLogin.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Volley Error", Toast.LENGTH_SHORT).show();
                        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), error.toString(),Snackbar.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName",userName);
                params.put("password",password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(loginRequest);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            validateUsernamePassword();
        }
    }


    private  void validateUsernamePassword() {
        if(tietUsername.getText().toString().isEmpty() && tietPassword.getText().toString().isEmpty()) {
            tilUsername.setError(getString(R.string.username_empty_error));
            tilPassword.setError(getString(R.string.password_empty_error));
            tilUsername.setErrorTextColor(getResources().getColorStateList(R.color.White100));
            tilPassword.setErrorTextColor(getResources().getColorStateList(R.color.White100));
        }
        if (!(tietUsername.getText().toString().isEmpty()) && tietPassword.getText().toString().isEmpty()) {
            tilPassword.setError(getString(R.string.password_empty_error));
            tilPassword.setErrorTextColor(getResources().getColorStateList(R.color.White100));
            tilUsername.setErrorEnabled(false);
        }
        if (!(tietPassword.getText().toString().isEmpty()) && tietUsername.getText().toString().isEmpty()) {
            tilUsername.setError(getString(R.string.username_empty_error));
            tilUsername.setErrorTextColor(getResources().getColorStateList(R.color.White100));
            tilPassword.setErrorEnabled(false);
        }
        if (!(tietUsername.getText().toString().isEmpty()) && !(tietPassword.getText().toString().isEmpty())) {

            tilUsername.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);
        }
    }

}
