package com.onilearnapp.onilearnapp.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.onilearnapp.onilearnapp.AppDatabase.AppDatabase;
import com.onilearnapp.onilearnapp.Dao.CategoryDao;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.CategoryAndSubject;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<CategoryAndSubject>> allCategoriesAndSubject;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.categoryDao = db.categoryDao();
        this.allCategories = categoryDao.loadCategory();
        this.allCategoriesAndSubject = categoryDao.loadCategoryWithSubject();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<CategoryAndSubject>> getAllCategoriesAndSubject() {
        return allCategoriesAndSubject;
    }

    public void insert (Category[] category) {
        new insertAsyncTask(categoryDao).execute(category);
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao asyncTaskDao;

        insertAsyncTask(CategoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            asyncTaskDao.insertCategories(params);
            return null;
        }
    }
}
