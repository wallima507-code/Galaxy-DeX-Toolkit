package cv.wallima.a56toolkit;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import cv.wallima.a56toolkit.databinding.ActivitySimpleListBinding;

public abstract class BaseListActivity extends AppCompatActivity {
    protected ActivitySimpleListBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySimpleListBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        build();
    }

    protected abstract void build();

    protected void header(String title, String description) {
        b.tvTitle.setText(title);
        b.tvDescription.setText(description);
    }

    protected void action(String text, Runnable runnable) {
        MaterialButton button = new MaterialButton(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextSize(16);
        button.setCornerRadius(dp(28));
        button.setMinHeight(dp(56));
        button.setPadding(dp(18), dp(12), dp(18), dp(12));
        button.setOnClickListener(view -> runnable.run());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = dp(14);
        button.setLayoutParams(params);
        b.listContainer.addView(button);
    }

    protected int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
