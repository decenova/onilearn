package com.onilearnapp.onilearnapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Model.Token;
import com.onilearnapp.onilearnapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyzeFragment extends Fragment {
    ViewGroup layoutAnalyze, layoutLogin;
    TextView txtName, txtNumOfExam, txtTotalPoint;

    public AnalyzeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        reflect(view);
        showAnalyze();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAnalyze();
    }

    private void reflect(View view) {
        layoutAnalyze = view.findViewById(R.id.layoutAnalyze);
        layoutLogin = view.findViewById(R.id.layoutLogin);
        txtName = view.findViewById(R.id.txtName);
        txtNumOfExam = view.findViewById(R.id.txtNumOfExam);
        txtTotalPoint = view.findViewById(R.id.txtTotalPoint);
    }

    private void showAnalyze() {
        if (Token.tmpToken != null) {
            layoutLogin.setVisibility(View.GONE);
            layoutAnalyze.setVisibility(View.VISIBLE);
            getAnalyze();
        } else {
            layoutAnalyze.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
        }
    }

    private void getAnalyze() {
        JSONObject object = new JSONObject();
        try {
            object.put("authentication_token", Token.tmpToken);
            postJson(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postJson(JSONObject object) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = getResources().getString(R.string.app_web) + "/api/info_of_exams_by_user";

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            txtName.setText(response.getString("name_user"));
                            txtNumOfExam.setText(response.getString("amount_of_exams"));
                            txtTotalPoint.setText(response.getString("total_mark"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley error:", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
