package com.example.tutu.facedectection.setting.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.tutu.facedectection.R;

import java.util.List;

/**
 * 设置页面二级菜单Dialog String数据类型适配器
 */


public class StringValueAdapter extends BaseAdapter<String, StringValueAdapter.CustomValueHolder>
{
    public StringValueAdapter(Context context, List dataList)
    {
        super(context, dataList);
    }

    @Override
    public int getLayoutId()
    {
        return R.layout.custom_value_cell_view;
    }

    @Override
    public CustomValueHolder getHolder(View view)
    {
        CustomValueHolder holder = new CustomValueHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomValueHolder holder, int position)
    {
                if (mDataList == null || holder == null) return;

        String data = mDataList.get(position);
        holder.setItemData(data);
        holder.bindData();
    }

    public class CustomValueHolder extends BaseAdapter<String, StringValueAdapter.CustomValueHolder>.BaseHolder implements View.OnClickListener
    {
        // 数值文本
        private TextView mValueText;

        private String mItemData;

        public CustomValueHolder(View itemView)
        {
            super(itemView);

            if (itemView == null) return;

            mValueText = itemView.findViewById(R.id.lsq_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setItemData(String data)
        {
            this.mItemData = data;
        }

        @Override
        public void onClick(View v)
        {
            if (mDelegate == null) return;

            mDelegate.onUpdateShownText(mItemData);
            mDelegate.onUpdateValue(mItemData);
        }

        @Override
        public void bindData()
        {
            mValueText.setText(mItemData);
        }
    }
}
