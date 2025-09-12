# SimplePermissionManager 🚀

A lightweight, developer-friendly **Java library** for managing Android runtime permissions with a sleek **Material Design UI**.  
Say goodbye to boilerplate code and hello to user-friendly permission dialogs! 🎉

---

## 🌟 Why SimplePermissionManager?

Tired of complex permission handling in Android?  
**SimplePermissionManager** makes it effortless with:

- ✅ Clean & simple API  
- 🎨 Customizable dialogs  
- ⚡ Minimal dependencies  
- 📱 Great user experience  

Perfect for both **small apps** and **large-scale projects**.

---

## ✨ Features

- 🎯 **Simple API** – Request multiple permissions with a single callback.  
- 🖼️ **Material Design Dialog** – Beautiful retry dialog for denied permissions.  
- ⚡ **Lightweight** – Only depends on `androidx.core` and `androidx.appcompat`.  
- 📝 **Customizable Explanations** – Use defaults or your own messages.  
- 🚫 **Permanent Denial Handling** – Detects *Don't ask again* selections.  
- 📜 **Debug Logging** – Built-in logs (`tag: PermissionManager`).  

---

## 🔧 Prerequisites

- **Android API Level:** 21+ (Lollipop)  
- **Dependencies:**  
  - `androidx.core:core:1.12.0`  
  - `androidx.appcompat:appcompat:1.6.1`  
- **Java:** 8 or higher  
- **Android Studio:** Hedgehog or later  

---

## 📦 Installation

Add **SimplePermissionManager** to your project using [JitPack](https://jitpack.io):

### Step 1: Add JitPack to your root `build.gradle`
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add the dependency to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.aghakhani:SimplePermissionManager:v1.0'
}
```

---

## 🚀 Usage

### 1. Initialize PermissionManager
```java
import com.aghakhani.simplepermissionmanager.PermissionCallback;
import com.aghakhani.simplepermissionmanager.PermissionManager;

public class MainActivity extends AppCompatActivity {
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Optional: Custom explanations for permissions
        String[] explanations = {
            "Camera access is needed to capture photos.",
            "Storage access is required to save files."
        };

        permissionManager = new PermissionManager(this, new PermissionCallback() {
            @Override
            public void onAllGranted() {
                Toast.makeText(MainActivity.this, "All permissions granted! 🎉", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Denied: " + deniedPermissions, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermanentlyDenied(List<String> permanentlyDenied) {
                Toast.makeText(MainActivity.this, "Permanently denied: " + permanentlyDenied + ". Please enable in settings.", Toast.LENGTH_LONG).show();
            }
        }, explanations);

        // Request permissions on button click
        findViewById(R.id.btn_request).setOnClickListener(v -> {
            permissionManager.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
```

---

### 2. Add Permissions to `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

---

### 3. Layout for Demo
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <Button
        android:id="@+id/btn_request"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Request Permissions"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

### 4. Run and Test

1. Run your app on a device/emulator (**API 23+ required**).  
2. Tap the button → Request permissions.  
3. If denied → A sleek dialog explains why & offers retry.  

---

## 📚 Additional Documentation

- **Custom Dialog** – Material Design UI with retry option.  
- **Default Explanations** – Auto-generated if none provided.  
- **Logging** – Check Logcat (`PermissionManager`).  
- **Demo App** – Explore demo module for real usage.  

---

## 🤝 Contributing

We ❤️ contributions!

1. Fork the repo  
2. Create a feature branch → `git checkout -b feature/awesome-feature`  
3. Commit → `git commit -m "Add awesome feature"`  
4. Push → `git push origin feature/awesome-feature`  
5. Open a Pull Request 🎉  

Please follow code style & include tests where possible.

---

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.

---

## 📬 Contact

- 📧 Email: **kiarash1988@gmail.com**  
- 🐛 GitHub Issues: Open an issue  

⭐ Star the repo if you find it useful!  
Happy coding! 🚀

