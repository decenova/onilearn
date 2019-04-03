package com.onilearnapp.onilearnapp.Fragment;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Adapter.CategoryAdapter;
import com.onilearnapp.onilearnapp.Dao.CategoryDao;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.CategoryAndSubject;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Repository.CategoryRepository;
import com.onilearnapp.onilearnapp.Utils.SyncData;
import com.onilearnapp.onilearnapp.ViewModel.CategoryViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txtStatus;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);

        reflect(view);
        loadData();
        return view;
    }

    private void reflect(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.lvCategory);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
    }

    private void loadData(){
//        SyncData.syncSubject(getContext(),getActivity().getApplication());


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(getContext());
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategoriesAndSubject().observe(this, new Observer<List<CategoryAndSubject>>() {
            boolean isSync = false;
            @Override
            public void onChanged(@Nullable List<CategoryAndSubject> categories) {
                if (!isSync && categories.size() <= 0 ){
                    SyncData.syncSubject(getContext(),getActivity().getApplication());
                    isSync = true;
                }
                categoryAdapter.setCategoryList(categories);
            }
        });
        recyclerView.setAdapter(categoryAdapter);

        txtStatus.setText("");
    }

}
