package com.aghakhani.simplepermissionmanager;

/**
 * Callback interface for handling permission request results.
 * Implement this to receive granted, denied, or permanently denied permission events.
 */
public interface PermissionCallback {
    /**
     * Called when all requested permissions are granted.
     */
    void onAllGranted();

    /**
     * Called when some permissions are denied.
     * @param deniedPermissions List of denied permission names (e.g., Manifest.permission.CAMERA).
     */
    void onDenied(java.util.List<String> deniedPermissions);

    /**
     * Optional: Called when permissions are permanently denied (user selects "Don't ask again").
     * @param permanentlyDenied List of permanently denied permissions.
     */
    default void onPermanentlyDenied(java.util.List<String> permanentlyDenied) {
        // Default empty implementation
    }
}