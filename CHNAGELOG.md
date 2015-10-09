#Change Log
Version 1.0.1 *(2015-09-18)*
----------------------------
* remove default scale for LoopViewPager

Version 1.1.0 *(2015-09-20)*
----------------------------
* use origin ViewPager instead of ViewPagerCompat, so PageTransformer needs API 3.0 and up.
if you want PageTransformer in lower version, use dependencies down V1.1.0,such as 'com.flyco.banner:FlycoBanner_Lib:1.0

Version 1.1.2 *(2015-09-22)*
----------------------------
* use addOnPageChangeListener instead of setOnPageChangeListener everywhere in library.

Version 1.1.4 *(2015-09-22)*
----------------------------
* fix bug:abstract method onCreateIndicator return null lead to crash.

Version 1.1.6 *(2015-09-22)*
----------------------------
* use list to manage LoopViewPager OnPageChangeListener.

Version 1.1.8 *(2015-10-09)*
----------------------------
* fix a small and update dependence FlycoDialog to the latest v1.1.0

