package com.psm.proyecto_sm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.utils.ImageController
import com.psm.proyecto_sm.models.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter(val posts: MutableList<Post>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false),
            mListener
        );
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val Post : Post = posts.get(position)

        holder.render(Post, position)
    }

    override fun getItemCount() = posts.size

    fun addItem(post: Post) {
        if (!this.isItemAdded(post.id_post!!)) {
            posts.add(post)
            posts.sortByDescending {
                it.posted_date
            }
            this.notifyDataSetChanged()
        }
    }

    fun isItemAdded(idPost: Long) : Boolean {
        var itemAdded = false
        for (index in 0..posts.count() - 1) {
            if (posts.get(index).id_post == idPost) {
                itemAdded = true
                break
            }
        }

        return itemAdded
    }

    fun clearItems() {
        posts.clear()
        this.notifyDataSetChanged()
    }

    class PostsViewHolder(val view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        fun render(post: Post, position: Int) {
            val profilePicBmp = ImageController.getImageBitmap(post.img_user)
            if (profilePicBmp != null) {
                view.iv_pfp_itempost.setImageBitmap(profilePicBmp)
            }
            view.txtTitle_Post.setText(post.title)
            view.txtContent_Post.setText(post.content)
            if (post.imageA == null && post.imageB == null) {
                view.iv_img1_itempost.visibility = View.GONE
                view.iv_img2_itempost.visibility = View.GONE
            }
            else if (post.imageA != null && post.imageB == null) {
                view.iv_img1_itempost.setImageBitmap(ImageController.getImageBitmap(post.imageA))
                view.iv_img2_itempost.visibility = View.GONE
            }
            else if (post.imageA != null && post.imageB != null){
                view.iv_img1_itempost.setImageBitmap(ImageController.getImageBitmap(post.imageA))
                view.iv_img2_itempost.setImageBitmap(ImageController.getImageBitmap(post.imageB))
            }
        }

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}