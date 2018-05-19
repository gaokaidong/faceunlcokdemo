package com.example.tutu.facedectection.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tutu.facedectection.setting.delegate.DialogValueDelegate;

import java.util.List;


/**
 * 设置适配器基础类
 */


public abstract class BaseAdapter<T, V extends BaseAdapter.BaseHolder> extends RecyclerView.Adapter<V>
{
    // 数据模型
    protected List<T> mDataList;

    protected Context mContext;

    public BaseAdapter(Context context, List<T> dataList)
    {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public abstract int getLayoutId();

    protected DialogValueDelegate mDelegate;

    public void setDelegate(DialogValueDelegate delegate)
    {
        this.mDelegate = delegate;
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType)
    {
        V holder = null;
        if (mContext == null) return holder;

        // LayoutInflater.from(mContext).inflate(getLayoutId(), null)这种写法,即使CellView设置match_parent,也不会填充满整个控件
        View rootView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        holder = getHolder(rootView);
        return holder;
    }

    public abstract V getHolder(View view);


    @Override
    public void onBindViewHolder(V holder, int position)
    {
        if (mDataList == null || holder == null) return;

        T data = mDataList.get(position);
        holder.setItemData(data);
        holder.bindData();
    }

    @Override
    public int getItemCount()
    {
        if (mDataList == null) return 0;

        return mDataList.size();
    }

    public abstract class BaseHolder extends RecyclerView.ViewHolder
    {

        public BaseHolder(View itemView)
        {
            super(itemView);
        }

        public abstract void setItemData(T data);

        public abstract void bindData();
    }
}
