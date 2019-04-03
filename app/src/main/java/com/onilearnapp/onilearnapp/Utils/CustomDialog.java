package com.onilearnapp.onilearnapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.R;

public class CustomDialog extends Dialog{

    public Activity activity;
    public String txtString;
    public int imageID;

    public CustomDialog(Activity c, String txtString, int imageID) {
        super(c);
        this.activity = c;
        this.txtString = txtString;
        this.imageID = imageID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);

        TextView textView = (TextView) findViewById(R.id.toast_text);
        textView.setText(txtString);
        ImageView imageView = (ImageView) findViewById(R.id.toast_image);
        imageView.setImageResource(imageID);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Log.d("action", "outside clicked");
        this.dismiss();
        return super.onTouchEvent(event);
    }
}
