package com.flyco.bannersamples.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.banner.widget.Banner.BaseIndicaorBanner;
import com.flyco.bannersamples.R;
import com.flyco.bannersamples.utils.ViewFindUtils;

public class SimpleGuideBanner extends BaseIndicaorBanner<Integer, SimpleGuideBanner> {
    public SimpleGuideBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBarShowWhenLast(false);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(context, R.layout.adapter_simple_guide, null);
        ImageView iv = ViewFindUtils.find(inflate, R.id.iv);
        TextView tv_jump = ViewFindUtils.find(inflate, R.id.tv_jump);

        final Integer resId = list.get(position);
        tv_jump.setVisibility(position == list.size() - 1 ? VISIBLE : GONE);

        Glide.with(context)
                .load(resId)
                .into(iv);

        tv_jump.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJumpClickL != null)
                    onJumpClickL.onJumpClick();
            }
        });

        return inflate;
    }

    private OnJumpClickL onJumpClickL;

    public interface OnJumpClickL {
        void onJumpClick();
    }

    public void setOnJumpClickL(OnJumpClickL onJumpClickL) {
        this.onJumpClickL = onJumpClickL;
    }
}
