package com.flyco.bannersamples.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.flyco.banner.widget.Banner.BaseIndicaorBanner;
import com.flyco.bannersamples.R;
import com.flyco.bannersamples.utils.ViewFindUtils;

public class SimpleTextBanner extends BaseIndicaorBanner<String, SimpleTextBanner> {
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
    public void onTitleSlect(TextView tv, int position) {
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(context, R.layout.adapter_simple_text, null);
        TextView tv = ViewFindUtils.find(inflate, R.id.tv);
        tv.setText(list.get(position));

        return inflate;
    }
}
