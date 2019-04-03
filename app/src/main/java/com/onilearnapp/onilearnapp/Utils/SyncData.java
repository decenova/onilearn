package com.onilearnapp.onilearnapp.Utils;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Adapter.CategoryAdapter;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Repository.CategoryRepository;
import com.onilearnapp.onilearnapp.Repository.SubjectRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SyncData {

    public static void syncSubject(final Context context, Application application) {

        final CategoryRepository categoryRepository = new CategoryRepository(application);
        final SubjectRepository subjectRepository = new SubjectRepository(application);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.app_web) + "/api/all_categories";
//        url = "https://onilearn.herokuapp.com/api/all_categories";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
//                        Log.d("Volley", jsonArray.toString());
//                        Toast.makeText(view.getContext(), jsonArray.toString(), Toast.LENGTH_SHORT).show();

                        ArrayList<Category> categoryList = new ArrayList<>();
                        ArrayList<Subject> subjects = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONArray subjectJsonArray = object.getJSONArray("subjects");
                                for (int j = 0; j < subjectJsonArray.length(); j++) {
                                    JSONObject JSONSubject = subjectJsonArray.getJSONObject(j);
                                    subjects.add(new Subject(JSONSubject.getInt("id"), JSONSubject.getString("name"), JSONSubject.getJSONObject("image").getString("url"), JSONSubject.getInt("category_id")));
                                }
                                categoryList.add(new Category(object.getInt("id"), object.getString("name")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Volley error:", e.getMessage());
                            }
                        }
                        Category[] arr1 = new Category[categoryList.size()];
                        categoryRepository.insert(categoryList.toArray(arr1));
                        Subject[] arr2 = new Subject[subjects.size()];
                        subjectRepository.insert(subjects.toArray(arr2));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Volley error:", volleyError.toString());
//                        Toast.makeText(view.getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );

        Log.d("Volley:", "sync");

        requestQueue.add(jsonArrayRequest);
    }
}
