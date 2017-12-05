package com.zld.demo2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zld.demo2.R;
import com.zld.demo2.bean.CategoryBean;

import java.util.List;

/**
 * Created by lingdong on 2017/12/5.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.LeftViewHolder> {

    private Context mContext;
    private RecyclerView mRcv;
    private List<CategoryBean> mData;
    private int checkedItem = -1;                //选中的item项
    private OnItemClickListener mItemClickListener;

    public CategoryAdapter(Context mContext, RecyclerView recyclerView, List<CategoryBean> mData) {
        this.mContext = mContext;
        this.mRcv = recyclerView;
        this.mData = mData;
    }

    public void updateCheck(int position){
        resetCheck();
        CategoryBean bean = mData.get(position);
        bean.setChecked(true);
        mData.set(position,bean);
        checkedItem = position;
        mRcv.smoothScrollToPosition(position);
        notifyDataSetChanged();
    }

    public List<CategoryBean> getData() {
        return mData;
    }

    public int getCheckedItem() {
        return checkedItem;
    }

    public void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
    }

    private void resetCheck(){
        for (int i = 0; i < mData.size(); i++) {
            CategoryBean bean = mData.get(i);
            bean.setChecked(false);
            mData.set(i,bean);
        }
    }

    @Override
    public CategoryAdapter.LeftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeftViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_left,parent,false));
    }

    @Override
    public void onBindViewHolder(LeftViewHolder holder, final int position) {
        if(mData.get(position).isChecked()){
            holder.tvCategory.setTextColor(Color.RED);
        }else {
            holder.tvCategory.setTextColor(Color.GRAY);
        }
        holder.tvCategory.setText(mData.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCheck(position);
                if(mItemClickListener !=null){
                    mItemClickListener.onItemClick(view,position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class LeftViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategory;
        public LeftViewHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
