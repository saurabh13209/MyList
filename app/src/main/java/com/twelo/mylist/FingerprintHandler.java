package com.twelo.mylist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Region;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private int task, position;
    private AlertDialog dialog;
    private CheckBox checkBox;

    FingerprintHandler(Context context, int Task, AlertDialog dialog, int position , CheckBox checkBox) {
        this.task = Task;
        this.context = context;
        this.dialog = dialog;
        this.position = position;
        this.checkBox = checkBox;

    }

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        if (task == 1) {                             //  Main Activity List
            new MainActivity.OpenMainList(position, context,dialog);
        }
        if (task == 2) {                            // UnHide Password
            new Setting.HiddenMe(context , dialog , checkBox);
        }
        if (task == 3) {                            // Un-Hide
            new Setting.PasswordEdit(context,dialog);
        }

    }

    @Override
    public void onAuthenticationFailed() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(500);
        }
        super.onAuthenticationFailed();
    }
}
