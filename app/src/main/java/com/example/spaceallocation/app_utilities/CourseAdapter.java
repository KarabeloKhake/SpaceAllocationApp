package com.example.spaceallocation.app_utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.spaceallocation.R;
import com.example.spaceallocation.entities.Course;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    //Data Members
    private Context context;
    private List<Course> courses;

    //Constructor
    public CourseAdapter(Context context, List<Course> list) {
        this.context = context;
        courses = list;
    } //end overloaded constructor

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Data Members
        ImageView ivBook;
        TextView tvCourseCode, tvCourseName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBook = itemView.findViewById(R.id.ivBook);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                } //end onClick()
            });
        } //end ViewHolder()
    } //end class ViewHolder

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(view);
    } //end onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(courses.get(position));  //gets the course that was clicked
        holder.tvCourseCode.setText(courses.get(position).getCourseCode());
        holder.tvCourseName.setText(courses.get(position).getCourseName());
    } //end onBindViewHolder()

    @Override
    public int getItemCount() {
        return courses.size();
    } //end getItemCount()
} //end class CourseAdapter()
