package manu.apps.prochama.fragments;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.Config;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private RegisterViewModel registerViewModel;
    private TextInputLayout tilFirstName, tilLastName, tilUsername,
            tilPhoneNumber, tilPassword ;
    private TextInputEditText tietFirstName, tietLastName, tietUserName,
            tietPhoneNumber, tietPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar pbRegister;

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

        tilFirstName = view.findViewById(R.id.til_first_name);
        tilLastName = view.findViewById(R.id.til_last_name);
        tilUsername = view.findViewById(R.id.til_username);
        tilPhoneNumber = view.findViewById(R.id.til_phone_number);
        tilPassword = view.findViewById(R.id.til_password);

        tietFirstName = view.findViewById(R.id.tiet_first_name);
        tietLastName = view.findViewById(R.id.tiet_last_name);
        tietUserName = view.findViewById(R.id.tiet_email);
        tietPhoneNumber = view.findViewById(R.id.tiet_phone_number);
        tietPassword = view.findViewById(R.id.tiet_password);

        btnRegister = view.findViewById(R.id.btn_register);
        tvLogin = view.findViewById(R.id.tv_login);
        pbRegister = view.findViewById(R.id.pb_register);

        pbRegister.setVisibility(View.GONE);

        // Workings
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                //registerCustomer();

                break;
            case R.id.tv_login:
                break;
            default:
                break;
        }

    }

    private void registerCustomer() {

        pbRegister.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);

        if (tietFirstName.getText().toString().trim().isEmpty()) {
            tilFirstName.setError(getString(R.string.first_name_empty_error));
            //tietFirstName.requestFocus();
        }

        if (tietLastName.getText().toString().trim().isEmpty()) {
            tilFirstName.setError(getString(R.string.last_name_empty_error));
            //tietLastName.requestFocus();
        }

        if (tietUserName.getText().toString().trim().isEmpty()) {
            tilUsername.setError(getString(R.string.username_empty_error));
            //tietUserName.requestFocus();
        }

        if (tietPhoneNumber.getText().toString().trim().isEmpty()) {
            tilPhoneNumber.setError(getString(R.string.username_empty_error));
            //tietUserName.requestFocus();
        }

        if (tietPassword.getText().toString().trim().isEmpty()) {
            tilPassword.setError(getString(R.string.username_empty_error));
            //tietPassword.requestFocus();
        }else {
            tilFirstName.setErrorEnabled(false);
            tilLastName.setErrorEnabled(false);
            tilUsername.setErrorEnabled(false);
            tilPhoneNumber.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);

            final String firstName = tietFirstName.getText().toString().trim();
            final String lastName = tietLastName.getText().toString().trim();
            final String userName = tietUserName.getText().toString().trim();
            final String phoneNumber = tietPhoneNumber.getText().toString().trim();
            final String password = tietPassword.getText().toString().trim();

//            StringRequest registerRequest = new StringRequest(Request.Method.POST, Config.CUSTOMERS_REGISTRATION,
//                    new Response.Listener<NetworkResponse>() {
//                        @Override
//                        public void onResponse(NetworkResponse response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(new String(response.data));
//                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                pbRegister.setVisibility(View.GONE);
//                                btnRegister.setVisibility(View.VISIBLE);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//
//                                pbRegister.setVisibility(View.GONE);
//                                btnRegister.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            pbRegister.setVisibility(View.GONE);
//                            btnRegister.setVisibility(View.VISIBLE);
//                        }
//                    })
//            {
//                // Add more parameters here
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("firstName", firstName);
//                    params.put("lastName", lastName);
//                    params.put("userName", userName);
//                    params.put("phoneNumber", phoneNumber);
//                    params.put("password",password)
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//            requestQueue.add(registerRequest);

        }



    }
}
