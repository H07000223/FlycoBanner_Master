package com.flyco.banner.widget.Banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.banner.R;
import com.flyco.banner.anim.BaseAnimator;
import com.flyco.banner.widget.Banner.base.BaseBanner;

import java.util.ArrayList;

public abstract class BaseIndicaorBanner<E, T extends BaseIndicaorBanner<E, T>> extends BaseBanner<E, T> {
    private ArrayList<ImageView> indicatorViews = new ArrayList<>();

    public static final int STYLE_DRAWABLE_RESOURCE = 0;
    public static final int STYLE_CORNER_RECTANGLE = 1;
    private int indicatorStyle;

    private int indicatorWidth;
    private int indicatorHeight;
    private int indicatorGap;
    private int indicatorCornerRadius;

    private Drawable selectDrawable;
    private Drawable unSelectDrawable;
    private int selectColor;
    private int unselectColor;

    private Class<? extends BaseAnimator> selectAnimClass;
    private Class<? extends BaseAnimator> unselectAnimClass;

    private LinearLayout ll_indicators;

    public BaseIndicaorBanner(Context context) {
        this(context, null, 0);
    }

    public BaseIndicaorBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicaorBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseIndicaorBanner);
        indicatorStyle = ta.getInt(R.styleable.BaseIndicaorBanner_bib_indicatorStyle, STYLE_CORNER_RECTANGLE);
        indicatorWidth = ta.getDimensionPixelSize(R.styleable.BaseIndicaorBanner_bib_indicatorWidth, dp2px(6));
        indicatorHeight = ta.getDimensionPixelSize(R.styleable.BaseIndicaorBanner_bib_indicatorHeight, dp2px(6));
        indicatorGap = ta.getDimensionPixelSize(R.styleable.BaseIndicaorBanner_bib_indicatorGap, dp2px(6));
        indicatorCornerRadius = ta.getDimensionPixelSize(R.styleable.BaseIndicaorBanner_bib_indicatorCornerRadius, dp2px(3));
        selectColor = ta.getColor(R.styleable.BaseIndicaorBanner_bib_indicatorSelectColor, Color.parseColor("#ffffff"));
        unselectColor = ta.getColor(R.styleable.BaseIndicaorBanner_bib_indicatorUnselectColor, Color.parseColor("#88ffffff"));

        int selectRes = ta.getResourceId(R.styleable.BaseIndicaorBanner_bib_indicatorSelectRes, 0);
        int unselectRes = ta.getResourceId(R.styleable.BaseIndicaorBanner_bib_indicatorUnselectRes, 0);
        ta.recycle();

        //create indicator container
        ll_indicators = new LinearLayout(context);
        ll_indicators.setGravity(Gravity.CENTER);

        setIndicatorSelectorRes(unselectRes, selectRes);
    }

    @Override
    public View onCreateIndicator() {
        if (indicatorStyle == STYLE_CORNER_RECTANGLE) {//rectangle
            this.unSelectDrawable = getDrawable(unselectColor, indicatorCornerRadius);
            this.selectDrawable = getDrawable(selectColor, indicatorCornerRadius);
        }

        int size = list.size();
        indicatorViews.clear();

        ll_indicators.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView iv = new ImageView(context);
            iv.setImageDrawable(i == currentPositon ? selectDrawable : unSelectDrawable);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(indicatorWidth,
                    indicatorHeight);
            lp.leftMargin = i == 0 ? 0 : indicatorGap;
            ll_indicators.addView(iv, lp);
            indicatorViews.add(iv);
        }

        setCurrentIndicator(currentPositon);

        return ll_indicators;
    }

    @Override
    public void setCurrentIndicator(int position) {
        for (int i = 0; i < indicatorViews.size(); i++) {
            indicatorViews.get(i).setImageDrawable(i == position ? selectDrawable : unSelectDrawable);
        }
        try {
//            Log.d(TAG, "position--->" + position);
//            Log.d(TAG, "lastPositon--->" + lastPositon);
            if (selectAnimClass != null) {
                if (position == lastPositon) {
                    selectAnimClass.newInstance().playOn(indicatorViews.get(position));
                } else {
                    selectAnimClass.newInstance().playOn(indicatorViews.get(position));
                    if (unselectAnimClass == null) {
                        selectAnimClass.newInstance().interpolator(new ReverseInterpolator()).playOn(indicatorViews.get(lastPositon));
                    } else {
                        unselectAnimClass.newInstance().playOn(indicatorViews.get(lastPositon));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** set indicator style,STYLE_DRAWABLE_RESOURCE or STYLE_CORNER_RECTANGLE */
    public T setIndicatorStyle(int indicatorStyle) {
        this.indicatorStyle = indicatorStyle;
        return (T) this;
    }

    /** set indicator width, unit dp,default 6dp */
    public T setIndicatorWidth(float indicatorWidth) {
        this.indicatorWidth = dp2px(indicatorWidth);
        return (T) this;
    }

    /** set indicator height,unit dp,default 6dp */
    public T setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = dp2px(indicatorHeight);
        return (T) this;
    }

    /** set gap between two indicators,unit dp,default 6dp */
    public T setIndicatorGap(float indicatorGap) {
        this.indicatorGap = dp2px(indicatorGap);
        return (T) this;
    }

    /** set indicator select color for STYLE_CORNER_RECTANGLE,default "#ffffff" */
    public T setIndicatorSelectColor(int selectColor) {
        this.selectColor = selectColor;
        return (T) this;
    }

    /** set indicator unselect color for STYLE_CORNER_RECTANGLE,default "#88ffffff" */
    public T setIndicatorUnselectColor(int unselectColor) {
        this.unselectColor = unselectColor;
        return (T) this;
    }

    /** set indicator corner raduis for STYLE_CORNER_RECTANGLE,unit dp,default 3dp */
    public T setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.indicatorCornerRadius = dp2px(indicatorCornerRadius);
        return (T) this;
    }

    /** set indicator select and unselect drawable resource for STYLE_DRAWABLE_RESOURCE */
    @SuppressWarnings("deprecation")
    public T setIndicatorSelectorRes(int unselectRes, int selectRes) {
        try {
            if (indicatorStyle == STYLE_DRAWABLE_RESOURCE) {
                if (selectRes != 0) {
                    this.selectDrawable = getResources().getDrawable(selectRes);
                }
                if (unselectRes != 0) {
                    this.unSelectDrawable = getResources().getDrawable(unselectRes);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return (T) this;
    }

    /** set indicator select anim */
    public T setSelectAnimClass(Class<? extends BaseAnimator> selectAnimClass) {
        this.selectAnimClass = selectAnimClass;
        return (T) this;
    }

    /** set indicator unselect anim */
    public T setUnselectAnimClass(Class<? extends BaseAnimator> unselectAnimClass) {
        this.unselectAnimClass = unselectAnimClass;
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
}
