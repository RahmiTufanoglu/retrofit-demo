package com.rahmitufanoglu.retrofitdemo


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_slider.*


class SliderActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {

    private var mMyPreferenceManager: MyPreferenceManager? = null
    private var mLayouts: IntArray? = null
    private var mBackPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkFirstTimeLaunch()
        setContentView(R.layout.activity_slider)

        makeNotificationBarTransparent()

        mLayouts = intArrayOf(
                R.layout.slide_screen_one,
                R.layout.slide_screen_two,
                R.layout.slide_screen_three
        )

        addBottomDots(0)

        val viewPagerAdapter = ViewPagerAdapter()
        view_pager.adapter = viewPagerAdapter
        view_pager.addOnPageChangeListener(this)
    }

    private fun checkFirstTimeLaunch() {
        mMyPreferenceManager = MyPreferenceManager(this)
        if (!mMyPreferenceManager!!.isFirstTimeLaunch()) {
            launchHomeScreen()
            finish()
        }
    }

    private fun launchHomeScreen() {
        mMyPreferenceManager!!.setFirstTimeLaunch(false)
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun makeNotificationBarTransparent() {
        val window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(mLayouts!!.size)

        val colorActive = resources.getIntArray(R.array.array_dot_active)
        val colorInactive = resources.getIntArray(R.array.array_dot_inactive)

        layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dots[i]!!.text = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                dots[i]!!.text = Html.fromHtml("&#8226;")
            }
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorActive[currentPage])
            layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[currentPage]!!.setTextColor(colorInactive[currentPage])
        }
    }

    override fun onClick(view: View?) {
        if (view!!.id == R.id.btn_skip) {
            launchHomeScreen()
        } else if (view.id == R.id.btn_next) {
            // Checking for last page, if last page home screen will be launched
            val current = view_pager.currentItem + 1
            if (current < mLayouts!!.size) {
                // Move to next screen
                view_pager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //
    }

    override fun onPageSelected(position: Int) {
        addBottomDots(position)

        if (position == mLayouts!!.size - 1) {
            btn_next.text = getString(R.string.start)
            btn_skip.visibility = View.GONE
        } else {
            btn_next.text = getString(R.string.next)
            btn_skip.visibility = View.VISIBLE
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        //
    }

    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()

        if (view_pager.currentItem == 1) {
            view_pager.setCurrentItem(0, true)
        } else if (view_pager.currentItem == 2) {
            view_pager.setCurrentItem(1, true)
        } else if (currentTimeMillis - mBackPressedTime > 2000) {
            mBackPressedTime = currentTimeMillis
            val toast = Toast.makeText(this, "Press back again to reach the homescreen", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            super.onBackPressed()
        }
    }

    private inner class ViewPagerAdapter : PagerAdapter() {

        private var view: View? = null

        override fun instantiateItem(container: ViewGroup, position: Int) {
            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(mLayouts!![position], container, false)
            container.addView(view)
        }

        override fun getCount(): Int {
            return mLayouts!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            view = `object` as View
            container.removeView(view)
        }
    }

}
