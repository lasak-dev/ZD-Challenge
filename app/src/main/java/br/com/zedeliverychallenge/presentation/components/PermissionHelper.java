package br.com.zedeliverychallenge.presentation.components;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import br.com.zedeliverychallenge.R;

public class PermissionHelper {

    private AppCompatActivity activity;
    private PermissionGrantedCallback callback;
    private String denyText;
    private String blockText;


    public PermissionHelper(AppCompatActivity activity, PermissionGrantedCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }


    public void checkPermission(String permission, String denyText, String blockText) {
        this.denyText = denyText;
        this.blockText = blockText;

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermission(permission)) {
                callback.onPermissionGranted();
            }
        } else {
            callback.onPermissionGranted();
        }
    }

    private boolean checkAndRequestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
            return false;
        }

        return true;
    }

    public void onRequestPermissionsResult(String permissions[], int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermissionGranted();
            } else {
                String permission = permissions[0];
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    showRetryDialog(denyText, (dialog, which) -> checkPermission(permission, denyText, blockText));
                } else {
                    showConfirmDialog(blockText);
                }
            }
        } else {
            showConfirmDialog(blockText);
        }
    }


    private void showRetryDialog(String text, DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(activity)
                .setMessage(text)
                .setPositiveButton(R.string.general_accept, positiveListener)
                .setNegativeButton(R.string.general_cancel, null)
                .create()
                .show();
    }

    private void showConfirmDialog(String text) {
        new AlertDialog.Builder(activity)
                .setMessage(text)
                .setPositiveButton(R.string.general_accept, null)
                .create()
                .show();
    }

    public interface PermissionGrantedCallback {
        void onPermissionGranted();
    }
}
