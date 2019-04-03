package com.onilearnapp.onilearnapp.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class TaskAndSubject {
    @Embedded
    public Task task;
//    @Relation(parentColumn = "id", entityColumn = "subjectId", entity = Subject.class)
    @Embedded
    public Subject subject;
}
