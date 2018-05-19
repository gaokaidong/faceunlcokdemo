package com.example.tutu.facedectection.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.SettingDialog;
import com.example.tutu.facedectection.setting.bean.SettingItemData;
import com.example.tutu.facedectection.setting.bean.SettingItemType;
import com.example.tutu.facedectection.setting.bean.SettingTypeValue;
import com.example.tutu.facedectection.setting.delegate.DialogValueDelegate;
import com.example.tutu.facedectection.setting.model.ISettingDialogModel;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;

import java.util.ArrayList;

/**
 * 人脸设置页面适配器
 */


public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingHolder>
{
    /** 上下文 */
    private Context mContext;

    /** 数据集合 */
    private ArrayList<SettingItemData> mDataList;

    public SettingAdapter(Context context, ArrayList<SettingItemData> dataList)
    {
        this.mContext = context;
        this.mDataList = dataList;
    }

    /**
     * 开关类型的布局
     *
     * @return
     */
    public int getSwitchLayoutId()
    {
        return R.layout.setting_cell_view_switch;
    }

    /**
     * 自定义数值类型的布局
     *
     * @return
     */
    public int getCustomValueLayoutId()
    {
        return R.layout.setting_cell_view_custom_value;
    }

    /**
     * 底部的布局
     *
     * @return
     */
    public int getFooterLayoutId()
    {
        return R.layout.setting_cell_view_footer;
    }

    @Override
    public SettingHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        SettingHolder holder = null;
        if (mContext == null) return holder;

        int currentLayoutId = 0;

        // 根据不同类型匹配不同的布局文件
        if (viewType == SettingTypeValue.CUSTOM_VALUE)
            currentLayoutId = getCustomValueLayoutId();
        else if (viewType == SettingTypeValue.SWITCH_BTN)
            currentLayoutId = getSwitchLayoutId();
        else if (viewType == SettingTypeValue.FOOTER_VALUE)
            currentLayoutId = getFooterLayoutId();

        View rootView = LayoutInflater.from(mContext).inflate(currentLayoutId, null);
        holder = new SettingHolder(rootView, viewType);

        return holder;
    }

    @Override
    public void onBindViewHolder(SettingHolder holder, int position)
    {
        if (mDataList == null || mDataList.size() == 0) return;

        SettingItemData itemData = mDataList.get(position);

        holder.setItemData(itemData);
        holder.bindData();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mDataList == null || mDataList.size() == 0) return -1;

        SettingItemData itemData = mDataList.get(position);
        int viewType = itemData.mItemType.mType;

        return viewType;
    }

    @Override
    public int getItemCount()
    {
        if (mDataList == null) return 0;

        return mDataList.size();
    }

    public class SettingHolder extends RecyclerView.ViewHolder implements DialogValueDelegate
    {
        /** 提示内容 */
        public TextView mHintText;

        /** Item名称 */
        public TextView mItemName;

        /** 开关按钮 */
        public Switch mSwitchBtn;

        /** 自定义数值 */
        public TextView mCustomValue;

        /** 数据 */
        public SettingItemData mItemData;

        public SettingDialog mDialog;

        public SettingHolder(View itemView)
        {
            super(itemView);
        }

        public SettingHolder(View itemView, int viewType)
        {
            this(itemView);
            itemView.setOnClickListener(mOnClickListener);

            mHintText = itemView.findViewById(R.id.lsq_hint_text);
            mItemName = itemView.findViewById(R.id.lsq_item_name);

            if (viewType == SettingTypeValue.SWITCH_BTN)
            {
                mSwitchBtn = itemView.findViewById(R.id.lsq_switch_btn);
                mSwitchBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
            }
            else if (viewType == SettingTypeValue.CUSTOM_VALUE)
                mCustomValue = itemView.findViewById(R.id.lsq_value_text);
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mItemData.mItemType == SettingItemType.CUSTOM_VALUE)
                {
                    mDialog = new SettingDialog(mContext);
                    ISettingDialogModel model = mItemData.mModel;
                    mDialog.setDialogModel(model);
                    mDialog.setDialogSize(getmDialogSize());
                    mDialog.setDelegate(SettingHolder.this);
                    mDialog.show();
                }
            }
        };

        /**
         * 自定义数值mDialog, 选中值时会调用此方法
         * 在这里修改对应参数的默认值
         *
         * @param value
         */
        @Override
        public void onUpdateValue(String value)
        {
            if (mItemData == SettingItemData.TIMING_LIMIT)
                mItemData.mDefaultValue = Long.valueOf(value);
            else
                 mItemData.mDefaultValue = Float.valueOf(value);

            mDialog.destroy();
        }

        @Override
        public void onUpdateShownText(String value)
        {
            mCustomValue.setText(value);
        }

        /**
         * 获取mDialog大小
         *
         * @return
         */
        public TuSdkSize getmDialogSize()
        {
            TuSdkSize size = new TuSdkSize();
            size.width = TuSdkContext.getScreenSize().width * 2 / 3;
            size.height = TuSdkContext.getScreenSize().width * 2 / 3;

            return size;
        }

        /**
         * 监听Switch开关
         */
        private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (mItemData == null) return;

                mItemData.mDefaultValue = isChecked;
            }
        };

        public void setItemData(SettingItemData itemData)
        {
            this.mItemData = itemData;
        }

        /**
         * 绑定数据
         */
        public void bindData()
        {
            if (mItemName != null)
                this.mItemName.setText(mItemData.mItemName);

            if (mHintText != null)
                this.mHintText.setText(mItemData.mHintText);

            if (this.mSwitchBtn != null)
                this.mSwitchBtn.setChecked((Boolean) mItemData.mDefaultValue);

            if (this.mCustomValue != null)
                this.mCustomValue.setText(String.valueOf(mItemData.mDefaultValue));

            if (StringHelper.isEmpty(mItemData.mHintText) && mHintText != null)
                this.mHintText.setVisibility(View.GONE);
        }
    }
}
