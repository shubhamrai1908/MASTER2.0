package shubham.latest.master20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.Manifest;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    String[] wantedPerm = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.READ_PHONE_NUMBERS};
    private static final int RC_PERM_SMS = 125;
    private static final int RC_SETTINGS = 126;
    private Activity activity = MainActivity.this;
    private String TAG = "spermi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isSimExists()) {
            if (EasyPermissions.hasPermissions(activity, wantedPerm)) {
                Log.d(TAG, "Already have permission, do the thing");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            } else {
                EasyPermissions.requestPermissions(activity, "This app needs access to send SMS.", RC_PERM_SMS, wantedPerm);
            }
        } else {
            Toast.makeText(getApplicationContext(),"Your device doesn't have Sim card,\nPlease insert Sim card to run this app",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, activity);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
            new AppSettingsDialog.Builder(activity)
                    .setTitle("Permissions Required")
                    .setPositiveButton("Settings")
                    .setNegativeButton("Cancel")
                    .setRequestCode(RC_SETTINGS)
                    .build()
                    .show();
        }
    }
    @AfterPermissionGranted(RC_PERM_SMS)
    private void methodRequireLocationPermission() {
        if (EasyPermissions.hasPermissions(this, wantedPerm)) {
            Log.d(TAG, "Already have permission, do the thing");
        } else {
            Log.d(TAG, "Do not have permission, request them now");
            EasyPermissions.requestPermissions(activity, "This app needs access to send SMS.", RC_PERM_SMS, wantedPerm);
        }
    }
    public boolean isSimExists() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int SIM_STATE = telephonyManager.getSimState();

        if (SIM_STATE == TelephonyManager.SIM_STATE_READY)
            return true;
        else {
            // we can inform user about sim state
            switch (SIM_STATE) {
                case TelephonyManager.SIM_STATE_ABSENT:
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    break;
            }
            return false;
        }
    }


}