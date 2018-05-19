package com.example.tutu.facedectection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.setting.adapter.SettingAdapter;
import com.example.tutu.facedectection.setting.bean.SettingItemData;
import com.example.tutu.facedectection.base.BaseFragment;

import java.util.ArrayList;

/**
 * 人脸解锁的配置页面
 */


public class FaceSettingFragment extends BaseFragment
{
    private RecyclerView mRecyclerView;

    @Override
    public int getLayoutId()
    {
        return R.layout.face_setting_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);

        TextView titleView = view.findViewById(R.id.lsq_title);
        titleView.setText(R.string.lsq_setting);

        initRecView(view);
    }

    public void initRecView(View view)
    {
        mRecyclerView = view.findViewById(R.id.lsq_recylcer_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SettingAdapter adapter = new SettingAdapter(getActivity(), getSettingItemData());
        mRecyclerView.setAdapter(adapter);

        // 添加分割线,必须使用AppTheme,否则无效
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取设置页面的数据
     *
     * @return
     */
    public ArrayList<SettingItemData> getSettingItemData()
    {
        ArrayList<SettingItemData> dataList = new ArrayList<>();

        for (SettingItemData itemData : SettingItemData.values())
        {
            dataList.add(itemData);
        }

        return dataList;
    }
}
