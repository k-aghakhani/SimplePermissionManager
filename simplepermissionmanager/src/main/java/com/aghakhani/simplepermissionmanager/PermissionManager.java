package com.aghakhani.simplepermissionmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * A lightweight library for managing Android runtime permissions with a custom Material Design UI.
 * Simplifies permission requests with callbacks and automatic retry dialogs.
 * Usage:
 * PermissionManager manager = new PermissionManager(activity, callback);
 * manager.requestPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
 */
public class PermissionManager {
    private static final String TAG = "PermissionManager";
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private final Activity activity;
    private final PermissionCallback callback;
    private final String[] permissionExplanations;

    /**
     * Constructor for PermissionManager.
     * @param activity The activity requesting permissions.
     * @param callback The callback to handle permission results.
     * @param permissionExplanations Optional explanations for permissions (in same order as requested).
     */
    public PermissionManager(Activity activity, PermissionCallback callback, String... permissionExplanations) {
        this.activity = activity;
        this.callback = callback;
        this.permissionExplanations = permissionExplanations;
    }

    /**
     * Requests the specified permissions, checking which ones are already granted.
     * @param permissions Array of permission strings (e.g., Manifest.permission.CAMERA).
     */
    public void requestPermissions(String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }

        if (deniedPermissions.isEmpty()) {
            Log.d(TAG, "All permissions already granted.");
            callback.onAllGranted();
            return;
        }

        ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        Log.d(TAG, "Requesting permissions: " + deniedPermissions);
    }

    /**
     * Handles the result of permission requests. Call this from Activity's onRequestPermissionsResult.
     * @param requestCode The request code passed to requestPermissions.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the permissions.
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }

        List<String> denied = new ArrayList<>();
        List<String> permanentlyDenied = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                denied.add(permission);
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permanentlyDenied.add(permission);
                }
            }
        }

        if (denied.isEmpty()) {
            Log.d(TAG, "All permissions granted.");
            callback.onAllGranted();
        } else {
            Log.w(TAG, "Some permissions denied: " + denied);
            callback.onDenied(denied);
            if (!denied.isEmpty()) {
                showCustomPermissionDialog(denied);
            }
            if (!permanentlyDenied.isEmpty()) {
                Log.e(TAG, "Permanently denied permissions: " + permanentlyDenied);
                callback.onPermanentlyDenied(permanentlyDenied);
            }
        }
    }

    /**
     * Displays a custom Material Design dialog for denied permissions with retry option.
     * @param deniedPermissions List of denied permissions.
     */
    private void showCustomPermissionDialog(List<String> deniedPermissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_permission, null);
        builder.setView(dialogView);

        TextView titleText = dialogView.findViewById(R.id.tv_title);
        TextView messageText = dialogView.findViewById(R.id.tv_message);
        Button btnRetry = dialogView.findViewById(R.id.btn_retry);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        titleText.setText("Permission Required");
        StringBuilder message = new StringBuilder("These permissions are needed for the app to work properly:\n");
        for (String perm : deniedPermissions) {
            String explanation = getExplanationForPermission(perm);
            message.append("• ").append(explanation).append("\n");
        }
        messageText.setText(message.toString());

        final AlertDialog dialog = builder.create();

        btnRetry.setOnClickListener(v -> {
            dialog.dismiss();
            requestPermissions(deniedPermissions.toArray(new String[0]));
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);
        dialog.show();
        Log.d(TAG, "Custom dialog shown for denied permissions.");
    }

    /**
     *
     * Retrieves an explanation for a given permission.
     * Uses provided explanations or defaults if none provided.
     * @param permission The permission string.
     * @return The explanation for the permission.
     */
    private String getExplanationForPermission(String permission) {
        if (permissionExplanations != null && permissionExplanations.length > 0) {
            for (String explanation : permissionExplanations) {
                if (explanation != null) {
                    return explanation;
                }
            }
        }
        if ("android.permission.CAMERA".equals(permission)) {
            return "To take photos or scan QR codes.";
        } else if ("android.permission.ACCESS_FINE_LOCATION".equals(permission)) {
            return "To show your location on a map.";
        } else if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(permission)) {
            return "To save files to your device.";
        } else {
            return "This feature requires access to " + permission + ".";
        }
    }
}