package com.onilearnapp.onilearnapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.Model.Answer;
import com.onilearnapp.onilearnapp.Model.Question;
import com.onilearnapp.onilearnapp.R;

import java.util.List;

public class AnswerFullAdapter extends ArrayAdapter<Question> {

    private Context context;
    private List<Question> questionList;

    public AnswerFullAdapter(@NonNull Context context, @NonNull List<Question> objects) {
        super(context, 0, objects);
        this.context = context;
        this.questionList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Question question = questionList.get(position);
        //get correct answer
        //String answerString = questionList.get(position).getAnswers().get(0);

        StringBuilder answerString = new StringBuilder();
        if (question.getTypeQuestion() != 2){
            answerString.append((question.getTypeQuestion() == 0)?"True":"False");
        } else {
            List<Answer> answers = question.getTrueAnswers();
            for (int i = 0; i < answers.size(); i++) {
                answerString.append(answers.get(i).getContent());
                if (i < answers.size() - 1)
                    answerString.append(", ");
            }
        }

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_answer_full, parent, false);
        }

        TextView asQuestion = (TextView) convertView.findViewById(R.id.asQuestion);
        TextView asAnswer = (TextView) convertView.findViewById(R.id.asAnswer);
        asQuestion.setText(question.getContent());
        asAnswer.setText(answerString);

        return convertView;

    }
}
