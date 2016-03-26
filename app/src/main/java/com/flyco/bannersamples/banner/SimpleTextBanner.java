package com.flyco.bannersamples.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.flyco.bannersamples.R;
import com.flyco.bannersamples.utils.ViewFindUtils;

import java.util.List;

public class SimpleTextBanner extends BaseIndicatorBanner<String, SimpleTextBanner> {
    public SimpleTextBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleTextBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTextBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View onCreateItemView(int position, List<String> mDatas) {
        View inflate = View.inflate(getContext(), R.layout.adapter_simple_text, null);
        TextView tv = ViewFindUtils.find(inflate, R.id.tv);
        tv.setText(mDatas.get(position));

        return inflate;
    }
}
