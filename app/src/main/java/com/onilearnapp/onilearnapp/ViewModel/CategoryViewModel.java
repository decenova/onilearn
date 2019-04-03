package com.onilearnapp.onilearnapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.CategoryAndSubject;
import com.onilearnapp.onilearnapp.Repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<CategoryAndSubject>> allCategoriesAndSubject;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
        allCategoriesAndSubject = categoryRepository.getAllCategoriesAndSubject();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<CategoryAndSubject>> getAllCategoriesAndSubject() {
        return allCategoriesAndSubject;
    }
}
