package com.rahmitufanoglu.retrofitdemo


import android.app.ProgressDialog
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.rahmitufanoglu.retrofitdemo.api.GitHubService
import com.rahmitufanoglu.retrofitdemo.api.ServiceGenerator
import com.rahmitufanoglu.retrofitdemo.model.GitHubRepo
import com.rahmitufanoglu.retrofitdemo.util.NetworkUtil
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainFragment : Fragment(), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
	private var recyclerView: RecyclerView? = null

    private var mListAdapter: ListAdapter? = null
    private var mGitHubRepoList: MutableList<GitHubRepo>? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mAnimationDrawable: AnimationDrawable? = null
    private var mSearchView: SearchView? = null
    private val mDisposable: Disposable? = null
    private var mFragmentCommunicator: FragmentCommunicator? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        recyclerView = view.findViewById(R.id.recycler_view_fragment) as RecyclerView

        handleIntent(activity.intent)
        setHasOptionsMenu(true)

        swipeRefreshLayout!!.setColorSchemeResources(R.color.primary)
        swipeRefreshLayout!!.setOnRefreshListener(this)

        setAnimation()
        setProgressDialog()
        setRecyclerView()

        return view
    }

    fun setAnimation() {
        mAnimationDrawable = swipeRefreshLayout!!.background as AnimationDrawable
        mAnimationDrawable!!.setEnterFadeDuration(0)
        mAnimationDrawable!!.setExitFadeDuration(5000)
    }

    override fun onResume() {
        super.onResume()
        if (mAnimationDrawable != null && !mAnimationDrawable!!.isRunning)
            mAnimationDrawable!!.start()
    }

    override fun onPause() {
        super.onPause()
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning)
            mAnimationDrawable!!.stop()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_replace_fragment) {
            replaceFragment()
            //mFragmentCommunicator.respond("Test");
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment() {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val searchResultFragment = SearchResultFragment()
        fragmentTransaction.replace(R.id.fragment_container, searchResultFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun setProgressDialog() {
        mProgressDialog = ProgressDialog.show(activity, null, null, true, false)

        if (mProgressDialog!!.window != null) {
            mProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        //mProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mProgressDialog!!.setContentView(R.layout.progress_dialog)

        /*mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching repos...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();*/
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater!!.inflate(R.menu.menu_search, menu)

        val componentName = ComponentName(activity, MainActivity::class.java)
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchMenuItem = menu!!.findItem(R.id.action_search)
        mSearchView = searchMenuItem.actionView as SearchView
        //mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        // mSearchView.setIconifiedByDefault(false); -> to show the search icoon
        mSearchView!!.setIconifiedByDefault(true)
        mSearchView!!.setOnQueryTextListener(this)
        mSearchView!!.queryHint = getString(R.string.search)

        //ImageView closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        //ImageView searchButton = (ImageView) mSearchView.findViewById(R.id.search_mag_icon);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                swipeRefreshLayout!!.isEnabled = false
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                swipeRefreshLayout!!.isEnabled = true
                return true
            }
        })
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val searchText = newText.toLowerCase()
        val list = ArrayList<GitHubRepo>()

        for (gitHubRepoList in mGitHubRepoList!!) {
            val name = gitHubRepoList.name.toString().toLowerCase()
            if (name.contains(searchText)) {
                list.add(gitHubRepoList)
            }
        }

        mListAdapter!!.setFilter(list)
        return true
    }

    fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            mSearchView!!.setQuery(query, true)
        }
    }

    override fun onRefresh() {
        if (NetworkUtil.isNetworkConnected(activity)) {
            Toast.makeText(activity, "Repos...", Toast.LENGTH_SHORT).show()
            loadJson()
        } else {
            NetworkUtil.isNetworkConnected(activity)
            val connectionToast = Toast.makeText(activity, "No internet", Toast.LENGTH_SHORT)
            connectionToast.show()
            swipeRefreshLayout!!.isRefreshing = false
            mProgressDialog!!.hide()
        }
    }

    fun setRecyclerView() {
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(staggeredGridLayoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //recyclerView.setLayoutManager(gridLayoutManager);

        val layoutManager = LinearLayoutManager(activity)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.isDrawingCacheEnabled = true
        recyclerView!!.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView!!.setItemViewCacheSize(20)
        loadJson()
    }

    private fun loadJson() {
        val gitHubService = ServiceGenerator.createService(GitHubService::class.java)
        val call = gitHubService.getRepos(Constants2.USER)
        call.enqueue(object : Callback<List<GitHubRepo>> {
            override fun onResponse(call: Call<List<GitHubRepo>>, response: Response<List<GitHubRepo>>) {
                // response.isSuccesful() is true if the HTTP response status code is in the range of 200-299
                Log.d(TAG, "request success")
                if (response.isSuccessful) {
                    mGitHubRepoList = response.body() as MutableList<GitHubRepo>?
                    mListAdapter = ListAdapter(mGitHubRepoList)
                    recyclerView!!.adapter = mListAdapter
                    swipeRefreshLayout!!.isRefreshing = false
                    mProgressDialog!!.hide()
                } else {
                    val statusCode = response.code()
                    val toastStatus = Toast.makeText(activity, "Status code: " + statusCode, Toast.LENGTH_SHORT)
                    toastStatus.show()
                }
            }

            override fun onFailure(call: Call<List<GitHubRepo>>, t: Throwable) {
                // handle execution failures like no internet connectivity
                Log.e(TAG, "request failed")
                if (call.isCanceled) {
                    Log.e(TAG, "request was cancelled")
                } else {
                    Log.e(TAG, "request was cancelled")
                    val toastError = Toast.makeText(activity, "Error fetching data!", Toast.LENGTH_SHORT)
                    toastError.show()
                }
                swipeRefreshLayout!!.isRefreshing = false
                mProgressDialog!!.hide()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null && !mDisposable.isDisposed) {
            mDisposable.dispose()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            mFragmentCommunicator = context as FragmentCommunicator?
        } catch (cce: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnListSelectedListener")
        }
    }

    companion object {
        val TAG = "CallInstances"
    }

}
