package com.flyco.banner.transform;

import android.view.View;

import com.flyco.banner.widget.LoopViewPager.ViewPagerCompat;
import com.nineoldandroids.view.ViewHelper;

public class FlowTransformer implements ViewPagerCompat.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        ViewHelper.setRotationY(page, position * -30f);
    }
}
