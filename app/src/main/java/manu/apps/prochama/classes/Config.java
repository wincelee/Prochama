package manu.apps.prochama.classes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

import manu.apps.prochama.R;

public class Config {

    public static String numberFormatter(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(d);
    }

    public static void checkInternetConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(context, "Wifi Enabled", Toast.LENGTH_SHORT).show();
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(context, "Data Network Enabled", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showSnackBar(Context context, String msg) {


        Snackbar snackbar = Snackbar.make(((Activity)context).getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_SHORT);
        /**Changing TextColor of the info in SnackBar*/
        View snackView = snackbar.getView();

        /**Setting background color to the snack bar*/
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        snackbar.show();
    }
}
