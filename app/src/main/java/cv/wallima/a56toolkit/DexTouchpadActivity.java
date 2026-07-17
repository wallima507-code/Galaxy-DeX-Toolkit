package cv.wallima.a56toolkit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DexTouchpadActivity extends BaseListActivity {
    private static final String PACKAGE = "com.android.systemui";
    private static final String ACTIVITY = "com.android.systemui.dextouchpad.activity.TouchpadActivity";
    private static final String ADB_COMMAND = "adb shell am start -n " + PACKAGE + "/" + ACTIVITY;

    @Override
    protected void build() {
        header("DeX Touchpad 2.2", "Abre o touchpad nativo da Samsung. A Activity existe no System UI, mas pode estar protegida como exported=false.");

        action("Launch Touchpad", this::launchNormally);
        action("Copiar comando ADB", this::copyAdbCommand);
        action("Abrir Depuração sem fios", () -> SystemTools.openFirst(this, "Depuração sem fios", "com.android.settings",
                "com.android.settings.Settings$AdbWirelessSettingsActivity",
                "com.android.settings.Settings$DevelopmentSettingsDashboardActivity"));
        action("Abrir Opções de programador", () -> SystemTools.openIntent(
                this,
                "android.settings.APPLICATION_DEVELOPMENT_SETTINGS",
                "Opções de programador"
        ));
        action("Informação técnica", () -> Toast.makeText(
                this,
                PACKAGE + "\n" + ACTIVITY + "\n\nSe o Launch normal for bloqueado, usa App Manager com Wireless Debugging e toca em Launch.",
                Toast.LENGTH_LONG
        ).show());
    }

    private void launchNormally() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(PACKAGE, ACTIVITY));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(this,
                    "Touchpad encontrado, mas protegido pela Samsung. Usa Wireless Debugging/App Manager.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this,
                    "Touchpad indisponível neste firmware: " + e.getClass().getSimpleName(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void copyAdbCommand() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Comando DeX Touchpad", ADB_COMMAND));
        Toast.makeText(this, "Comando ADB copiado", Toast.LENGTH_SHORT).show();
    }
}
