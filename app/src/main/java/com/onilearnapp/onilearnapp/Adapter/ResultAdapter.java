package com.onilearnapp.onilearnapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.Model.Answer;
import com.onilearnapp.onilearnapp.Model.Question;
import com.onilearnapp.onilearnapp.R;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHoder> {
    private Context context;
    private List<Question> questions;


    public ResultAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ResultAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ViewHoder holder, int position) {
        Question question = questions.get(position);
        holder.txtQuestion.setText(question.getContent());
        if (question.getTypeQuestion() != 2){
            String result = (question.getTypeQuestion() == 0)?"true":"false";
            holder.txtAnswer.setText(result);
        } else {
            StringBuilder result = new StringBuilder();
            List<Answer> trueAnswers = question.getTrueAnswers();
            for (int i = 0; i < trueAnswers.size(); i++) {
                Answer answer = trueAnswers.get(i);
                result.append(answer.getContent());
                if (i != trueAnswers.size() - 1){
                    result.append(", ");
                }
            }
            holder.txtAnswer.setText(result);
        }
    }


    @Override
    public int getItemCount() {
        if (questions != null)
            return questions.size();
        else
            return 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView txtQuestion, txtAnswer;

        public ViewHoder(View itemView) {
            super(itemView);
            txtQuestion = (TextView) itemView.findViewById(R.id.question);
            txtAnswer = (TextView) itemView.findViewById(R.id.answer);
        }
    }
}
