package com.onilearnapp.onilearnapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Model.Token;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Repository.TokenRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reflect();
    }

    private void reflect() {
        edtEmail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
    }

    public void clickToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void clickToLogin(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = this.getResources().getString(R.string.app_web) + "/api/sessions";
//        url = "https://onilearn.herokuapp.com/api/all_categories";
        Map<String, String> params = new HashMap();
        params.put("email", email);
        params.put("password", password);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String info;
                String token;
                try {
                    info = response.getString("info");
                    if (info.equals("Invalid Email or Password")) {
                        Toast.makeText(getApplicationContext(), "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Log.d("Volley error", e.getMessage());
                }

                try {
                    token = response.getString("authentication_token");
                    TokenRepository tokenRepository = TokenRepository.getInstance(getApplication());
                    tokenRepository.insert(new Token(Token.DEFAULT_ID, token));
                    Token.tmpToken = token;
                    Toast.makeText(getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    Log.d("Volley error", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
