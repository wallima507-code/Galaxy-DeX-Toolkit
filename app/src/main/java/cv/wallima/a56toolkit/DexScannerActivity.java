package cv.wallima.a56toolkit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.InputDevice;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cv.wallima.a56toolkit.databinding.ActivityDexScannerBinding;

public class DexScannerActivity extends AppCompatActivity {
    private ActivityDexScannerBinding b;

    private record CheckResult(String label, String detail, boolean found, boolean protectedComponent) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDexScannerBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.btnScan.setOnClickListener(v -> runScan());
        b.btnShare.setOnClickListener(v -> shareReport());
        b.btnWirelessDebug.setOnClickListener(v -> openWirelessDebugging());
        runScan();
    }

    private void runScan() {
        List<CheckResult> checks = new ArrayList<>();
        checks.add(checkActivity("Painel DeX", "com.android.settings", "com.android.settings.Settings$DexEntryScreenSettingsActivity"));
        checks.add(checkActivity("Definições Desktop", "com.android.settings", "com.android.settings.Settings$DesktopModeSettingsActivity"));
        checks.add(checkActivity("Definições DeX", "com.android.settings", "com.android.settings.Settings$DexModeActivity"));
        checks.add(checkActivity("Touchpad nativo", "com.android.systemui", "com.android.systemui.dextouchpad.activity.TouchpadActivity"));
        checks.add(checkActivity("Gestos DeX", "com.android.settings", "com.android.settings.Settings$DexModeTouchGestureSettingsActivity"));
        checks.add(checkActivity("Teclado DeX", "com.android.settings", "com.android.settings.Settings$DexModeKeyboardSettingsActivity"));
        checks.add(checkActivity("Rato DeX", "com.android.settings", "com.android.settings.Settings$DexModeMouseSettingsActivity"));
        checks.add(checkActivity("Wallpaper DeX", "com.android.settings", "com.android.settings.Settings$DexModeWallpaperSettingsActivity"));

        int found = 0;
        int protectedCount = 0;
        StringBuilder components = new StringBuilder();
        for (CheckResult check : checks) {
            if (check.found()) found++;
            if (check.protectedComponent()) protectedCount++;
            components.append(check.found() ? "✓ " : "✕ ")
                    .append(check.label())
                    .append(" — ")
                    .append(check.detail())
                    .append('\n');
        }

        DisplayInfo displayInfo = readDisplayInfo();
        InputInfo inputInfo = readInputInfo();
        boolean wirelessDebug = Settings.Global.getInt(getContentResolver(), "adb_wifi_enabled", 0) == 1;

        b.tvScore.setText(found + " de " + checks.size() + " componentes encontrados");
        b.tvScoreDetail.setText(protectedCount > 0
                ? protectedCount + " componente(s) protegido(s) pelo sistema"
                : "Nenhum componente protegido identificado");
        b.tvComponents.setText(components.toString().trim());
        b.tvDisplay.setText(displayInfo.text());
        b.tvInput.setText(inputInfo.text());
        b.tvAdb.setText(wirelessDebug ? "✓ Depuração sem fios ativa" : "○ Depuração sem fios desativada");
        b.tvTimestamp.setText("Última análise: " + DateFormat.getDateTimeInstance().format(new Date()));
    }

    private CheckResult checkActivity(String label, String packageName, String className) {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(
                    new ComponentName(packageName, className),
                    PackageManager.ComponentInfoFlags.of(PackageManager.MATCH_DISABLED_COMPONENTS)
            );
            boolean enabled = info.enabled;
            boolean exported = info.exported;
            String detail = enabled
                    ? (exported ? "disponível" : "encontrado • protegido")
                    : "encontrado • desativado";
            return new CheckResult(label, detail, true, !exported);
        } catch (Exception ignored) {
            return new CheckResult(label, "não encontrado", false, false);
        }
    }

    private DisplayInfo readDisplayInfo() {
        DisplayManager manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = manager == null ? new Display[0] : manager.getDisplays();
        StringBuilder text = new StringBuilder();
        int external = 0;
        for (Display display : displays) {
            if (display.getDisplayId() == Display.DEFAULT_DISPLAY) continue;
            external++;
            Display.Mode mode = display.getMode();
            text.append("✓ ").append(display.getName()).append('\n')
                    .append(mode.getPhysicalWidth()).append(" × ")
                    .append(mode.getPhysicalHeight()).append(" • ")
                    .append(String.format(Locale.getDefault(), "%.0f Hz", mode.getRefreshRate()));
        }
        if (external == 0) return new DisplayInfo("○ Nenhum monitor externo detetado");
        return new DisplayInfo(text.toString());
    }

    private InputInfo readInputInfo() {
        boolean mouse = false;
        boolean keyboard = false;
        boolean touchpad = false;
        for (int id : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(id);
            if (device == null || device.isVirtual()) continue;
            int sources = device.getSources();
            mouse |= (sources & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE;
            touchpad |= (sources & InputDevice.SOURCE_TOUCHPAD) == InputDevice.SOURCE_TOUCHPAD;
            keyboard |= device.getKeyboardType() != InputDevice.KEYBOARD_TYPE_NONE;
        }
        return new InputInfo((mouse ? "✓ Rato detetado\n" : "○ Rato não detetado\n")
                + (keyboard ? "✓ Teclado detetado\n" : "○ Teclado não detetado\n")
                + (touchpad ? "✓ Touchpad físico detetado" : "○ Touchpad físico não detetado"));
    }

    private void openWirelessDebugging() {
        String[] actions = {
                "android.settings.WIRELESS_DEBUGGING_SETTINGS",
                "android.settings.APPLICATION_DEVELOPMENT_SETTINGS"
        };
        for (String action : actions) {
            try {
                startActivity(new Intent(action));
                return;
            } catch (Exception ignored) { }
        }
        SystemTools.openIntent(this, Settings.ACTION_SETTINGS, "Definições");
    }

    private void shareReport() {
        String report = "Galaxy DeX Toolkit v3.0\n"
                + Build.MODEL + " • Android " + Build.VERSION.RELEASE + " • API " + Build.VERSION.SDK_INT + "\n\n"
                + b.tvScore.getText() + "\n" + b.tvScoreDetail.getText() + "\n\n"
                + "COMPONENTES\n" + b.tvComponents.getText() + "\n\n"
                + "MONITOR\n" + b.tvDisplay.getText() + "\n\n"
                + "ENTRADA\n" + b.tvInput.getText() + "\n\n"
                + "ADB\n" + b.tvAdb.getText();
        Intent share = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, "Relatório Galaxy DeX Toolkit")
                .putExtra(Intent.EXTRA_TEXT, report);
        startActivity(Intent.createChooser(share, "Partilhar relatório"));
    }

    private record DisplayInfo(String text) {}
    private record InputInfo(String text) {}
}
