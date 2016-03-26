package com.flyco.banner.widget.LoopViewPager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
    private int mScrollSpeed = 450;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, int scrollSpeed) {
        super(context, interpolator);
        this.mScrollSpeed = scrollSpeed;
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(int scrollFactor) {
        this.mScrollSpeed = scrollFactor;
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.mScrollSpeed);
    }
}
