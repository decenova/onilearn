package com.onilearnapp.onilearnapp.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId"))
public class Subject implements Serializable {
    @PrimaryKey
    private int id;
    @NonNull
    private String name;
    private String iconUrl;
    @ColumnInfo(name = "categoryId")
    private int categoryId;

    public Subject() {
    }

    public Subject(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Subject(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Subject(int id, @NonNull String name, String iconUrl, int categoryId) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
