package com.codepath.tiago.nytimessearch.network;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;

/**
 * Created by tiago on 9/23/17.
 */

public class ConnectivityChecker {

    // Set tag for logging.
    private final String TAG = ConnectivityChecker.class.toString();

    private Context mContext;

    public ConnectivityChecker(Context context) {
        this.mContext = context;
    }

    private Context getContext() {
        return this.mContext;
    }

    /*
     * Returns true if there is network availability, returns false otherwise.
     */
    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    /*
     * Returns true if the device has access to internet.
     */
    public Boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            java.lang.Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) { e.printStackTrace(); }
          catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public void checkConnectivity() {
        if (!isNetworkAvailable() || (isNetworkAvailable() && !isOnline())) {
            Log.e(TAG, "Couldn't find an internet connection, dialog shown to context: " + getContext().toString());
            showAlertDialogNoInternet();
        }
    }

    public void showAlertDialogNoInternet() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setTitle("Internet connection not found");
        alertDialog.setMessage("Internet not available, please check your internet connection and try again");
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);

        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
           }
        });
        alertDialog.show();
    }
}
