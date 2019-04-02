package com.teigenm.coursemanager.base;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.model.CourseEntity;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH>{
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView course_section;
        public final TextView course_name;
        public final TextView course_week;
        public final TextView course_remind;
        public VH(View v) {
            super(v);
            course_week= (TextView) v.findViewById(R.id.course_week);
            course_name = (TextView) v.findViewById(R.id.course_name);
            course_section = (TextView) v.findViewById(R.id.course_section);
            course_remind= (TextView) v.findViewById(R.id.course_remind);
        }
    }

    private List<CourseEntity> mDatas;
    public CourseAdapter(List<CourseEntity> data) {
        this.mDatas = data;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(VH holder, int position) {
        CourseEntity course=mDatas.get(position);
        holder.course_section.setText(course.getCsectionStart()+"-"+course.getCsectionEnd()+"节");
        holder.course_name.setText(course.getCname());
        holder.course_week.setText("第"+course.getCweekStart()+"-"+course.getCweekEnd()+"周");
        String s=course.getCremind()==1?"是":"否";
        holder.course_remind.setText("是否提醒："+s);
//        holder.itemView.setOnClickListener(new background_view.OnClickListener() {
//            @Override
//            public void onClick(background_view v) {
//                //item 点击事件
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_layout_items, parent, false);
        return new VH(v);
    }
}
