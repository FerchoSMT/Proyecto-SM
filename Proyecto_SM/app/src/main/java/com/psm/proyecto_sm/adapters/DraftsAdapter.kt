package com.psm.proyecto_sm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.models.Post
import kotlinx.android.synthetic.main.item_draft_2.view.*

class DraftsAdapter(val drafts: MutableList<Post>) : RecyclerView.Adapter<DraftsAdapter.DraftsViewHolder>() {

    private lateinit var mListener : onItemClickListener;

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftsViewHolder {
        return DraftsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_draft_2, parent, false),
            mListener
        );
    }

    override fun onBindViewHolder(holder: DraftsViewHolder, position: Int) {
        val Draft : Post = drafts.get(position)

        holder.render(Draft, position)
    }

    override fun getItemCount() = drafts.size

    fun addItem(draft: Post) {
        if (!this.isItemAdded(draft.id_post!!))
            drafts.add(draft)
        this.notifyDataSetChanged()
    }

    fun isItemAdded(idDraft: Long) : Boolean {
        var itemAdded = false
        for (index in 0..drafts.count() - 1) {
            if (drafts.get(index).id_post == idDraft) {
                itemAdded = true
                break
            }
        }

        return itemAdded
    }

    class DraftsViewHolder(val view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        fun render(draft: Post, position: Int) {
            view.tv_title_item_draft.setText(draft.title)
            view.tv_content_item_draft.setText(draft.content)
        }

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}