package com.rahmitufanoglu.retrofitdemo


import android.R.attr.duration
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_detail.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*


class SampleDetailActivity : AppCompatActivity(), View.OnClickListener {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        fab_detail.setOnClickListener(this)

        setCollapsingToolbar()
        setToolbar()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun setCollapsingToolbar() {
        val intent = intent
        val collapsingImage = intent.getStringExtra("image")
        val id = intent.getIntExtra("id", 0)
        val collapsingHeader = intent.getStringExtra("name")
        val content = intent.getStringExtra("fullname")

        Glide.with(this)
                .load(collapsingImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .dontAnimate()
                .into(iv_collapsing!!)

        tv_id!!.text = String.format(Locale.getDefault(), "%d", id)
        collapsing_toolbar!!.title = collapsingHeader
        tv_activity_detail!!.text = content
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_activity_detail)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        /*when (view.id) {
            R.id.fab_detail -> showCustomToast()
        }*/
        showCustomToast()
    }

    fun showCustomToast() {
        // Java Version works
        /*val customToast = Toast(this)
        customToast.view = layoutInflater.inflate(R.layout.toast_custom, findViewById(R.id.toast_custom) as ViewGroup)
        customToast.setGravity(Gravity.CENTER or Gravity.BOTTOM, 0, 32)
        customToast.duration = Toast.LENGTH_SHORT
        customToast.show()*/

        val toast = Toast.makeText(this, "asfafdfsdf", duration)
        toast.show()
    }

}
