package cv.wallima.a56toolkit;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.InputDevice;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Locale;

import cv.wallima.a56toolkit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        String model = Build.MODEL == null ? "Samsung Galaxy" : Build.MODEL;
        boolean a56 = model.toUpperCase(Locale.ROOT).startsWith("SM-A566");

        b.tvDevice.setText(a56 ? "Galaxy A56" : model);
        b.tvSystem.setText("Android " + Build.VERSION.RELEASE + "  •  One UI " + oneUi() + "  •  API " + Build.VERSION.SDK_INT);
        b.tvCompatibility.setText(a56 ? "● DeX oculto disponível para investigação" : "● Modo de compatibilidade Samsung");

        b.btnDesktop.setOnClickListener(v -> open(DesktopToolsActivity.class));
        b.btnTouchpad.setOnClickListener(v -> open(DexTouchpadActivity.class));
        b.btnInput.setOnClickListener(v -> open(InputDevicesActivity.class));
        b.btnWallpaper.setOnClickListener(v -> open(WallpaperActivity.class));
        b.btnDiagnostics.setOnClickListener(v -> open(DeviceInfoActivity.class));
        b.btnScanner.setOnClickListener(v -> open(DexScannerActivity.class));
        b.btnAdvanced.setOnClickListener(v -> open(ActivityExplorerActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLiveStatus();
    }

    private void updateLiveStatus() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager == null ? new Display[0] : displayManager.getDisplays();
        Display external = null;
        for (Display display : displays) {
            if (display.getDisplayId() != Display.DEFAULT_DISPLAY) {
                external = display;
                break;
            }
        }
        if (external != null) {
            Display.Mode mode = external.getMode();
            b.tvDisplayStatus.setText(mode.getPhysicalWidth() + "×" + mode.getPhysicalHeight()
                    + " • " + Math.round(mode.getRefreshRate()) + " Hz");
        } else {
            b.tvDisplayStatus.setText("Monitor: não detetado");
        }

        boolean mouse = false;
        boolean keyboard = false;
        for (int id : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(id);
            if (device == null) continue;
            int sources = device.getSources();
            mouse |= (sources & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE;
            keyboard |= device.getKeyboardType() != InputDevice.KEYBOARD_TYPE_NONE;
        }

        String inputSummary;
        if (mouse && keyboard) inputSummary = "Rato + teclado";
        else if (mouse) inputSummary = "Rato ligado";
        else if (keyboard) inputSummary = "Teclado ligado";
        else inputSummary = "Sem periféricos";

        b.tvInputStatus.setText(inputSummary);
        b.tvInputCardState.setText(inputSummary.toUpperCase(Locale.ROOT));
    }

    private void open(Class<?> target) {
        startActivity(new Intent(this, target));
    }

    private String oneUi() {
        try {
            Field field = Build.VERSION.class.getField("SEM_PLATFORM_INT");
            int sem = field.getInt(null);
            int major = (sem / 10000) - 9;
            int minor = (sem % 10000) >= 500 ? 5 : 0;
            if (major >= 1 && major <= 20) return major + "." + minor;
            return "SEM " + sem;
        } catch (Exception ignored) {
            return "não detetada";
        }
    }
}
