package com.onilearnapp.onilearnapp.AppDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.onilearnapp.onilearnapp.Dao.CategoryDao;
import com.onilearnapp.onilearnapp.Dao.SubjectDao;
import com.onilearnapp.onilearnapp.Dao.TaskDao;
import com.onilearnapp.onilearnapp.Dao.TokenDao;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Model.Token;
import com.onilearnapp.onilearnapp.Utils.Converters;

@Database(entities = {Task.class, Subject.class, Category.class, Token.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();
    public abstract SubjectDao subjectDao();
    public abstract TokenDao tokenDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if (instance == null){
            synchronized (AppDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context,AppDatabase.class,"onilearn_database").build();
                }
            }
        }
        return instance;
    }
}
