package com.example.ghostswitch.custom_components;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.ghostswitch.R;

public class CustomCursorEditText extends AppCompatEditText {

    public CustomCursorEditText(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomCursorEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCursorEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setTextCursorDrawable(ContextCompat.getDrawable(context, R.drawable.cursor));
        }
    }
}
