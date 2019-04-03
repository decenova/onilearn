package com.onilearnapp.onilearnapp.Model;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private static final int TRUE = 0;
    private static final int FALSE = 1;
    private static final int MULTIPLE_CHOICE = 2;
    private int id;
    private String content;
    private int typeQuestion;
    private List<Answer> answers;
    private List<Answer> trueAnswers;

    public Question(String content, int typeQuestion) {
        this.content = content;
        this.typeQuestion = typeQuestion;
    }

    public Question(int id, String content, int typeQuestion) {
        this.id = id;
        this.content = content;
        this.typeQuestion = typeQuestion;
    }

    public Question(String content, int typeQuestion, List<Answer> answers) {
        this.content = content;
        this.typeQuestion = typeQuestion;
        this.answers = answers;
        this.trueAnswers = new ArrayList<>();
        for (Answer answer :
                answers) {
            if (answer.isRight()){
                trueAnswers.add(answer);
            }
        }
    }

    public Question(int id, String content, int typeQuestion, List<Answer> answers) {
        this.id = id;
        this.content = content;
        this.typeQuestion = typeQuestion;
        this.answers = answers;
        this.trueAnswers = new ArrayList<>();
        for (Answer answer :
                answers) {
            if (answer.isRight()){
                trueAnswers.add(answer);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(int typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getTrueAnswers() {
        return trueAnswers;
    }
}
