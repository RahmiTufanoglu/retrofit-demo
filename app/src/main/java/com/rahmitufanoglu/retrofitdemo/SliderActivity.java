package com.rahmitufanoglu.retrofitdemo;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SliderActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.layoutDots) LinearLayout dotsLayout;
    @BindView(R.id.btn_skip) Button btnSkip;
    @BindView(R.id.btn_next) Button btnNext;
    private PreferenceManager mPreferenceManager;
    private int[] mLayouts;
    private long mBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstTimeLaunch();
        setContentView(R.layout.activity_slider);
        ButterKnife.bind(this);

        makeNotificationBarTransparent();

        mLayouts = new int[]{
                R.layout.slide_screen_one,
                R.layout.slide_screen_two,
                R.layout.slide_screen_three};

        addBottomDots(0);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void checkFirstTimeLaunch() {
        mPreferenceManager = new PreferenceManager(this);
        if (!mPreferenceManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
    }

    private void launchHomeScreen() {
        mPreferenceManager.setFirstTimeLaunch(false);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void makeNotificationBarTransparent() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[mLayouts.length];

        int[] colorActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            } else {
                dots[i].setText(Html.fromHtml("&#8226;"));
            }
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorActive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorInactive[currentPage]);
        }
    }

    @OnClick({R.id.btn_skip, R.id.btn_next})
    public void setOnClick(View view) {
        if (view.getId() == R.id.btn_skip) {
            launchHomeScreen();
        } else if (view.getId() == R.id.btn_next) {
            // Checking for last page, if last page home screen will be launched
            int current = viewPager.getCurrentItem() + 1;
            if (current < mLayouts.length) {
                // Move to next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position);

        if (position == mLayouts.length - 1) {
            btnNext.setText(getString(R.string.start));
            btnSkip.setVisibility(View.GONE);
        } else {
            btnNext.setText(getString(R.string.next));
            btnSkip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //
    }

    @Override
    public void onBackPressed() {
        final long currentTimeMillis = System.currentTimeMillis();

        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0, true);
        } else if (viewPager.getCurrentItem() == 2) {
            viewPager.setCurrentItem(1, true);
        } else if (currentTimeMillis - mBackPressedTime > 2000) {
            mBackPressedTime = currentTimeMillis;
            Toast toast = Toast.makeText(this, "Press back again to reach the homescreen", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            super.onBackPressed();
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private View mView;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(mLayouts[position], container, false);
            container.addView(mView);

            return mView;
        }

        @Override
        public int getCount() {
            return mLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mView = (View) object;
            container.removeView(mView);
        }
    }
}
