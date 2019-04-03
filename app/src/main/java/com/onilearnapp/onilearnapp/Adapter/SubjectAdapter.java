package com.onilearnapp.onilearnapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Activity.SubjectActivity;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.R;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHoder> {

    private Context context;
    private List<Subject> subjectList;
    private String webAppUrl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public SubjectAdapter(Context context) {
        this(context, null);
    }

    public SubjectAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
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

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder holder, final int position) {
        Subject subject = subjectList.get(position);
        holder.txtSubjectName.setText(subject.getName());
        holder.imgSubjectIcon.setImageUrl(webAppUrl + subject.getIconUrl(), imageLoader);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = ((Activity) context).getIntent().getStringExtra("type");
                if (type != null) {
                    if (type.equals("make a task")) {
                        Intent intent = new Intent();
                        intent.putExtra("SubjectDao", subjectList.get(position));
                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
                        ((Activity) context).finish();
                    }
                } else {
                    Intent intent = new Intent(context, SubjectActivity.class);
                    intent.putExtra("subject", subjectList.get(position));
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (subjectList != null)
            return subjectList.size();
        else
            return 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        TextView txtSubjectName;
        NetworkImageView imgSubjectIcon;

        public ViewHoder(View itemView) {
            super(itemView);
            txtSubjectName = (TextView) itemView.findViewById(R.id.txtSubjectName);
            imgSubjectIcon = (NetworkImageView) itemView.findViewById(R.id.imgSubjectIcon);
            imgSubjectIcon.setDefaultImageResId(R.drawable.default_icon);
        }
    }

}
