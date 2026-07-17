package cv.wallima.a56toolkit;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import cv.wallima.a56toolkit.databinding.ActivityWallpaperBinding;

public class WallpaperActivity extends AppCompatActivity {

    private ActivityWallpaperBinding b;
    private Uri selectedImage;

    private final ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri == null) return;

                        selectedImage = uri;

                        try {
                            getContentResolver().takePersistableUriPermission(
                                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception ignored) { }

                        b.ivPreview.setImageURI(uri);
                        b.tvStatus.setText("Imagem selecionada. Toque em Aplicar imagem.");
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityWallpaperBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.btnChoose.setOnClickListener(
                v -> imagePicker.launch(new String[]{"image/*"}));

        b.btnApply.setOnClickListener(v -> applyWallpaper());

        b.btnSamsungSettings.setOnClickListener(v -> SystemTools.openIntent(
                this,
                "android.settings.WALLPAPER_SETTINGS",
                "Definições de papel de parede"));

        b.btnDexWallpaper.setOnClickListener(v -> SystemTools.openFirst(
                this,
                "Papel de parede DeX",
                "com.android.settings",
                "com.android.settings.Settings$DexModeWallpaperSettingsActivity",
                "com.android.settings.Settings$DesktopModeWallpaperSettingsActivity",
                "com.android.settings.Settings$WallpaperSettingsActivity"));

        b.btnSetWallpaper.setOnClickListener(v -> SystemTools.openIntent(
                this,
                Intent.ACTION_SET_WALLPAPER,
                "Seletor de papel de parede"));
    }

    private void applyWallpaper() {
        if (selectedImage == null) {
            Toast.makeText(this, "Escolhe primeiro uma imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        WallpaperManager manager = WallpaperManager.getInstance(this);

        if (!manager.isWallpaperSupported()) {
            b.tvStatus.setText("Este modo do sistema não permite alterar o papel de parede.");
            Toast.makeText(this, "Papel de parede não suportado", Toast.LENGTH_LONG).show();
            return;
        }

        try (InputStream stream = getContentResolver().openInputStream(selectedImage)) {
            Bitmap bitmap = BitmapFactory.decodeStream(stream);

            if (bitmap == null) {
                b.tvStatus.setText("Não foi possível ler a imagem selecionada.");
                return;
            }

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            b.tvStatus.setText("Imagem aplicada ao sistema. Fecha e reabre o modo desktop para testar.");
            Toast.makeText(this, "Papel de parede aplicado", Toast.LENGTH_LONG).show();

        } catch (SecurityException e) {
            b.tvStatus.setText("Permissão recusada pelo sistema: " + e.getMessage());
            Toast.makeText(this, "O firmware recusou a alteração", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            b.tvStatus.setText("Não foi possível aplicar: " + e.getClass().getSimpleName());
            Toast.makeText(this, "Erro ao aplicar a imagem", Toast.LENGTH_LONG).show();
        }
    }
}
