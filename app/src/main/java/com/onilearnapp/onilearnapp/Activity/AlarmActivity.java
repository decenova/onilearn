package com.onilearnapp.onilearnapp.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private String webAppUrl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Task task;
    TextView txtTime, txtSubjectName, txtTaskStartTime, txtTaskEndTime, txtCourse, txtTestType;
    ImageView imgBellIcon;
    NetworkImageView imgSubjectIcon;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        init();
        initImageLoader();
        initTask();
    }

//    public void clickToStopAlarm(View view) {
//        stopAlarm(view);
//        finish();
//    }

    public void skipTask(View view) {
        stopAlarm(view);
        finish();
    }

    public void doTask(View view) {
        stopAlarm(view);
        if(task != null){
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra("subject", task.getSubject());
            this.startActivity(intent);
        }
        finish();
    }

    public void stopAlarm(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("action", "off");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        sendBroadcast(intent);
    }

    private void init() {
        final Calendar cal = Calendar.getInstance();
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        txtTime = (TextView) findViewById(R.id.txtTime);
        String strHour = (hourOfDay > 9) ? (hourOfDay + "") : ("0" + hourOfDay);
        String strMinute = (minute > 9) ? (minute + "") : ("0" + minute);
        txtTime.setText(strHour + ":" + strMinute);

        seekBar = (SeekBar) findViewById(R.id.seekBarSwipe);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress < 80 && progress > 20) {
                    seekBar.setProgress(50);
                } else if (progress <= 20) {
                    seekBar.setProgress(5);
                    skipTask(seekBar.getRootView());
                } else if (progress >= 80) {
                    seekBar.setProgress(95);
                    doTask(seekBar.getRootView());
                }
            }
        });
    }
    private void initImageLoader(){
        webAppUrl = getResources().getString(R.string.app_web);
        requestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            final int maxMemory =(int)(Runtime.getRuntime().maxMemory()/1024);
            final int cacheSize = maxMemory /8;
            private final LruCache<String,Bitmap> imageCache = new LruCache<>(cacheSize);
            @Override
            public Bitmap getBitmap(String s) {
                return imageCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                imageCache.put(s,bitmap);
            }
        });
    }
    private void initTask() {
//        txtTaskStartTime = (TextView) findViewById(R.id.txtTaskStartTime);
//        txtTaskEndTime = (TextView) findViewById(R.id.txtTaskEndTime);
        txtSubjectName = (TextView) findViewById(R.id.txtSubjectName);
        imgSubjectIcon = (NetworkImageView) findViewById(R.id.imgSubjectIcon);
        txtCourse = (TextView) findViewById(R.id.txtCourse);
        txtTestType = (TextView) findViewById(R.id.txtTestType);
        imgBellIcon = (ImageView) findViewById(R.id.imgBellIcon);
        imgSubjectIcon.setDefaultImageResId(R.drawable.default_icon);
        task = (Task) getIntent().getSerializableExtra("task");
        Subject subject = task.getSubject();
        txtSubjectName.setText(subject.getName());
        imgSubjectIcon.setImageUrl(webAppUrl + subject.getIconUrl(), imageLoader);
        txtCourse.setText("Basic");
        txtTestType.setText("Multiple choice");
        if (task.isAlarm())
            imgBellIcon.setImageResource(R.drawable.notification);
        else
            imgBellIcon.setImageResource(R.drawable.notifications_disable);
    }
}
