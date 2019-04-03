package com.onilearnapp.onilearnapp.Model;

import java.io.Serializable;

public class Course implements Serializable {
    private int id;
    private String name;
    private String iconUrl;
    private boolean typeCourse;

    public Course(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Course(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Course(int id, String name, String iconUrl, boolean typeCourse) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.typeCourse = typeCourse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isTypeCourse() {
        return typeCourse;
    }

    public void setTypeCourse(boolean typeCourse) {
        this.typeCourse = typeCourse;
    }
}