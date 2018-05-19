package com.example.tutu.facedectection.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorreco.unlock.bean.FaceFeature;
import com.colorreco.unlock.bean.Person;
import com.example.tutu.facedectection.R;
import com.example.tutu.facedectection.adapter.ImageAdapter;
import com.example.tutu.facedectection.adapter.SpeceItemDecoration;
import com.example.tutu.facedectection.base.BaseFragment;
import com.example.tutu.facedectection.utils.Constants;
import com.example.tutu.facedectection.utils.DataUtils;

import org.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * 人脸识别前提示信息
 */


public class FaceDetectionHintFragment extends BaseFragment implements View.OnClickListener
{
    // 开始识别按钮
    private Button mStartDetectionBtn;

    private RecyclerView mImageListView;

    private ImageView mDefaultImage;

    @Override
    public int getLayoutId()
    {
        return R.layout.face_dect_hint_fragment;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);

        mStartDetectionBtn = view.findViewById(R.id.lsq_face_detect_start);
        mStartDetectionBtn.setOnClickListener(this);
        TextView titleView = view.findViewById(R.id.lsq_title);
        titleView.setText(R.string.lsq_add_face_info_hint);
        mDefaultImage = view.findViewById(R.id.lsq_face_image);

        initRecyclerView(view);
    }

    private ArrayList<Bitmap> getBitmapList()
    {
        ArrayList<Person> persons = DataUtils.getPersons();
        if (persons == null || persons.size() < 1) return null;

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (Person person : persons)
        {
           for (int i = 0, j = person.getBaseFeatureSize(); i<j ; i++)
           {
              FaceFeature faceFeature =  person.featureList.get(i);
              File tempFile = DataUtils.getTempFile(faceFeature.mRotation, person.personId);
              Bitmap bitmap = BitmapHelper.getBitmap(tempFile);
              if (bitmap == null) continue;

              bitmaps.add(bitmap);
           }
        }

        return bitmaps;
    }

    private void initRecyclerView(View view)
    {
        mImageListView = view.findViewById(R.id.lsq_face_image_list_view);
        ArrayList<Bitmap> bitmaps = getBitmapList();
        if (bitmaps == null || bitmaps.size() < 1)
        {
            mImageListView.setVisibility(View.GONE);
            return;
        }

        mDefaultImage.setVisibility(View.GONE);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        mImageListView.setLayoutManager(gridLayoutManager);

        mImageListView.addItemDecoration(new SpeceItemDecoration(Constants.DIAL_KEY_SPACE_LEFT, Constants.DIAL_KEY_SPACE_LEFT,
                Constants.DIAL_KEY_SPACE_LEFT, Constants.DIAL_KEY_SPACE_LEFT));
        ImageAdapter imageAdapter = new ImageAdapter(bitmaps, getActivity());
        mImageListView.setAdapter(imageAdapter);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mStartDetectionBtn)
        {
            showFragment(new FaceDetectionFragment());
        }
    }
}
