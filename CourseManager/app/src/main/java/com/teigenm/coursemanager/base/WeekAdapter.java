package com.teigenm.coursemanager.base;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.model.WeekEntity;

import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.VH>{
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView text_day;
        public VH(View v) {
            super(v);
            text_day= (TextView) v.findViewById(R.id.text_home_day);
        }
    }

    private List<WeekEntity> mDatas;
    public WeekAdapter(List<WeekEntity> data) {
        this.mDatas = data;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        WeekEntity week =mDatas.get(position);
        holder.text_day.setText(week.getDay());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout_items,parent, false);
        return new VH(v);
    }
}
