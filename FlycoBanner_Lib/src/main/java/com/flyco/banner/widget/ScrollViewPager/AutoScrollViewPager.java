package com.flyco.banner.widget.ScrollViewPager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.flyco.banner.widget.LoopViewPager.FixedSpeedScroller;
import com.flyco.banner.widget.LoopViewPager.LoopViewPager;

import java.lang.reflect.Field;

/**
 * Auto Scroll View Pager
 * <ul>
 * <strong>Basic Setting and Usage</strong>
 * <li>{@link #startAutoScroll()} start auto scroll, or {@link #startAutoScroll(long)} start auto scroll delayed</li>
 * <li>{@link #stopAutoScroll()} stop auto scroll</li>
 * <li>{@link #setInterval(long)} set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}</li>
 * </ul>
 * <ul>
 * <strong>Advanced Settings and Usage</strong>
 * <li>{@link #setDirection(int)} set auto scroll direction</li>
 * <li>{@link #setSlideBorderMode(int)} set how to process when sliding at the last or first item</li>
 * <li>{@link #setStopScrollWhenTouch(boolean)} set whether stop auto scroll when touching, default is true</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-30
 */
public class AutoScrollViewPager extends LoopViewPager implements Handler.Callback {

    public static final int DEFAULT_INTERVAL = 1500;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    /**
     * do nothing when sliding at the last or first item *
     */
    public static final int SLIDE_BORDER_MODE_NONE = 0;
    /**
     * cycle when sliding at the last or first item *
     */
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    /**
     * deliver event to parent when sliding at the last or first item *
     */
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;
    /**
     * the message.what for scroll
     */
    public static final int SCROLL_WHAT = 0;
    /**
     * auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} *
     */
    private long interval = DEFAULT_INTERVAL;
    /**
     * auto scroll direction, default is {@link #RIGHT} *
     */
    private int direction = RIGHT;
    /**
     * whether stop auto scroll when touching, default is true *
     */
    private boolean stopScrollWhenTouch = true;
    /**
     * how to process when sliding at the last or first item, default is {@link #SLIDE_BORDER_MODE_NONE} *
     */
    private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
    /**
     * whether animating when auto scroll at the last or first item *
     */
    private boolean isBorderAnimation = true;
    /**
     * scroll factor for auto scroll animation, default is 1.0 *
     */
    private int autoScrollFactor = 450;
    /**
     * scroll factor for swipe scroll animation, default is 1.0
     **/
    private int swipeScrollFactor = 450;

    private Handler handler;
    private boolean isAutoScroll = false;
    private boolean isStopByTouch = false;
    private float touchX = 0f, downX = 0f, touchY = 0f, downY = 0f;
    private FixedSpeedScroller scroller = null;

    public AutoScrollViewPager(Context paramContext) {
        super(paramContext);
        init();
    }

    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new Handler(this);
        setViewPagerScroller();
    }

    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage(interval + scroller.getDuration() / autoScrollFactor);
    }

    /**
     * start auto scroll
     *
     * @param delayTimeInMills first scroll delay time
     */
    public void startAutoScroll(long delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling
     */
    public void setAutoScrollDurationFactor(int scrollFactor) {
        autoScrollFactor = scrollFactor;
    }

    /**
     * set the factor by which the duration of sliding animation will change while swiping
     */
    public void setSwipeScrollDurationFactor(int scrollFactor) {
        swipeScrollFactor = scrollFactor;
    }


    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    /**
     * set ViewPager scroller to change animation duration when sliding
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = LoopViewPager.class.getSuperclass().getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

            scroller = new FixedSpeedScroller(getContext(), interpolator, swipeScrollFactor);
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * scroll only once
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        if (adapter == null) return;
        int currentItem = getCurrentItem();
        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        setCurrentItem(nextItem, true);
        sendScrollMessage(interval + scroller.getDuration());
    }

    /**
     * <ul>
     * if stopScrollWhenTouch is true
     * <li>if event is down, stop auto scroll.</li>
     * <li>if event is up, start auto scroll again.</li>
     * </ul>
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        PagerAdapter adapter = getAdapter();
        if (adapter == null)
            return super.dispatchTouchEvent(ev);

        int action = MotionEventCompat.getActionMasked(ev);

        touchX = ev.getX();
        touchY = ev.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            downX = touchX;
            downY = touchY;
            if (isAutoScroll && stopScrollWhenTouch) {
                isStopByTouch = true;
                scroller.setScrollDurationFactor(swipeScrollFactor);
                stopAutoScroll();
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (isStopByTouch && stopScrollWhenTouch) {
                startAutoScroll();
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
            int currentItem = getCurrentItem();
            int pageCount = adapter.getCount();
            /**
             * current index is first one and slide to right or current index is last one and slide to left.<br/>
             * if slide border mode is to parent, then requestDisallowInterceptTouchEvent false.<br/>
             * else scroll to last one when current item is first one, scroll to first one when current item is last
             * one.
             */
            if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }

        }

        if (Math.abs(downX - touchX) > Math.abs(downY - touchY)) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SCROLL_WHAT:
                if (isAutoScroll) {
                    scroller.setScrollDurationFactor(autoScrollFactor);
                    scrollOnce();
                }
            default:
                break;
        }
        return false;
    }

    /**
     * get auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
     *
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
     *
     * @param interval the interval to set
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * get auto scroll direction
     *
     * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }

    /**
     * set auto scroll direction
     *
     * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * whether stop auto scroll when touching, default is true
     *
     * @return the stopScrollWhenTouch
     */
    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }

    /**
     * set whether stop auto scroll when touching, default is true
     *
     * @param stopScrollWhenTouch
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    /**
     * get how to process when sliding at the last or first item
     *
     * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT},
     * {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public int getSlideBorderMode() {
        return slideBorderMode;
    }

    /**
     * set how to process when sliding at the last or first item
     *
     * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT},
     *                        {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }

    /**
     * whether animating when auto scroll at the last or first item, default is true
     *
     * @return
     */
    public boolean isBorderAnimation() {
        return isBorderAnimation;
    }

    /**
     * set whether animating when auto scroll at the last or first item, default is true
     *
     * @param isBorderAnimation
     */
    public void setBorderAnimation(boolean isBorderAnimation) {
        this.isBorderAnimation = isBorderAnimation;
    }

}
