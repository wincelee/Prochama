package manu.apps.prochama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private AlertDialog alertDialog;

    private Dialog connectionDialog;

    private Button btnEnableData, btnEnableWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        connectionDialog = new Dialog(this, android.R.style.Theme_NoTitleBar_Fullscreen);
        connectionDialog.setContentView(R.layout.layout_connection_dialog);
        connectionDialog.setCancelable(false);

        btnEnableData = connectionDialog.findViewById(R.id.btn_enable_data);
        btnEnableWifi = connectionDialog.findViewById(R.id.btn_enable_wifi);


        btnEnableData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Data Enabled", Toast.LENGTH_SHORT).show();
            }
        });

        btnEnableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    wifiManager.setWifiEnabled(true);
                }
                Toast.makeText(MainActivity.this, "Wifi Enabled", Toast.LENGTH_SHORT).show();
            }
        });

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (!isNetworkAvailable()) {
            connectionDialog.show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            NetworkRequest networkRequest = new NetworkRequest.Builder().build();

            networkCallback = new ConnectivityManager.NetworkCallback(){

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //update UI
                            connectionDialog.show();
                        }
                    });

                }

                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //update UI
                            if (connectionDialog.isShowing()) {
                                connectionDialog.dismiss();
                            }
                        }
                    });

//                        if (alertDialog != null){
//                            alertDialog.dismiss();
//                       }
                }
            };

            if (connectivityManager != null) {

                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
            }
        }

    }


    private boolean isNetworkAvailable(){

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable() ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }

    }
}
