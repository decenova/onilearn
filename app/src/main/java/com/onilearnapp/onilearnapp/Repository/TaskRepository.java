package com.onilearnapp.onilearnapp.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.onilearnapp.onilearnapp.AppDatabase.AppDatabase;
import com.onilearnapp.onilearnapp.Dao.TaskDao;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Model.TaskAndSubject;

import java.util.Date;
import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<TaskAndSubject>> allTasksAndSubject;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.taskDao = db.taskDao();
        this.allTasks = taskDao.loadTask();
        this.allTasksAndSubject = taskDao.loadTaskAndSubject();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<TaskAndSubject>> getAllTasksAndSubject() {
        return allTasksAndSubject;
    }
    public LiveData<List<TaskAndSubject>> getAllTasksAndSubject(Date startDate, Date endDate) {
        return taskDao.loadTaskAndSubject(startDate,endDate);
    }

    public void insertTask(Task[] tasks){
        new insertAsyncTask(taskDao).execute(tasks);
    }

    private static class insertAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao asyncTaskDao;

        public insertAsyncTask(TaskDao taskDao) {
            this.asyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            asyncTaskDao.insertTasks(tasks);
            return null;
        }
    }
}
