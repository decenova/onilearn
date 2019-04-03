package com.onilearnapp.onilearnapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Model.TaskAndSubject;
import com.onilearnapp.onilearnapp.Repository.TaskRepository;

import java.util.Date;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTask;
    private LiveData<List<TaskAndSubject>> allTasksAndSubject;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTask = taskRepository.getAllTasks();
        allTasksAndSubject = taskRepository.getAllTasksAndSubject();
    }

    public LiveData<List<Task>> getAllTask() {
        return allTask;
    }

    public LiveData<List<TaskAndSubject>> getAllTasksAndSubject() {
        return allTasksAndSubject;
    }
    public LiveData<List<TaskAndSubject>> getAllTasksAndSubject(Date startDate, Date endDate) {
        return taskRepository.getAllTasksAndSubject(startDate,endDate);
    }

    public void insert(Task task) {
        Task[] tasks = {task};
        taskRepository.insertTask(tasks);
    }
}
