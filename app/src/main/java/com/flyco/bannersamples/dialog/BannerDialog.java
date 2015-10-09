package com.flyco.bannersamples.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.bannersamples.R;
import com.flyco.bannersamples.banner.SimpleImageBanner;
import com.flyco.bannersamples.utils.DataProvider;
import com.flyco.bannersamples.utils.T;
import com.flyco.bannersamples.utils.ViewFindUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

public class BannerDialog extends BottomBaseDialog {
    private SimpleImageBanner sib;
    private Class<? extends ViewPager.PageTransformer> transformerClass;

    public BannerDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        View inflate = View.inflate(context, R.layout.dialog_banner, null);
        sib = ViewFindUtils.find(inflate, R.id.sib);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        sib();
    }

    public BannerDialog transformerClass(Class<? extends ViewPager.PageTransformer> transformerClass) {
        this.transformerClass = transformerClass;
        return this;
    }

    private void sib() {
        sib
                .setIndicatorGap(8)                                 //set gap btween two indicators
                .setSelectAnimClass(ZoomInEnter.class)              //set indicator select anim
                .setBarColor(Color.parseColor("#88000000"))         //set bootom bar color
                .setSource(DataProvider.getList())                  //data source list
                .setTransformerClass(transformerClass)              //set page transformer
                .startScroll();                                     //start scroll,the last method to call

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }
}
