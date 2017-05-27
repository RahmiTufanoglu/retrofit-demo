package com.rahmitufanoglu.retrofitdemo


import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rahmitufanoglu.retrofitdemo.model.GitHubRepo
import java.util.*



internal class ListAdapter(private var gitHubRepoList: MutableList<GitHubRepo>?) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val gitHubRepoList = gitHubRepoList!![position]

        Glide.with(holder.civAvatar.context)
                .load(gitHubRepoList.owner!!.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .dontAnimate()
                .into(holder.civAvatar)

        holder.tvPlaceholderOne.text = String.format(Locale.getDefault(), "%d", gitHubRepoList.id)
        holder.tvPlaceholderTwo.text = gitHubRepoList.name

        // start the Activity
        holder.view.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, SampleDetailActivity::class.java)
            intent.putExtra("image", gitHubRepoList.owner!!.avatarUrl)
            intent.putExtra("id", gitHubRepoList.id)
            intent.putExtra("name", gitHubRepoList.name)
            intent.putExtra("fullname", gitHubRepoList.fullName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return gitHubRepoList!!.size
    }

    fun setFilter(list: List<GitHubRepo>) {
        gitHubRepoList = ArrayList<GitHubRepo>()
        gitHubRepoList!!.addAll(list)
        notifyDataSetChanged()
    }

    // set the RecyclerView content
    internal class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val civAvatar: ImageView
        val tvPlaceholderOne: TextView
        val tvPlaceholderTwo: TextView

        init {
            civAvatar = view.findViewById(R.id.civ_avatar) as ImageView
            tvPlaceholderOne = view.findViewById(R.id.tv_placeholder_one) as TextView
            tvPlaceholderTwo = view.findViewById(R.id.tv_placeholder_two) as TextView
        }
    }

}
