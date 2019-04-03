package com.onilearnapp.onilearnapp.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Fragment.DatePickerFragment;
import com.onilearnapp.onilearnapp.Fragment.TimePickerFragment;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    final int REQUEST_CODE_SUBJECT = 123;
    private TextView txtSubjectName,txtDate,txtTime;
    NetworkImageView imgSubjectIcon;
    private CheckBox checkBoxIsAlarm;
    private Subject subject;
    private Calendar taskTime;
    private String webAppUrl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_task);

        Toolbar myChildToolBar = (Toolbar) findViewById(R.id.my_child_toolbar);
        setSupportActionBar(myChildToolBar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        webAppUrl = getResources().getString(R.string.app_web);
        requestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            private final LruCache<String, Bitmap> imageCache = new LruCache<>(cacheSize);

            @Override
            public Bitmap getBitmap(String s) {
                return imageCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                imageCache.put(s, bitmap);
            }
        });

        reflect();
        init();

    }

    private void reflect() {
        txtSubjectName = (TextView) findViewById(R.id.txtSubjectName);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        checkBoxIsAlarm = (CheckBox) findViewById(R.id.checkBoxIsAlarm);
        imgSubjectIcon = (NetworkImageView) findViewById(R.id.imgSubjectIcon);
        imgSubjectIcon.setDefaultImageResId(R.drawable.default_icon);
    }

    private void init(){
        taskTime = Calendar.getInstance();
        taskTime.set(Calendar.SECOND,0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        txtTime.setText(simpleDateFormat.format(taskTime.getTime()));
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText(simpleDateFormat.format(taskTime.getTime()));
    }

    public void clickToChangeDate(View view) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateFragment = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                taskTime.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtDate.setText(simpleDateFormat.format(taskTime.getTime()));
            }
        }, year, month, day);
        dateFragment.show();
    }

    public void clickToChangeTime(View view) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                taskTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                taskTime.set(Calendar.MINUTE, minute);
                txtTime.setText(simpleDateFormat.format(taskTime.getTime()));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_task_action,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save){
            saveTask();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        txtDate.setText(dayOfMonth + "/" + month + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView txtTime = (TextView) findViewById(R.id.txtTime);
        String strHour = (hourOfDay > 9)? (hourOfDay + "") : ("0" + hourOfDay);
        String strMinute = (minute > 9)? (minute + "") : ("0" + minute);
        txtTime.setText(strHour + ":" + strMinute);
    }


    private void saveTask() {
        if (subject == null){
            Toast.makeText(this, "Choose a subject to continue!", Toast.LENGTH_SHORT).show();
            return;
        }

        Date startTime = taskTime.getTime();
        taskTime.add(Calendar.HOUR_OF_DAY,1);
        Date endTime = taskTime.getTime();
        Task task = new Task(subject,startTime,endTime,checkBoxIsAlarm.isChecked(),"Multiple choice");
        taskTime.add(Calendar.HOUR_OF_DAY,-1);

        Log.d("Setting Task:", "Is check: " + checkBoxIsAlarm.isChecked());
        if (checkBoxIsAlarm.isChecked()){
            Log.d("Setting Task:", "Is check: " + checkBoxIsAlarm.isChecked());
            Intent intent = new Intent(this, AlarmReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("task",task);
            bundle.putString("action", "on");
            intent.putExtra("bundle",bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, taskTime.getTimeInMillis(), pendingIntent);
        }

        Intent saveTaskIntent = new Intent();
        saveTaskIntent.putExtra("task",task);
        setResult(RESULT_OK,saveTaskIntent);
        finish();
    }

    public void clickToChangeSubject(View view) {
        Intent intent = new Intent(this, SubjectChoiceActivity.class);
        intent.putExtra("type", "make a task");
        startActivityForResult(intent, REQUEST_CODE_SUBJECT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SUBJECT && resultCode == RESULT_OK && data != null) {
            subject = (Subject) data.getSerializableExtra("SubjectDao");
            txtSubjectName.setText(subject.getName());

            imgSubjectIcon.setImageUrl(webAppUrl + subject.getIconUrl(), imageLoader);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
