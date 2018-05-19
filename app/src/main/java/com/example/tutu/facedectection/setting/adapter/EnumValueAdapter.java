package com.example.tutu.facedectection.setting.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.bean.LiveModeType;

import java.util.List;

/**
 * 设置页面二级菜单Dialog 枚举数据类型适配器
 */


public class EnumValueAdapter extends BaseAdapter<LiveModeType, EnumValueAdapter.EnumValueHolder>
{
    public EnumValueAdapter(Context context, List<LiveModeType> dataList)
    {
        super(context, dataList);
    }

    @Override
    public int getLayoutId()
    {
        return R.layout.custom_value_cell_view;
    }

    @Override
    public EnumValueHolder getHolder(View view)
    {
        EnumValueHolder holder = new EnumValueHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EnumValueHolder holder, int position)
    {
        if (mDataList == null || holder == null) return;

        LiveModeType data = mDataList.get(position);
        holder.setItemData(data);
        holder.bindData();
    }

    public class EnumValueHolder extends BaseAdapter<LiveModeType, EnumValueAdapter.EnumValueHolder>.BaseHolder implements View.OnClickListener
    {
        // 数值文本
        private TextView mValueText;

        private LiveModeType mItemData;

        public EnumValueHolder(View itemView)
        {
            super(itemView);

            if (itemView == null) return;

            mValueText = itemView.findViewById(R.id.lsq_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setItemData(LiveModeType data)
        {
            this.mItemData = data;
        }

        @Override
        public void onClick(View v)
        {
            if (mDelegate == null) return;

            mDelegate.onUpdateShownText(String.valueOf(mItemData.mModeName));
            mDelegate.onUpdateValue(String.valueOf(mItemData.mType));
        }

        @Override
        public void bindData()
        {
            mValueText.setText(String.valueOf(mItemData.mModeName));
        }

    }
}
