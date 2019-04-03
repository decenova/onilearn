package com.onilearnapp.onilearnapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.onilearnapp.onilearnapp.Model.Subject;

import java.util.List;

@Dao
public interface SubjectDao {
    @Insert
    void insert(Subject subject);


    @Query("SELECT * from subject")
    List<Subject> getAllWords();

    @Query("SELECT * FROM subject")
    public LiveData<List<Subject>> loadSubject();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSubjects(Subject... subjects);

    @Update
    public void updateSubjects(Subject... subjects);
}
