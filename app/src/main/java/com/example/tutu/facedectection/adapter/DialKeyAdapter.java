package com.example.tutu.facedectection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.ScreenSizeUtils;

/**
 * Created by LiuHang on 11/29/2017.
 */


public class DialKeyAdapter extends RecyclerView.Adapter<DialKeyAdapter.DialKeyHolder>
{
    private Context mContext;

    /**
     * 点击事件监听
     */
    public interface OnDialKeyClickListener
    {
        void onClick(String value);
    }

    private OnDialKeyClickListener mOnDialKeyClickListener;

    public void setOnDialKeyClickListener(OnDialKeyClickListener onDialKeyClickListener)
    {
        this.mOnDialKeyClickListener = onDialKeyClickListener;
    }

    public DialKeyAdapter(Context context)
    {
        this.mContext = context;
    }

    @Override
    public DialKeyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dial_key_cell_view, null);

        return new DialKeyHolder(view);
    }

    @Override
    public void onBindViewHolder(DialKeyHolder holder, int position)
    {
        // 最后一个位置设置居中
        if (position == getItemCount() -1)
        {
            holder.mItemView.setLayoutParams(new FrameLayout.LayoutParams(ScreenSizeUtils.getWidth() - 2 * Constants.DIAL_KEY_SPACE_LEFT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        holder.mNumText.setText(String.valueOf((position + 1) == 10 ? 0 : (position + 1)));
        holder.mNumText.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount()
    {
        return 10;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (mOnDialKeyClickListener != null && v instanceof TextView)
                mOnDialKeyClickListener.onClick(((TextView)v).getText().toString());
        }
    };

    public class DialKeyHolder extends RecyclerView.ViewHolder
    {
        private TextView mNumText;
        private View mItemView;

        public DialKeyHolder(View itemView)
        {
            super(itemView);
            this.mItemView = itemView;
            mNumText = itemView.findViewById(R.id.lsq_num_text);
        }
    }
}
