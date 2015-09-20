package com.flyco.banner.transform;

import android.view.View;
import android.support.v4.view.ViewPager;

import com.nineoldandroids.view.ViewHelper;

public class RotateUpTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(View page, float position) {
		final float width = page.getWidth();
		final float rotation = ROT_MOD * position;

		ViewHelper.setPivotX(page,width * 0.5f);
        ViewHelper.setPivotY(page,0f);
        ViewHelper.setTranslationX(page,0f);
        ViewHelper.setRotation(page,rotation);
	}
}
