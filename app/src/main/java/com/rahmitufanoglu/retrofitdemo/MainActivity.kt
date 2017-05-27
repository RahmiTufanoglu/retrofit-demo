package com.rahmitufanoglu.retrofitdemo


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), FragmentCommunicator {

    private var mBackPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        setFragment()
    }

    private fun setToolbar() {
        tv_toolbar_title!!.setText(R.string.placeholder)
        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
    }

    private fun setFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val mainFragment = MainFragment()
        fragmentTransaction.add(R.id.fragment_container, mainFragment)
        fragmentTransaction.commit()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val fragment = Fragment()

        if (fragment is MainFragment) {
            fragment.handleIntent(intent)
        }
    }

    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()
        val fragmentManager = supportFragmentManager

        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else if (currentTimeMillis - mBackPressedTime > 2000) {
            mBackPressedTime = currentTimeMillis
            val toast = Toast.makeText(this, "Press back again to reach the homescreen", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun respond(data: String) {
        val fragmentManager = supportFragmentManager
        val searchResultFragment = fragmentManager.findFragmentById(R.id.fragment_search_result) as SearchResultFragment
        searchResultFragment.test(data)
    }

}
