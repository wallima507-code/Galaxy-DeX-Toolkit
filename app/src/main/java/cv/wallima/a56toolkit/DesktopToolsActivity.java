package cv.wallima.a56toolkit;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import cv.wallima.a56toolkit.databinding.ActivityDesktopToolsBinding;

public class DesktopToolsActivity extends AppCompatActivity {
    private static final String SETTINGS = "com.android.settings";
    private ActivityDesktopToolsBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDesktopToolsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.cardLaunchDex.setOnClickListener(v -> SystemTools.openFirst(this, "Painel DeX", SETTINGS,
                "com.android.settings.Settings$DexEntryScreenSettingsActivity",
                "com.android.settings.Settings$DesktopModeSettingsActivity"));

        b.cardDexSettings.setOnClickListener(v -> SystemTools.openFirst(this, "Modo DeX", SETTINGS,
                "com.android.settings.Settings$DexModeActivity",
                "com.android.settings.Settings$DesktopModeSettingsActivity"));

        b.cardSmartView.setOnClickListener(v -> SystemTools.openIntent(this,
                "android.settings.CAST_SETTINGS", "Smart View"));

        b.cardTouchpad.setOnClickListener(v -> startActivity(new Intent(this, DexTouchpadActivity.class)));

        b.cardInput.setOnClickListener(v -> startActivity(new Intent(this, InputDevicesActivity.class)));

        b.cardGestures.setOnClickListener(v -> SystemTools.openFirst(this, "Gestos DeX", SETTINGS,
                "com.android.settings.Settings$DexModeTouchGestureSettingsActivity"));

        b.cardWallpaper.setOnClickListener(v -> startActivity(new Intent(this, WallpaperActivity.class)));

        b.cardKeyboard.setOnClickListener(v -> SystemTools.openFirst(this, "Teclado DeX", SETTINGS,
                "com.android.settings.Settings$DexModeKeyboardSettingsActivity"));

        b.cardMouse.setOnClickListener(v -> SystemTools.openFirst(this, "Rato DeX", SETTINGS,
                "com.android.settings.Settings$DexModeMouseSettingsActivity"));

        b.cardBluetooth.setOnClickListener(v -> SystemTools.openIntent(this,
                Settings.ACTION_BLUETOOTH_SETTINGS, "Bluetooth"));

        b.cardPhysicalKeyboard.setOnClickListener(v -> SystemTools.openIntent(this,
                Settings.ACTION_HARD_KEYBOARD_SETTINGS, "Teclado físico"));

        b.cardDiagnostics.setOnClickListener(v -> startActivity(new Intent(this, DeviceInfoActivity.class)));
    }
}
