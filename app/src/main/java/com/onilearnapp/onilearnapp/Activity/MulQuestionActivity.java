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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Adapter.AnswerAdapter;
import com.onilearnapp.onilearnapp.Model.Answer;
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

public class MulQuestionActivity extends AppCompatActivity {
    private final int MAX_NUM_OF_QUESTION = 10;
    private Course course;
    private List<Question> questions;
    private List<Answer> answers;
    private boolean[] selected;
    private boolean isRight;
    private int index, point;
    private JSONObject jsonExam, jsonQuestion, jsonAnswer;
    private JSONArray jsonQuestionArray, jsonAnswerArray;
    ArrayAdapter<Answer> arrayAdapter;
    CustomDialog customDialog;
    TextView questionText;
    ListView listView;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mul_question);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setTitle("");

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        listView = (ListView) findViewById(R.id.questionListView);
        questionText = (TextView) findViewById(R.id.txtTitle);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setText("SUBMIT");

        course = (Course) getIntent().getSerializableExtra("course");
        index = 0;
        point = 0;
        isRight = false;
        questions = new ArrayList<Question>();
        answers = new ArrayList<>();
        jsonExam = new JSONObject();
        jsonQuestionArray = new JSONArray();
        arrayAdapter = new AnswerAdapter(this, answers);
        listView.setAdapter(arrayAdapter);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutAnswer);

                if (!selected[i]) {
                    layout.setBackgroundResource(R.drawable.round_corner_green);
                    selected[i] = true;
                } else {
                    layout.setBackgroundResource(R.drawable.round_corner);
                    selected[i] = false;
                }
            }
        });

        getQuestion();
        arrayAdapter.notifyDataSetChanged();
    }

    private void getQuestion() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.app_web) + "/api/courses/" + course.getId() + "/random_multichoice_questions/" + MAX_NUM_OF_QUESTION;
//        url = getResources().getString(R.string.app_web) + "/api/courses/" + course.getId() + "/questions/";

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
                                List<Answer> answerList = new ArrayList<>();
                                JSONArray JSONAnswerList = object.getJSONArray("answers");
                                for (int j = 0; j < JSONAnswerList.length(); j++) {
                                    JSONObject JSONAnswer = JSONAnswerList.getJSONObject(j);
                                    int answerId = JSONAnswer.getInt("id");
                                    String answerContent = JSONAnswer.getString("content");
                                    boolean answerIsRight = JSONAnswer.getBoolean("is_right");
                                    answerList.add(new Answer(answerId, answerContent, answerIsRight));
                                }
                                questions.add(new Question(id, content, result, answerList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Volley error:", e.getMessage());
                            }
                        }
                        index = -1;
                        nextQuestion();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Volley Response Error", volleyError.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void putDataToJson() {
        jsonQuestion = new JSONObject();
        jsonAnswerArray = new JSONArray();
        try {
            jsonQuestion.put("question_id", questions.get(index).getId());
            jsonQuestion.put("user_is_right", isRight);
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    jsonAnswer = new JSONObject();
                    jsonAnswer.put("answer_id_user", answers.get(i).getId());
                    jsonAnswerArray.put(jsonAnswer);
                }
            }
            jsonQuestion.put("answer_details", jsonAnswerArray);
            jsonQuestionArray.put(jsonQuestion);
        } catch (JSONException e) {
            Log.d("Json error: ", e.getMessage());
        }
    }

    private void postResult() {
        if (Token.tmpToken != null) {
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

    private void nextQuestion() {
        index++;
        if (index >= questions.size()) {
            postResult();
            Intent intent = new Intent(this, AnswerActivity.class);
            intent.putExtra("answer_full", (Serializable) questions);
            intent.putExtra("point", point);
            startActivity(intent);
            return;
        }
        questionText.setText(questions.get(index).getContent());
        selected = new boolean[questions.get(index).getAnswers().size()];
        answers.clear();
        answers.addAll(questions.get(index).getAnswers());
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSubmitClick(View view) {
        boolean isTrue = false;
        boolean isCheck = false;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] && answers.get(i).isRight()) {
                isTrue = true;
                isCheck = true;
            } else if (selected[i] && !answers.get(i).isRight()) {
                isTrue = false;
                isCheck = true;
                break;
            }
        }
        if (isCheck) {
            if (isTrue) {
                point++;
                isRight = true;
            } else {
                isRight = false;
            }
            putDataToJson();
            nextQuestion();
        } else {
            Toast.makeText(this, "Please choose a answer!", Toast.LENGTH_SHORT).show();
        }
    }
}
