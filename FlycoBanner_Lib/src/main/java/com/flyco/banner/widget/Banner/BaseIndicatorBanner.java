package com.flyco.banner.widget.Banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.banner.R;
import com.flyco.banner.anim.BaseAnimator;
import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseIndicatorBanner<E, T extends BaseIndicatorBanner<E, T>> extends BaseBanner<E, T> {
    public static final int STYLE_DRAWABLE_RESOURCE = 0;
    public static final int STYLE_CORNER_RECTANGLE = 1;

    private ArrayList<ImageView> mIndicatorViews = new ArrayList<>();
    private int mIndicatorStyle;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorGap;
    private int mIndicatorCornerRadius;

    private Drawable mSelectDrawable;
    private Drawable mUnSelectDrawable;
    private int mSelectColor;
    private int mUnselectColor;
    private boolean isSnap;

    private Class<? extends BaseAnimator> mSelectAnimClass;
    private Class<? extends BaseAnimator> mUnselectAnimClass;

    private RelativeLayout mLlIndicators;

    private ImageView movingItem;

    public BaseIndicatorBanner(Context context) {
        this(context, null, 0);
    }

    public BaseIndicatorBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicatorBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseIndicatorBanner);
        mIndicatorStyle = ta.getInt(R.styleable.BaseIndicatorBanner_bb_indicatorStyle, STYLE_CORNER_RECTANGLE);
        mIndicatorWidth = ta.getDimensionPixelSize(R.styleable.BaseIndicatorBanner_bb_indicatorWidth, dp2px(6));
        mIndicatorHeight = ta.getDimensionPixelSize(R.styleable.BaseIndicatorBanner_bb_indicatorHeight, dp2px(6));
        mIndicatorGap = ta.getDimensionPixelSize(R.styleable.BaseIndicatorBanner_bb_indicatorGap, dp2px(6));
        mIndicatorCornerRadius = ta.getDimensionPixelSize(R.styleable.BaseIndicatorBanner_bb_indicatorCornerRadius, dp2px(3));
        mSelectColor = ta.getColor(R.styleable.BaseIndicatorBanner_bb_indicatorSelectColor, Color.parseColor("#ffffff"));
        mUnselectColor = ta.getColor(R.styleable.BaseIndicatorBanner_bb_indicatorUnselectColor, Color.parseColor("#88ffffff"));

        int selectRes = ta.getResourceId(R.styleable.BaseIndicatorBanner_bb_indicatorSelectRes, 0);
        int unselectRes = ta.getResourceId(R.styleable.BaseIndicatorBanner_bb_indicatorUnselectRes, 0);
        isSnap = ta.getBoolean(R.styleable.BaseIndicatorBanner_bb_indicatorIsSnap, true);
        ta.recycle();

        //create indicator container
        mLlIndicators = new RelativeLayout(context);

        setIndicatorSelectorRes(unselectRes, selectRes);
        setIndicatorSelectorDrawable(mUnselectColor, mSelectColor, mIndicatorCornerRadius);
    }

    /**
     * 设置显示器选中以及未选中资源(for STYLE_DRAWABLE_RESOURCE)
     */
    public T setIndicatorSelectorRes(int unselectRes, int selectRes) {
        if (mIndicatorStyle == STYLE_DRAWABLE_RESOURCE) {
            if (selectRes != 0) {
                this.mSelectDrawable = getResources().getDrawable(selectRes);
            }
            if (unselectRes != 0) {
                this.mUnSelectDrawable = getResources().getDrawable(unselectRes);
            }
        }
        return (T) this;
    }

    /**
     * 设置显示器选中以及未选中Drawable(for STYLE_CORNER_RECTANGLE)
     */
    public T setIndicatorSelectorDrawable(int mUnselectColor, int mSelectColor, int mIndicatorCornerRadius) {
        if (mIndicatorStyle == STYLE_CORNER_RECTANGLE) {//rectangle
            this.mUnSelectDrawable = getDrawable(mUnselectColor, mIndicatorCornerRadius);
            this.mSelectDrawable = getDrawable(mSelectColor, mIndicatorCornerRadius);
        }
        return (T) this;
    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }

    private GradientDrawable getDrawable(int color, float raduis) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(raduis);
        drawable.setColor(color);

        return drawable;
    }

    @Override
    public View onCreateIndicator(int mCurrentPosition, List<E> mDatas) {

        int size = mDatas.size();
        mIndicatorViews.clear();

        mLlIndicators.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageDrawable(mCurrentPosition == i ? mSelectDrawable : mUnSelectDrawable);
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            rp.leftMargin = i * (mIndicatorGap + mIndicatorWidth);

            mLlIndicators.addView(iv, rp);
            mIndicatorViews.add(iv);
        }

        if (isSnap) {
            movingItem = new ImageView(getContext());
            movingItem.setImageDrawable(mSelectDrawable);
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            mLlIndicators.addView(movingItem, rp);
        }

        return mLlIndicators;
    }

    @Override
    public void onIndicatorSelect(int mLastPositon, int position) {

        mIndicatorViews.get(mLastPositon).setImageDrawable(mUnSelectDrawable);
        mIndicatorViews.get(position).setImageDrawable(mSelectDrawable);

        try {
            if (mSelectAnimClass != null) {
                mSelectAnimClass.newInstance().playOn(mIndicatorViews.get(position));
                if (position != mLastPositon) {
                    if (mUnselectAnimClass == null) {
                        mSelectAnimClass.newInstance().interpolator(new ReverseInterpolator()).playOn(mIndicatorViews.get(mLastPositon));
                    } else {
                        mUnselectAnimClass.newInstance().playOn(mIndicatorViews.get(mLastPositon));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isSnap)
            layoutMovingItem(position, positionOffset);
    }

    private void layoutMovingItem(final int position, final float positionOffset) {
        if (movingItem == null) {
            throw new IllegalStateException("forget to create movingItem?");
        }

        ImageView item = mIndicatorViews.get(position);

        float x = ViewHelper.getX(item) + (mIndicatorGap + mIndicatorHeight) * positionOffset;
        ViewHelper.setTranslationX(movingItem, x);
    }

    /**
     * 设置显示样式,STYLE_DRAWABLE_RESOURCE or STYLE_CORNER_RECTANGLE
     */
    public T setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        return (T) this;
    }

    /**
     * 设置显示宽度,单位dp,默认6dp
     */
    public T setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = dp2px(indicatorWidth);
        return (T) this;
    }

    /**
     * 设置显示器高度,单位dp,默认6dp
     */
    public T setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = dp2px(indicatorHeight);
        return (T) this;
    }

    /**
     * 设置两个显示器间距,单位dp,默认6dp
     */
    public T setIndicatorGap(float indicatorGap) {
        this.mIndicatorGap = dp2px(indicatorGap);
        return (T) this;
    }

    /**
     * 设置显示器选中颜色(for STYLE_CORNER_RECTANGLE),默认"#ffffff"
     */
    public T setIndicatorSelectColor(int selectColor) {
        this.mSelectColor = selectColor;
        return (T) this;
    }

    /**
     * 设置显示器未选中颜色(for STYLE_CORNER_RECTANGLE),默认"#88ffffff"
     */
    public T setIndicatorUnselectColor(int unselectColor) {
        this.mUnselectColor = unselectColor;
        return (T) this;
    }

    /**
     * 设置显示器圆角弧度(for STYLE_CORNER_RECTANGLE),单位dp,默认3dp
     */
    public T setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        return (T) this;
    }

    /**
     * 设置显示器选中动画
     */
    public T setSelectAnimClass(Class<? extends BaseAnimator> selectAnimClass) {
        this.mSelectAnimClass = selectAnimClass;
        return (T) this;
    }

    /**
     * 设置显示器未选中动画
     */
    public T setUnselectAnimClass(Class<? extends BaseAnimator> unselectAnimClass) {
        this.mUnselectAnimClass = unselectAnimClass;
        return (T) this;
    }

}
