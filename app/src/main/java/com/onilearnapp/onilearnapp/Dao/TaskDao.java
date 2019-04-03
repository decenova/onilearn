package com.onilearnapp.onilearnapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Model.TaskAndSubject;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    public LiveData<List<Task>> loadTask();

    @Query("SELECT task.*, subject.* FROM task, subject WHERE subjectId = subject.id")
    public LiveData<List<TaskAndSubject>> loadTaskAndSubject();

    @Query("SELECT task.*, subject.* FROM task, subject WHERE subjectId = subject.id AND startTime BETWEEN :startDate AND :endDate ORDER BY startTime")
    public LiveData<List<TaskAndSubject>> loadTaskAndSubject(Date startDate, Date endDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTasks(Task... tasks);

    @Update
    public void updateTasks(Task... tasks);
}
