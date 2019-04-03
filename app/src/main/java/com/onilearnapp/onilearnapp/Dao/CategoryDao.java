package com.onilearnapp.onilearnapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.CategoryAndSubject;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    public LiveData<List<Category>> loadCategory();

    @Query("SELECT * FROM category")
    public LiveData<List<CategoryAndSubject>> loadCategoryWithSubject();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCategories(Category... Categories);

    @Update
    public void updateCategories(Category... Categories);
}
