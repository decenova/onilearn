package com.onilearnapp.onilearnapp.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Relation;

import java.util.List;


public class CategoryAndSubject {
    @Embedded
    public Category category;
    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = Subject.class)
    public List<Subject> subjects;
}
