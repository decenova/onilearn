package com.onilearnapp.onilearnapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.onilearnapp.onilearnapp.Activity.AlarmActivity;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Service.RingToneService;

import java.net.MulticastSocket;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        Task task = new Task();
        String actionStr = "off";
        if (bundle != null) {
            task = (Task)bundle.getSerializable("task");
            actionStr = bundle.getString("action");
        }

        Intent musicIntent = new Intent(context, RingToneService.class);
        musicIntent.putExtra("action", actionStr);
        context.startService(musicIntent);
        Log.d("alarm", "ringtone");
        if (actionStr.equals("on")) {
            Log.d("alarm", "alrm");
            Intent alarmIntent = new Intent(context, AlarmActivity.class);
            alarmIntent.putExtra("task",task);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmIntent);
        }

    }
}
