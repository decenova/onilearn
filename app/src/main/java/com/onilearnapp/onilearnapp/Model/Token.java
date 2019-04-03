package com.onilearnapp.onilearnapp.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Token implements Serializable{
    @PrimaryKey
    private int id;
    private String token;

    @Ignore
    public static final int DEFAULT_ID = 0;
    @Ignore
    public static String tmpToken;

    public Token() {
    }

    public Token(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
