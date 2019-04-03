package com.onilearnapp.onilearnapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.Model.Answer;
import com.onilearnapp.onilearnapp.R;

import java.util.List;

public class AnswerAdapter extends ArrayAdapter<Answer> {

    private Context context;
    private List<Answer> answerList;

    public AnswerAdapter(@NonNull Context context, @NonNull List<Answer> objects) {
        super(context, 0, objects);
        this.context = context;
        this.answerList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Answer answer = answerList.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_answer, parent, false);
        }

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layoutAnswer);
        layout.setBackgroundResource(R.drawable.round_corner);
        TextView textView = (TextView) convertView.findViewById(R.id.txtAnswer);
        textView.setText(answer.getContent());

        return convertView;

    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

}
