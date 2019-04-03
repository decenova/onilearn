package com.onilearnapp.onilearnapp.Fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.Adapter.TaskAdapter;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Model.TaskAndSubject;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.ViewModel.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment {
    private final int REQUEST_CODE_TASK = 001;
    private TaskViewModel taskViewModel;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private Calendar calendar;
    TextView txtTaskNum;
    RecyclerView lvTask;


    public TimeTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_time_table, container, false);
        lvTask = (RecyclerView) view.findViewById(R.id.lsTask);
        txtTaskNum = (TextView) view.findViewById(R.id.txtTaskNum);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date endDate = calendar.getTime();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        taskList = new ArrayList<>();
        lvTask.setLayoutManager(linearLayoutManager);
        taskAdapter = new TaskAdapter(getContext());
        this.taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasksAndSubject(startDate,endDate).observe(this, new Observer<List<TaskAndSubject>>() {
            @Override
            public void onChanged(@Nullable List<TaskAndSubject> tasks) {
                taskAdapter.setTaskList(tasks);
                setTaskNum(tasks.size());
            }
        });
        lvTask.setAdapter(taskAdapter);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TASK && resultCode == RESULT_OK && data != null) {
            Task task =(Task) data.getSerializableExtra("task");
//            taskList.add(task);
//            taskAdapter.notifyDataSetChanged();
            taskViewModel.insert(task);
            Log.d("task", task.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTaskNum(int taskNum){
//        int taskNum = taskList.size();
        if (taskNum < 2)
            txtTaskNum.setText("There is " + taskNum + " task to do");
        else
            txtTaskNum.setText("There are " + taskNum + " tasks to do");
    }
}
