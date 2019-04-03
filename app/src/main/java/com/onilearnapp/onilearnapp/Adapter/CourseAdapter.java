package com.onilearnapp.onilearnapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Activity.MulQuestionActivity;
import com.onilearnapp.onilearnapp.Activity.QuestionTrueFalseActivity;
import com.onilearnapp.onilearnapp.Model.Course;
import com.onilearnapp.onilearnapp.R;

import java.util.List;

public class CourseAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Course> courseList;
    private String webAppUrl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public CourseAdapter(Context context, int layout, List<Course> courseList) {
        this.context = context;
        this.layout = layout;
        this.courseList = courseList;
        webAppUrl = context.getResources().getString(R.string.app_web);
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            private final LruCache<String, Bitmap> imageCache = new LruCache<>(cacheSize);

            @Override
            public Bitmap getBitmap(String s) {
                return imageCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                imageCache.put(s, bitmap);
            }
        });
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        final Course course = courseList.get(position);
        TextView txtSubjectName = (TextView) convertView.findViewById(R.id.txtSubjectName);
        NetworkImageView imgSubjectIcon = (NetworkImageView) convertView.findViewById(R.id.imgSubjectIcon);
        imgSubjectIcon.setDefaultImageResId(R.drawable.default_icon);

        txtSubjectName.setText(course.getName());
        imgSubjectIcon.setImageUrl(webAppUrl + course.getIconUrl(), imageLoader);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Select a subject " + subjectList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent;
                if (course.isTypeCourse()) {
                    intent = new Intent(context, MulQuestionActivity.class);
                } else {
                    intent = new Intent(context, QuestionTrueFalseActivity.class);
                }
                intent.putExtra("course", course);
                context.startActivity(intent);


            }
        });

        return convertView;
    }
}
