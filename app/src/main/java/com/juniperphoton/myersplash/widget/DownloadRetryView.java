package com.juniperphoton.myersplash.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juniperphoton.myersplash.R;
import com.juniperphoton.myersplash.interfaces.SetThemeColor;
import com.juniperphoton.myersplash.utils.ColorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadRetryView extends FrameLayout implements SetThemeColor {

    @BindView(R.id.widget_retry_rl)
    RelativeLayout retryRL;

    @BindView(R.id.retry_tv)
    TextView retryTextView;

    @BindView(R.id.widget_retry_btn)
    View retyBtn;

    @BindView(R.id.delete_btn)
    ImageView deleteView;

    public DownloadRetryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_download_retry_view, this);
        ButterKnife.bind(this);
    }

    public void setOnClickDeleteListener(View.OnClickListener listener) {
        deleteView.setOnClickListener(listener);
    }

    public void setOnClickRetryListener(View.OnClickListener listener){
        retyBtn.setOnClickListener(listener);
    }

    @Override
    public void setThemeBackColor(int color) {
        retryRL.setBackground(new ColorDrawable(color));
        retryTextView.setTextColor(ColorUtil.isColorLight(color) ? Color.BLACK : Color.WHITE);
        if (ColorUtil.isColorLight(color)) {
            deleteView.setImageResource(R.drawable.vector_ic_delete_black);
        }
    }
}
