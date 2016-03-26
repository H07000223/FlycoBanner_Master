package com.flyco.banner.widget.Banner.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.banner.R;
import com.flyco.banner.widget.ScrollViewPager.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBanner<E, T extends BaseBanner<E, T>> extends RelativeLayout {
    /**
     * 日志
     */
    private static final String TAG = BaseBanner.class.getSimpleName();
    /**
     * 设备密度
     */
    protected DisplayMetrics mDisplayMetrics;
    /**
     * ViewPager
     */
    private AutoScrollViewPager mViewPager;
    /**
     * 数据源
     */
    private List<E> mDatas = new ArrayList<>();
    /**
     * 当前position
     */
    private int mCurrentPositon;
    /**
     * 上一个position
     */
    private int mLastPositon;
    /**
     * 多久后开始滚动
     */
    private long mDelay;
    /**
     * 滚动间隔
     */
    private long mPeriod;
    /**
     * 是否自动滚动
     */
    private boolean mIsAutoScrollEnable;
    /**
     * 切换动画
     */
    private Class<? extends ViewPager.PageTransformer> mTransformerClass;

    private int mItemWidth;
    private int mItemHeight;

    /**
     * 显示器和标题的直接父容器
     */
    private LinearLayout mLlBottomBar;
    /**
     * 最后一条item是否显示背景条
     */
    private boolean mIsBarShowWhenLast;

    /**
     * 显示器的的直接容器
     */
    private LinearLayout mLlIndicatorContainer;

    /**
     * 标题
     */
    private TextView mTvTitle;

    public BaseBanner(Context context) {
        this(context, null, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDisplayMetrics = context.getResources().getDisplayMetrics();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        float scale = ta.getFloat(R.styleable.BaseBanner_bb_scale, -1);

        boolean isLoopEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isLoopEnable, true);
        mDelay = ta.getInt(R.styleable.BaseBanner_bb_delay, 5);
        mPeriod = ta.getInt(R.styleable.BaseBanner_bb_period, 5);
        mIsAutoScrollEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isAutoScrollEnable, true);

        int barColor = ta.getColor(R.styleable.BaseBanner_bb_barColor, Color.TRANSPARENT);
        mIsBarShowWhenLast = ta.getBoolean(R.styleable.BaseBanner_bb_isBarShowWhenLast, true);
        int indicatorGravity = ta.getInt(R.styleable.BaseBanner_bb_indicatorGravity, Gravity.CENTER);
        float barPaddingLeft = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingLeft, dp2px(10));
        float barPaddingTop = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingTop, dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        float barPaddingRight = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingRight, dp2px(10));
        float barPaddingBottom = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingBottom, dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        int textColor = ta.getColor(R.styleable.BaseBanner_bb_textColor, Color.parseColor("#ffffff"));
        float textSize = ta.getDimension(R.styleable.BaseBanner_bb_textSize, sp2px(12.5f));
        boolean isTitleShow = ta.getBoolean(R.styleable.BaseBanner_bb_isTitleShow, true);
        boolean isIndicatorShow = ta.getBoolean(R.styleable.BaseBanner_bb_isIndicatorShow, true);
        ta.recycle();

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        mViewPager = new AutoScrollViewPager(context);
        mViewPager.setBoundaryLooping(isLoopEnable);

        mItemWidth = mDisplayMetrics.widthPixels;
        if (scale < 0) {//scale not set in xml
            if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
                mItemHeight = LayoutParams.MATCH_PARENT;
            } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
                mItemHeight = LayoutParams.WRAP_CONTENT;
            } else {
                int[] systemAttrs = {android.R.attr.layout_height};
                TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
                int h = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                a.recycle();
                mItemHeight = h;
            }
        } else {
            if (scale > 1) {
                scale = 1;
            }
            mItemHeight = (int) (mItemWidth * scale);
        }

        LayoutParams lp = new LayoutParams(mItemWidth, mItemHeight);
        addView(mViewPager, lp);


        //container of indicators and title
        mLlBottomBar = new LinearLayout(context);
        LayoutParams lp2 = new LayoutParams(mItemWidth, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        addView(mLlBottomBar, lp2);

        mLlBottomBar.setBackgroundColor(barColor);
        mLlBottomBar.setPadding((int) barPaddingLeft, (int) barPaddingTop, (int) barPaddingRight, (int) barPaddingBottom);
        mLlBottomBar.setClipChildren(false);
        mLlBottomBar.setClipToPadding(false);

        //container of indicators
        mLlIndicatorContainer = new LinearLayout(context);
        mLlIndicatorContainer.setGravity(Gravity.CENTER);
        mLlIndicatorContainer.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        mLlIndicatorContainer.setClipChildren(false);
        mLlIndicatorContainer.setClipToPadding(false);

        // title
        mTvTitle = new TextView(context);
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0F));
        mTvTitle.setSingleLine(true);
        mTvTitle.setTextColor(textColor);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTvTitle.setVisibility(isTitleShow ? VISIBLE : INVISIBLE);

        if (indicatorGravity == Gravity.CENTER) {
            mLlBottomBar.setGravity(Gravity.CENTER);
            mLlBottomBar.addView(mLlIndicatorContainer);
        } else {
            if (indicatorGravity == Gravity.RIGHT) {
                mLlBottomBar.setGravity(Gravity.CENTER_VERTICAL);
                mLlBottomBar.addView(mTvTitle);
                mLlBottomBar.addView(mLlIndicatorContainer);

                mTvTitle.setPadding(0, 0, dp2px(7), 0);
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setGravity(Gravity.LEFT);
            } else if (indicatorGravity == Gravity.LEFT) {
                mLlBottomBar.setGravity(Gravity.CENTER_VERTICAL);
                mLlBottomBar.addView(mLlIndicatorContainer);
                mLlBottomBar.addView(mTvTitle);

                mTvTitle.setPadding(dp2px(7), 0, 0, 0);
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setGravity(Gravity.RIGHT);
            }
        }
    }

    /**
     * 创建ViewPager的Item布局
     */
    public abstract View onCreateItemView(int position, List<E> mDatas);

    /**
     * 创建显示器
     */
    public View onCreateIndicator(int position, List<E> mDatas) {
        return new LinearLayout(getContext());
    }

    /**
     * 设置当前显示器的状态,选中或者未选中
     */
    public void onIndicatorSelect(int mLastPositon, int mCurrentPositon) {
    }

    /**
     *
     */
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onPageSelected(int position) {
    }

    /**
     * 覆写这个方法设置标题
     */
    public void onTitleSlect(TextView tv, int position, List<E> mDatas) {
    }

    /**
     * 设置数据源
     */
    public T setSource(List<E> list) {
        this.mDatas = list;
        return (T) this;
    }

    /**
     * 滚动延时,默认5秒
     */
    public T setDelay(long delay) {
        this.mDelay = delay;
        return (T) this;
    }

    /**
     * 滚动间隔,默认5秒
     */
    public T setPeriod(long period) {
        this.mPeriod = period;
        return (T) this;
    }

    /**
     * 设置是否支持自动滚动,默认true.仅对LoopViewPager有效
     */
    public T setAutoScrollEnable(boolean isAutoScrollEnable) {
        this.mIsAutoScrollEnable = isAutoScrollEnable;
        return (T) this;
    }

    /**
     * 设置页面切换动画
     */
    public T setTransformerClass(Class<? extends ViewPager.PageTransformer> transformerClass) {
        this.mTransformerClass = transformerClass;
        return (T) this;
    }

    /**
     * 设置底部背景条颜色,默认透明
     */
    public T setBarColor(int barColor) {
        mLlBottomBar.setBackgroundColor(barColor);
        return (T) this;
    }

    /**
     * 设置最后一条item是否显示背景条,默认true
     */
    public T setBarShowWhenLast(boolean isBarShowWhenLast) {
        this.mIsBarShowWhenLast = isBarShowWhenLast;
        return (T) this;
    }

    /**
     * 设置底部背景条padding,单位dp
     */
    public T barPadding(float left, float top, float right, float bottom) {
        mLlBottomBar.setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        return (T) this;
    }

    /**
     * 设置标题文字颜色,默认"#ffffff"
     */
    public T setTextColor(int textColor) {
        mTvTitle.setTextColor(textColor);
        return (T) this;
    }

    /**
     * 设置标题文字大小,单位sp,默认14sp
     */
    public T setTextSize(float textSize) {
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return (T) this;
    }

    /**
     * 设置是否显示标题,默认true
     */
    public T setTitleShow(boolean isTitleShow) {
        mTvTitle.setVisibility(isTitleShow ? VISIBLE : INVISIBLE);
        return (T) this;
    }

    /**
     * 设置是否显示显示器,默认true
     */
    public T setIndicatorShow(boolean isIndicatorShow) {
        mLlIndicatorContainer.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        return (T) this;
    }

    /**
     * 设置viewpager
     */
    private void setViewPager() {
        InnerBannerAdapter mInnerAdapter = new InnerBannerAdapter();
        mViewPager.setAdapter(mInnerAdapter);

        try {
            if (mTransformerClass != null) {
                mViewPager.setPageTransformer(true, mTransformerClass.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mInternalPageListener != null) {
            mViewPager.removeOnPageChangeListener(mInternalPageListener);
        }
        mViewPager.addOnPageChangeListener(mInternalPageListener);
    }

    private ViewPager.OnPageChangeListener mInternalPageListener = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mCurrentPositon = position;

            BaseBanner.this.onPageSelected(position);

            onIndicatorSelect(mLastPositon, mCurrentPositon);
            onTitleSlect(mTvTitle, mCurrentPositon, mDatas);
            mLlBottomBar.setVisibility(mCurrentPositon == mDatas.size() - 1 && !mIsBarShowWhenLast ? GONE : VISIBLE);

            mLastPositon = mCurrentPositon;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            BaseBanner.this.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            BaseBanner.this.onPageScrollStateChanged(state);
        }
    };

    /**
     * 开始滚动
     */
    public void startScroll() {
        if (mDatas == null) {
            throw new IllegalStateException("Data source is empty,you must setSource() before startScroll()");
        }

        if (mDatas.size() > 0 && mCurrentPositon > mDatas.size() - 1) {
            mCurrentPositon = 0;
        }

        setViewPager();
        onTitleSlect(mTvTitle, mCurrentPositon, mDatas);
        //create indicator
        View indicatorViews = onCreateIndicator(mCurrentPositon, mDatas);

        if (indicatorViews != null) {
            mLlIndicatorContainer.removeAllViews();
            mLlIndicatorContainer.addView(indicatorViews);
        }

        goOnScroll();
    }

    /**
     * 继续滚动(for LoopViewPager)
     */
    private void goOnScroll() {

        if (mIsAutoScrollEnable) {

            mViewPager.setInterval(mPeriod * 1000);
            mViewPager.startAutoScroll(mDelay * 1000);

            Log.d(TAG, this.getClass().getSimpleName() + "--->goOnScroll()");
        }
    }

    /**
     * 获取ViewPager对象
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    private class InnerBannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View inflate = onCreateItemView(position, mDatas);
            inflate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickL != null) {
                        mOnItemClickL.onItemClick(position);
                    }
                }
            });
            container.addView(inflate);

            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    protected int dp2px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

    protected float sp2px(float sp) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    private OnItemClickL mOnItemClickL;

    public void setOnItemClickL(OnItemClickL onItemClickL) {
        this.mOnItemClickL = onItemClickL;
    }

    public interface OnItemClickL {
        void onItemClick(int position);
    }
}
