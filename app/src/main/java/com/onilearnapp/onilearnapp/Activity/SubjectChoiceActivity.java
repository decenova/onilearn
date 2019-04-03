package com.onilearnapp.onilearnapp.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Adapter.CategoryAdapter;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.CategoryAndSubject;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.ViewModel.CategoryViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubjectChoiceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView txtStatus;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_choice);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reflect();
        loadData();


    }

    private void reflect() {
        recyclerView = (RecyclerView) findViewById(R.id.lvCategory);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
    }

    private void loadData(){
//        SyncData.syncSubject(getContext(),getActivity().getApplication());


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategoriesAndSubject().observe(this, new Observer<List<CategoryAndSubject>>() {
            @Override
            public void onChanged(@Nullable List<CategoryAndSubject> categories) {
                categoryAdapter.setCategoryList(categories);
            }
        });
        recyclerView.setAdapter(categoryAdapter);

        txtStatus.setText("");
    }
}
