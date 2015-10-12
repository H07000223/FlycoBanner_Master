#FlycoBanner-Master

A powerful android view looper library animing to simplify this high frequency function in daily development. Support for Android 2.2 and up. 

#Background

Image loop banner is high frequency used in most apps. So is the app of the company I work for. At first, this is only used in home page. Later with the app update, there are some simliar functions needed in other pages both image and text. I copy the same code here and there, finally I can not standing copying anymore. I explore on the github to find libs that meet my needs. Though I find some poewful libs, but the lib what I want should support view looping not only ImageView. And I do not want a lib having a ImageLoad Framework inside.
So I decide to code a libray with features below:

>
* Use Executors instead of Task
* Supports both normal banner and endless auto loop banner
* Supports inflate item view and logic operation such as loading image outside.
* Supports set source using data list with different entity
* Supports smartly pause scroll and continue scroll.
  * Touch down pause. Touch up continue.
  * Window of activty off pause.Window of activty on continue.
* Supports inner indictaors
* Supports page transformer (not often used in my daily work, just support this feature for others if needed)

##Demo
![](https://github.com/H07000223/FlycoBanner_Master/blob/master/preview_FlycoBanner.gif)

####[Here is a DemoApk download](http://fir.im/7qzm)

##Gradle

```groovy
dependencies{
    compile 'com.android.support:support-v4:22.2.1'
    compile 'com.nineoldandroids:library:2.4.0'
    compile 'com.flyco.banner:FlycoBanner_Lib:1.1.8@aar'
}
```

##Usage

###Extends BaseIndicaorBanner and Set Data Type

```Java
public class SimpleImageBanner extends BaseIndicaorBanner<BannerItem, SimpleImageBanner> {
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
        banner:bib_indicatorCornerRadius="3dp"
        banner:bib_indicatorGap="8dp"
        banner:bib_indicatorHeight="6dp"
        banner:bib_indicatorSelectColor="#ffffff"
        banner:bib_indicatorStyle="CORNER_RECTANGLE"
        banner:bib_indicatorUnselectColor="#88ffffff"
        banner:bib_indicatorWidth="6dp"/>
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
| bb_scale | float |scale of Banner height and width,height/screenWidth
| bb_isLoopEnable | boolean |is LoopViewPager or not,default true
| bb_delay | integer |delay before start scroll,unit second,default 5 seconds
| bb_period | integer |scroll period,unit second,default 5 seconds
| bb_isAutoScrollEnable | boolean |is auto scroll enable,default true
| bb_barColor | color |set bootom bar color,default transparent
| bb_isBarShowWhenLast | boolean |set bottom bar show or not when the position is the last 
| bb_barPaddingLeft | dimension |set bottom bar padding,unit dp
| bb_barPaddingTop | dimension |set bottom bar padding,unit dp
| bb_barPaddingRight | dimension |set bottom bar padding,unit dp
| bb_barPaddingBottom | dimension |set bottom bar padding,unit dp
| bb_textColor | color |set title text color,default "#ffffff" 
| bb_textSize | dimension |set title text size,unit sp,default 14sp
| bb_isTitleShow | boolean |set title show or not,default true 
| bb_isIndicatorShow | boolean |set indicator show or not,default true 
| bb_indicatorGravity |enum| Gravity.CENTER or RIGHT or LEFT.if gravity equals CENTER,title will not be supported,default CENTER

####BaseIndicaorBanner
|name|format|description|
|:---:|:---:|:---:|
| bib_indicatorStyle |enum|indicator style, `STYLE_DRAWABLE_RESOURCE` or  `STYLE_CORNER_RECTANGLE`
| bib_indicatorWidth | dimension |indicator width, unit dp, default 6dp
| bib_indicatorHeight | dimension |indicator height,unit dp,default 6dp
| bib_indicatorGap | dimension |gap between two indicators,unit dp,default 6dp
| bib_indicatorSelectColor | color |indicator select color for `STYLE_CORNER_RECTANGLE`, default `"#ffffff"`
| bib_indicatorUnselectColor | color |indicator unselect color for `STYLE_CORNER_RECTANGLE`, default `"#88ffffff" `
| bib_indicatorCornerRadius | dimension |indicator corner raduis for `STYLE_CORNER_RECTANGLE`,unit dp, default 3dp 
| bib_indicatorSelectRes | reference |indicator select drawable resource for `STYLE_DRAWABLE_RESOURCE`
| bib_indicatorUnselectRes | reference |indicator unselect drawable resource for `STYLE_DRAWABLE_RESOURCE`



##Thanks
*   [LoopingViewPager](https://github.com/imbryk/LoopingViewPager)
*   [NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)
