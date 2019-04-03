package com.onilearnapp.onilearnapp.Activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Adapter.CourseAdapter;
import com.onilearnapp.onilearnapp.Adapter.SubjectAdapter;
import com.onilearnapp.onilearnapp.Model.Course;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private GridView gridView;
    private Subject subject;
    private ArrayList<Course> courses;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        subject = (Subject) getIntent().getSerializableExtra("subject");
        setTitle(subject.getName());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        gridView = (GridView) findViewById(R.id.subjectContent);

        this.setupSubjectContent();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(subject.getName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getSerializableExtra("subject") != null){
            subject = (Subject) getIntent().getSerializableExtra("subject");
            setTitle(subject.getName());
        }
    }

    private void setupSubjectContent() {
        courses = new ArrayList<>();
        courseAdapter = new CourseAdapter(this, R.layout.subject_item, courses);
        gridView.setAdapter(courseAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.app_web) + "/api/subjects/" + subject.getId() + "/courses";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String iconUrl = object.getJSONObject("icon").getString("url");
                                iconUrl = (iconUrl != null && !iconUrl.equals("null"))? iconUrl:subject.getIconUrl();
                                String typeCourse = object.getString("type_course");
                                boolean isTypeCourse = (typeCourse != null && !typeCourse.equals("null"))?Boolean.parseBoolean(typeCourse):true;
                                courses.add(new Course(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        iconUrl,
                                        isTypeCourse)
                                );
                                courseAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Volley error:", e.getMessage());
                            }
                        }
                        courseAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("SubjectDao", volleyError.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
