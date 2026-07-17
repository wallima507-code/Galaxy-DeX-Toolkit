package cv.wallima.a56toolkit;

import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cv.wallima.a56toolkit.databinding.ActivityInputDevicesBinding;

public class InputDevicesActivity extends AppCompatActivity {
    private ActivityInputDevicesBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityInputDevicesBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.btnRefresh.setOnClickListener(v -> refresh());
        refresh();
    }

    private void refresh() {
        List<String> lines = new ArrayList<>();
        for (int id : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(id);
            if (device == null) continue;
            lines.add("DISPOSITIVO\n" + device.getName()
                    + "\nID: " + id
                    + "\nFontes: " + sourceNames(device.getSources())
                    + "\nExterno: " + (device.isExternal() ? "Sim" : "Não")
                    + "\n");
        }
        b.tvDevices.setText(lines.isEmpty() ? "Nenhum dispositivo de entrada detetado." : String.join("\n", lines));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            b.tvLastEvent.setText("Última tecla: " + KeyEvent.keyCodeToString(event.getKeyCode()));
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        b.tvLastEvent.setText("Rato: botões=" + event.getButtonState()
                + "  roda=" + event.getAxisValue(MotionEvent.AXIS_VSCROLL));
        return super.dispatchGenericMotionEvent(event);
    }

    private String sourceNames(int sources) {
        List<String> names = new ArrayList<>();
        if ((sources & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE) names.add("Rato");
        if ((sources & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD) names.add("Teclado");
        if ((sources & InputDevice.SOURCE_TOUCHPAD) == InputDevice.SOURCE_TOUCHPAD) names.add("Touchpad");
        if ((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) names.add("Gamepad");
        return names.isEmpty() ? String.valueOf(sources) : String.join(", ", names);
    }
}
