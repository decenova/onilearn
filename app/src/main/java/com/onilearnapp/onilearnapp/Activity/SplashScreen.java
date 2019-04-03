package com.onilearnapp.onilearnapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.onilearnapp.onilearnapp.Activity.MainActivity;
import com.onilearnapp.onilearnapp.Model.Token;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Repository.TokenRepository;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    TokenRepository tokenRepository = TokenRepository.getInstance(getApplication());
                    if (tokenRepository.getToken() != null)
                        Token.tmpToken = tokenRepository.getToken().getToken();
                    sleep(500);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        myThread.start();
    }
}
