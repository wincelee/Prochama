package manu.apps.prochama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private AlertDialog alertDialog;

    private Dialog connectionDialog;

    private Button btnEnableData, btnEnableWifi;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    MaterialToolbar mainToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        mainToolBar = findViewById(R.id.main_tool_bar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.wallet_fragment)
                .setOpenableLayout(drawerLayout)
                .build();

        // Setting up Navigation Drawer, Bottom Navigation View with default action bar
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Setting up Navigation Drawer with custom toolbar
        NavigationUI.setupWithNavController(mainToolBar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.slides_fragment || destination.getId() == R.id.login_fragment ||
                        destination.getId() == R.id.register_fragment ) {

                    // If your are using your own custom toolbar
                    //topAppBar.setVisibility(View.GONE);
                    mainToolBar.setVisibility(View.GONE);

                    //getSupportActionBar().hide();

                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    );

                } else {

                    // If your are using your own custom toolbar
                    //topAppBar.setVisibility(View.VISIBLE);
                    mainToolBar.setVisibility(View.VISIBLE);

                    //getSupportActionBar().show();

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                }
            }
        });

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

    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}
