package cv.wallima.a56toolkit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public final class SystemTools {
    private SystemTools() {}
    public static boolean openComponent(Activity a, String pkg, String cls) {
        try { a.startActivity(new Intent().setComponent(new ComponentName(pkg, cls))); return true; }
        catch (Exception e) { return false; }
    }
    public static void openIntent(Activity a, String action, String label) {
        try { a.startActivity(new Intent(action)); }
        catch (Exception e) { Toast.makeText(a, label + " indisponível neste firmware", Toast.LENGTH_LONG).show(); }
    }
    public static void openFirst(Activity a, String label, String pkg, String... classes) {
        for (String cls : classes) if (openComponent(a, pkg, cls)) return;
        Toast.makeText(a, label + " bloqueado ou removido pela Samsung", Toast.LENGTH_LONG).show();
    }
    public static Intent settings(String action) { return new Intent(action); }
}
