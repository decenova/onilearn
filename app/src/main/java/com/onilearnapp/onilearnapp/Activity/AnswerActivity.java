package com.onilearnapp.onilearnapp.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.Adapter.AnswerFullAdapter;
import com.onilearnapp.onilearnapp.Model.Answer;
import com.onilearnapp.onilearnapp.Model.Question;
import com.onilearnapp.onilearnapp.R;

import java.util.ArrayList;
import java.util.List;

public class AnswerActivity extends AppCompatActivity {

    ListView listView;
    TextView totalMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setTitle("");

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setupAnswerList();
    }

    void setupAnswerList() {
        listView = (ListView) findViewById(R.id.answerListView);
        totalMark = findViewById(R.id.totalMark);

        List<Question> questions = (List<Question>) getIntent().getSerializableExtra("answer_full");
        int point = getIntent().getIntExtra("point", 0);

        totalMark.setText(point + "/" + questions.size());

        ArrayAdapter<Question> questionArrayAdapter = new AnswerFullAdapter(this, questions);

        listView.setAdapter(questionArrayAdapter);
        listView.setDivider(null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
