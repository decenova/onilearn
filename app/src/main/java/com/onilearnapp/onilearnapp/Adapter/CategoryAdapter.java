package com.onilearnapp.onilearnapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.CategoryAndSubject;
import com.onilearnapp.onilearnapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHoder> {

    private Context context;
    private List<CategoryAndSubject> categoryList;
    private SubjectAdapter subjectAdapter;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public CategoryAdapter(Context context, List<CategoryAndSubject> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setCategoryList(List<CategoryAndSubject> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        CategoryAndSubject categoryAndSubject = categoryList.get(position);
        Category category = categoryAndSubject.category;
        holder.txtCategoryName.setText(category.getName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        holder.lvSubject.setLayoutManager(layoutManager);
        subjectAdapter = new SubjectAdapter(context,categoryAndSubject.subjects);
        holder.lvSubject.setAdapter(subjectAdapter);
    }

    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        else
            return 0;
    }


    public class ViewHoder extends RecyclerView.ViewHolder{
        TextView txtCategoryName;
        RecyclerView lvSubject;
        public ViewHoder(View itemView) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);
            lvSubject = (RecyclerView) itemView.findViewById(R.id.lsSubject);
        }
    }
}
