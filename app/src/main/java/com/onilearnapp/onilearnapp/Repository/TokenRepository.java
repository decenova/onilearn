package com.onilearnapp.onilearnapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.onilearnapp.onilearnapp.AppDatabase.AppDatabase;
import com.onilearnapp.onilearnapp.Dao.TokenDao;
import com.onilearnapp.onilearnapp.Model.Token;

public class TokenRepository {
    private static TokenRepository instance;

    private TokenDao tokenDao;
    private Token token;


    TokenRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        tokenDao = db.tokenDao();
        token = tokenDao.loadToken(Token.DEFAULT_ID);
    }

    public static TokenRepository getInstance(Application application) {

        if (instance == null){
            synchronized (TokenRepository.class){
                instance = new TokenRepository(application);
            }
        }
        return instance;
    }

    public Token getToken() {
        return token;
    }

    public void insert (Token token) {
        new insertAsyncTask(tokenDao).execute(token);
    }
    public void delete (Token token) {
        new deleteAsyncTask(tokenDao).execute(token);
    }
    private static class loadAsyncTask extends AsyncTask<Integer, Void, Token> {
        private Token token;
        private TokenDao asyncTaskDao;

        loadAsyncTask(Token token, TokenDao dao) {
            asyncTaskDao = dao;
            this.token = token;
        }


        @Override
        protected Token doInBackground(Integer... integers) {
            return asyncTaskDao.loadToken(integers[0]);
        }

        @Override
        protected void onPostExecute(Token token) {
            super.onPostExecute(token);
            if (token != null){
                this.token = token;
            }
        }
    }
    private static class insertAsyncTask extends AsyncTask<Token, Void, Void> {

        private TokenDao asyncTaskDao;

        insertAsyncTask(TokenDao dao) {
            asyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Token... tokens) {
            asyncTaskDao.insertToken(tokens[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Token, Void, Void> {

        private TokenDao asyncTaskDao;

        deleteAsyncTask(TokenDao dao) {
            asyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Token... tokens) {
            asyncTaskDao.deleteToken(tokens[0]);
            return null;
        }
    }
}
