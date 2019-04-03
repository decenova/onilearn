package com.onilearnapp.onilearnapp.Adapter;

import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.onilearnapp.onilearnapp.Activity.SubjectActivity;
import com.onilearnapp.onilearnapp.Model.Subject;
import com.onilearnapp.onilearnapp.Model.Task;
import com.onilearnapp.onilearnapp.Model.TaskAndSubject;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.ViewModel.TaskViewModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHoder> {
    private Context context;

    private List<TaskAndSubject> taskList;
    private String webAppUrl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public TaskAdapter(Context context) {
        this(context, null);
    }

    public TaskAdapter(Context context, List<TaskAndSubject> taskList) {
        this.context = context;
        this.taskList = taskList;

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

    public void setTaskList(List<TaskAndSubject> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        TaskAndSubject taskAndSubject = taskList.get(position);
        Task task = taskAndSubject.task;
        final Subject subject = taskAndSubject.subject;
        holder.txtTaskStartTime.setText(task.getStartTimeString());
        holder.txtTaskEndTime.setText(task.getEndTimeString());
        holder.txtSubjectName.setText(subject.getName());
        holder.imgSubjectIcon.setImageUrl(webAppUrl + subject.getIconUrl(), imageLoader);
        holder.txtCourse.setText("Basic");
        holder.txtTestType.setText("Multiple choice");
        if (task.isAlarm())
            holder.imgBellIcon.setImageResource(R.drawable.notification);
        else
            holder.imgBellIcon.setImageResource(R.drawable.notifications_disable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubjectActivity.class);
                intent.putExtra("subject", subject);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (taskList != null)
            return taskList.size();
        else
            return 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        TextView txtSubjectName, txtTaskStartTime, txtTaskEndTime, txtCourse, txtTestType;
        ImageView imgBellIcon;
        NetworkImageView imgSubjectIcon;


        public ViewHoder(View itemView) {
            super(itemView);
            txtTaskStartTime = (TextView) itemView.findViewById(R.id.txtTaskStartTime);
            txtTaskEndTime = (TextView) itemView.findViewById(R.id.txtTaskEndTime);
            txtSubjectName = (TextView) itemView.findViewById(R.id.txtSubjectName);
            imgSubjectIcon = (NetworkImageView) itemView.findViewById(R.id.imgSubjectIcon);
            txtCourse = (TextView) itemView.findViewById(R.id.txtCourse);
            txtTestType = (TextView) itemView.findViewById(R.id.txtTestType);
            imgBellIcon = (ImageView) itemView.findViewById(R.id.imgBellIcon);
            imgSubjectIcon.setDefaultImageResId(R.drawable.default_icon);
        }
    }
}
