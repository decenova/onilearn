package com.onilearnapp.onilearnapp.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.onilearnapp.onilearnapp.AppDatabase.AppDatabase;
import com.onilearnapp.onilearnapp.Dao.SubjectDao;
import com.onilearnapp.onilearnapp.Model.Subject;

import java.util.List;

public class SubjectRepository {
    private SubjectDao subjectDao;
    private LiveData<List<Subject>> allSubjects;

    public SubjectRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.subjectDao = db.subjectDao();
        this.allSubjects = subjectDao.loadSubject();
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public void insert (Subject[] subject) {
        new insertAsyncTask(subjectDao).execute(subject);
    }

    private static class insertAsyncTask extends AsyncTask<Subject, Void, Void> {

        private SubjectDao asyncTaskDao;

        insertAsyncTask(SubjectDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Subject... params) {
            asyncTaskDao.insertSubjects(params);
            return null;
        }
    }
}
