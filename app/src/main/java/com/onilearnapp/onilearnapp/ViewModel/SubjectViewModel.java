package com.onilearnapp.onilearnapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.Repository.SubjectRepository;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {
    private SubjectRepository subjectRepository;
    private LiveData<List<Subject>> subjects;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        subjectRepository = new SubjectRepository(application);
        subjects = subjectRepository.getAllSubjects();
    }
}
