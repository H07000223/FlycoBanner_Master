package com.flyco.bannersamples.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.banner.anim.select.RotateEnter;
import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.anim.unselect.NoAnimExist;
import com.flyco.banner.transform.ZoomOutSlideTransformer;
import com.flyco.bannersamples.R;
import com.flyco.bannersamples.banner.SimpleImageBanner;
import com.flyco.bannersamples.banner.SimpleTextBanner;
import com.flyco.bannersamples.dialog.BannerDialog;
import com.flyco.bannersamples.utils.DataProvider;
import com.flyco.bannersamples.utils.T;
import com.flyco.bannersamples.utils.ViewFindUtils;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalListDialog;

import java.util.ArrayList;

public class BannerHomeActivity extends Activity {
    private Context context = this;
    private View decorView;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        dm = getResources().getDisplayMetrics();

        sib_simple_usage();
        sib_the_most_comlex_usage();
        sib_res();
        sib_rectangle();
        sib_corner_rectangle();
        sib_indicator_right_with_text();
        sib_indicator_left_with_text();
        sib_anim();
        sib_anim2();
        stb();


        ViewFindUtils.find(decorView, R.id.tv_user_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserGuideActivity.class);
                intent.putExtra("isFromBannerHome", true);
                startActivity(intent);
            }
        });

        ViewFindUtils.find(decorView, R.id.tv_select_transformer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog(false);
            }
        });

        ViewFindUtils.find(decorView, R.id.tv_select_transformer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog(true);
            }
        });
    }

    private void sib_simple_usage() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_simple_usage);

        sib
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_the_most_comlex_usage() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_the_most_comlex_usage);
        sib
                /** methods in BaseIndicatorBanner */
//              .setIndicatorStyle(BaseIndicaorBanner.STYLE_CORNER_RECTANGLE)//set indicator style
//              .setIndicatorWidth(6)                               //set indicator width
//              .setIndicatorHeight(6)                              //set indicator height
//              .setIndicatorGap(8)                                 //set gap btween two indicators
//              .setIndicatorCornerRadius(3)                        //set indicator corner raduis
                .setSelectAnimClass(ZoomInEnter.class)              //set indicator select anim
                        /** methods in BaseBanner */
//              .setBarColor(Color.parseColor("#88000000"))         //set bootom bar color
//              .barPadding(5, 2, 5, 2)                             //set bottom bar padding
//              .setBarShowWhenLast(true)                           //set bottom bar show or not when the position is the last
//              .setTextColor(Color.parseColor("#ffffff"))          //set title text color
//              .setTextSize(12.5f)                                 //set title text size
//              .setTitleShow(true)                                 //set title show or not
//              .setIndicatorShow(true)                             //set indicator show or not
//              .setDelay(2)                                        //setDelay before start scroll
//              .setPeriod(10)                                      //scroll setPeriod
                .setSource(DataProvider.getList())                  //data source list
                .setTransformerClass(ZoomOutSlideTransformer.class) //set page transformer
                .startScroll();                                     //start scroll,the last method to call

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_res() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_res);

        sib
//                .setIndicatorStyle(SimpleImageBanner.STYLE_DRAWABLE_RESOURCE)
//                .setIndicatorSelectorRes(R.mipmap.banner_dot_unselect, R.mipmap.banner_dot_select)
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_rectangle() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_rectangle);
        sib
//                .setIndicatorCornerRadius(0)
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_corner_rectangle() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_corner_rectangle);
        sib
//                .setIndicatorWidth(10)
//                .setIndicatorHeight(4)
//                .setIndicatorCornerRadius(2)
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }


    private void sib_indicator_right_with_text() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_indicator_right_with_text);
        sib
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_indicator_left_with_text() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_indicator_left_with_text);
        sib
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_anim() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_anim);
        sib
                .setSelectAnimClass(ZoomInEnter.class)
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void sib_anim2() {
        SimpleImageBanner sib = ViewFindUtils.find(decorView, R.id.sib_anim2);
        sib
//                .setIndicatorWidth(10)
//                .setIndicatorHeight(4)
//                .setIndicatorCornerRadius(2)
                .setSelectAnimClass(RotateEnter.class)
                .setUnselectAnimClass(NoAnimExist.class)
                .setSource(DataProvider.getList())
                .startScroll();

        sib.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void stb() {
        SimpleTextBanner stb = ViewFindUtils.find(decorView, R.id.stb);

        ArrayList<String> titles = new ArrayList<>();
        for (String title : DataProvider.titles) {
            titles.add(title);
        }
        stb
                .setSource(titles)
                .startScroll();

        stb.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                T.showShort(context, "position--->" + position);
            }
        });
    }

    private void showSelectDialog(final boolean isSimpleImageBanner) {

        ArrayList<String> itemList = new ArrayList<>();
        for (Class<?> c : DataProvider.transformers) {
            itemList.add(c.getSimpleName());
        }
        final String[] contents = new String[itemList.size()];
        final NormalListDialog dialog = new NormalListDialog(context, itemList.toArray(contents));
        dialog.title("Select Transformer")//
                .cornerRadius(2)
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSimpleImageBanner) {
                    new BannerDialog(context)
                            .transformerClass(DataProvider.transformers[position])
                            .show();
                    return;
                }

                Intent intent = new Intent(context, UserGuideActivity.class);
                intent.putExtra("isFromBannerHome", true);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        final MaterialDialog dialog = new MaterialDialog(context);
        dialog.isTitleShow(false)//
                .content("Are you sure to exit app?")//
                .contentTextSize(18)
                .btnText("Exit", "Cancel")//
                .showAnim(new BounceTopEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                finish();
                System.exit(0);
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });
    }
}
