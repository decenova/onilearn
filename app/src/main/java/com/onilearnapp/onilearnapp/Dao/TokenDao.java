package com.onilearnapp.onilearnapp.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.onilearnapp.onilearnapp.Model.Token;

import java.util.List;

@Dao
public interface TokenDao {
    @Query("SELECT * FROM token WHERE token.id = :id LIMIT 1")
    public Token loadToken(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertToken(Token token);

    @Delete
    public void deleteToken(Token token);
}
