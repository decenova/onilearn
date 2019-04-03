package com.onilearnapp.onilearnapp.Model;

import java.io.Serializable;

public class Answer implements Serializable{
    private int id;
    private String content;
    private boolean isRight;

    public Answer(String content, boolean isRight) {
        this.content = content;
        this.isRight = isRight;
    }

    public Answer(int id, String content, boolean isRight) {
        this.id = id;
        this.content = content;
        this.isRight = isRight;
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

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
