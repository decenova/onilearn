package com.onilearnapp.onilearnapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Model.Course;
import com.onilearnapp.onilearnapp.Model.Question;
import com.onilearnapp.onilearnapp.Model.Token;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Utils.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionTrueFalseActivity extends AppCompatActivity {
    private final int MAX_NUM_OF_QUESTION = 10;
    private Course course;
    private List<Question> questions;
    private int index, point;
    private boolean isRight;
    private JSONObject jsonExam, jsonQuestion;
    private JSONArray jsonQuestionArray;
    CustomDialog customDialog;
    TextView questionText;
    Button trueButton, falseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_true_false);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        course = (Course) getIntent().getSerializableExtra("course");
        setTitle("");

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        reflect();

        index = 0;
        point = 0;
        isRight = false;
        questions = new ArrayList<Question>();
        jsonExam = new JSONObject();
        jsonQuestionArray = new JSONArray();
        getQuestion();
    }

    private void getQuestion() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.app_web) + "/api/courses/" + course.getId() + "/random_true_false_questions/" + MAX_NUM_OF_QUESTION;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String content = object.getString("content");
                                int result = object.getInt("type_question");
                                questions.add(new Question(id, content, result));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Volley error:", e.getMessage());
                            }
                        }
                        questionText.setText(questions.get(index).getContent());
                        trueButton.setEnabled(true);
                        falseButton.setEnabled(true);
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

    private void putDataToJson() {
        jsonQuestion = new JSONObject();
        try {
            jsonQuestion.put("question_id", questions.get(index).getId());
            jsonQuestion.put("user_is_right", isRight);
            jsonQuestionArray.put(jsonQuestion);

        } catch (JSONException e) {
            Log.d("Json error: ", e.getMessage());
        }
    }

    private void postResult() {
        if (Token.tmpToken != null){
            try {
                jsonExam.put("authentication_token", Token.tmpToken);
                jsonExam.put("total_mark", point);
                jsonExam.put("course_id", course.getId());
                jsonExam.put("exam_details", jsonQuestionArray);
                postJson(jsonExam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("JSONarray", jsonExam.toString());
        }
    }

    private void postJson(JSONObject object) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.app_web) + "/api/exams";

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley error:", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void reflect() {
        questionText = findViewById(R.id.questionText);
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
    }

    private void showResult(boolean selection) {
        int result = questions.get(index).getTypeQuestion();
        if ((result == 0 && selection) ||
                (result == 1 && !selection)) {
            point++;
            isRight = true;
            customDialog = new CustomDialog(this, "", R.drawable.true_icon);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //customDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
        } else {
            isRight = false;
            customDialog = new CustomDialog(this, "", R.drawable.false_icon);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();
        }
    }

    private void nextQuestion() {
        index++;
        if (index < questions.size()) {
            questionText.setText(questions.get(index).getContent());
        } else {
            if (customDialog != null)
                customDialog.cancel();
            postResult();
            Intent intent = new Intent(this, AnswerActivity.class);
            intent.putExtra("answer_full", (Serializable) questions);
            intent.putExtra("point", point);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTrueClick(View view) {
        showResult(true);
        putDataToJson();
        nextQuestion();
    }

    public void onFalseClick(View view) {
        showResult(false);
        putDataToJson();
        nextQuestion();
    }
}
