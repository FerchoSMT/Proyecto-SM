package com.psm.proyecto_sm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.models.ImageController
import com.psm.proyecto_sm.models.Reply
import com.psm.proyecto_sm.models.UserLogged
import kotlinx.android.synthetic.main.item_comment_2.view.*

class RepliesAdapter(val replies: MutableList<Reply>) : RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
        fun onSettingsClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepliesViewHolder {
        return RepliesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment_2, parent, false),
            mListener
        );
    }

    override fun onBindViewHolder(holder: RepliesViewHolder, position: Int) {
        val Reply : Reply = replies.get(position)

        holder.render(Reply, position)
    }

    override fun getItemCount() = replies.size

    fun addItem(reply: Reply) {
        if (!this.isItemAdded(reply.id_reply!!))
            replies.add(reply)
        this.notifyDataSetChanged()
    }

    fun isItemAdded(idReply: Long) : Boolean {
        var itemAdded = false
        for (index in 0..replies.count() - 1) {
            if (replies.get(index).id_reply == idReply) {
                itemAdded = true
                break
            }
        }

        return itemAdded
    }

    fun deleteItem(idReply: Long) {
        for (index in 0..replies.count() - 1) {
            if (replies.get(index).id_reply == idReply) {
                replies.removeAt(index)
                break
            }
        }
        this.notifyDataSetChanged()
    }

    class RepliesViewHolder(val view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        fun render(reply: Reply, position: Int) {
            val profilePicBmp = ImageController.getImageBitmap(reply.img_user)
            if (profilePicBmp != null) {
                view.iv_photo_comment.setImageBitmap(profilePicBmp)
            }
            view.tv_name_comment.setText(reply.name_user)
            view.tv_content_comment.setText(reply.content)
            if (reply.id_user != UserLogged.userId) {
                view.tv_settings_comment.visibility = View.GONE
            }
        }

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            view.tv_settings_comment.setOnClickListener {
                listener.onSettingsClick(it, adapterPosition)
            }
        }
    }

}