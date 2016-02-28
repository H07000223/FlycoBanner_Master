#FlycoBanner-Master

一个Android View轮播库,支持2.2+.

##Demo
![](https://github.com/H07000223/FlycoBanner_Master/blob/master/preview_FlycoBanner.gif)

####[Here is a DemoApk download](http://fir.im/7qzm)

##Gradle

```groovy
dependencies{
    compile 'com.android.support:support-v4:22.2.1'
    compile 'com.nineoldandroids:library:2.4.0'
    compile 'com.flyco.banner:FlycoBanner_Lib:2.0.2@aar'
}
```

##Usage

###Extends BaseIndicatorBanner and Set Data Type

```Java
public class SimpleImageBanner extends BaseIndicatorBanner<BannerItem, SimpleImageBanner> {
    private ColorDrawable colorDrawable;

    public SimpleImageBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        final BannerItem item = list.get(position);
        tv.setText(item.title);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(context, R.layout.adapter_simple_image, null);
        ImageView iv = ViewFindUtils.find(inflate, R.id.iv);

        final BannerItem item = list.get(position);
        int itemWidth = dm.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = item.imgUrl;

        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(context)
                    .load(imgUrl)
                    .override(itemWidth, itemHeight)
                    .centerCrop()
                    .placeholder(colorDrawable)
                    .into(iv);
        } else {
            iv.setImageDrawable(colorDrawable);
        }

        return inflate;
    }
}
```

###The Most Complex
in layout xml

``` xml
<com.flyco.bannersamples.banner.SimpleImageBanner
        android:id="@+id/sib_the_most_comlex_usage"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        banner:bb_barColor="#88000000"
        banner:bb_barPaddingBottom="5dp"
        banner:bb_barPaddingLeft="10dp"
        banner:bb_barPaddingRight="10dp"
        banner:bb_barPaddingTop="5dp"
        banner:bb_delay="2"
        banner:bb_indicatorGravity="RIGHT"
        banner:bb_isAutoScrollEnable="true"
        banner:bb_isBarShowWhenLast="true"
        banner:bb_isIndicatorShow="true"
        banner:bb_isLoopEnable="true"
        banner:bb_isTitleShow="true"
        banner:bb_period="10"
        banner:bb_scale="0.5625"
        banner:bb_textColor="#ffffff"
        banner:bb_textSize="13.5sp"
        banner:bb_indicatorCornerRadius="3dp"
        banner:bb_indicatorGap="8dp"
        banner:bb_indicatorHeight="6dp"
        banner:bb_indicatorSelectColor="#ffffff"
        banner:bb_indicatorStyle="CORNER_RECTANGLE"
        banner:bb_indicatorUnselectColor="#88ffffff"
        banner:bb_indicatorWidth="6dp"/>
```

int Java Code

``` Java
sib
    .setSelectAnimClass(ZoomInEnter.class)              //set indicator select anim
    .setSource(DataProvider.getList())                  //data source list
    .setTransformerClass(ZoomOutSlideTransformer.class) //set page transformer
    .startScroll();
```

###The Simplest
in layout xml

``` xml
<com.flyco.bannersamples.banner.SimpleImageBanner
       android:id="@+id/sib_simple_usage"
       android:layout_width="fill_parent"
       android:layout_height="wrap_content"
       banner:bb_scale="0.5625"/>
```

int Java Code

``` Java
sib
   .setSource(DataProvider.getList())
   .startScroll();
```

###Attributes

####BaseBanner

|name|format|description|
|:---:|:---:|:---:|
| bb_scale | float |Banner高宽比,范围0-1,默认0.5(for LoopViewPager)
| bb_isLoopEnable | boolean |是否支持循环,默认true
| bb_delay | integer |滚动延时,默认5秒
| bb_period | integer |滚动间隔,默认5秒
| bb_isAutoScrollEnable | 是否支持自动滚动,默认true
| bb_barColor | color |设置底部背景条颜色,默认透明
| bb_isBarShowWhenLast | boolean |设置当滚动到最后一个item时是否显示底部背景条
| bb_barPaddingLeft | dimension |设置底部背景条padding,单位dp
| bb_barPaddingTop | dimension |设置底部背景条padding,单位dp
| bb_barPaddingRight | dimension |设置底部背景条padding,单位dp
| bb_barPaddingBottom | dimension |设置底部背景条padding,单位dp
| bb_textColor | color |设置标题文字颜色,默认"#ffffff"
| bb_textSize | dimension |设置标题文字大小,单位sp,默认14sp
| bb_isTitleShow | boolean |设置是否显示标题,默认true
| bb_isIndicatorShow | boolean |设置是否显示显示器,默认true
| bb_indicatorGravity |enum| 设置显示器位置.默认Gravity.CENTER.Gravity.CENTER时文字不显示
| bb_indicatorStyle |设置显示器样式
| bb_indicatorWidth | dimension |设置显示器宽度,单位dp,默认6dp
| bb_indicatorHeight | dimension |设置显示器高度,单位dp,默认6dp
| bb_indicatorGap | dimension |设置显示器间距,单位dp,默认6dp
| bb_indicatorSelectColor | color |设置显示器选中颜色,默认"#ffffff"
| bb_indicatorUnselectColor | color |设置显示器未选中颜色,默认"#88ffffff"
| bb_indicatorCornerRadius | dimension |设置显示器圆角弧度,单位dp,默认3dp
| bb_indicatorSelectRes | reference |设置显示器选中drawable资源
| bb_indicatorUnselectRes | reference |设置显示器未选中drawable资源


##Thanks
*   [LoopingViewPager](https://github.com/imbryk/LoopingViewPager)
*   [NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)