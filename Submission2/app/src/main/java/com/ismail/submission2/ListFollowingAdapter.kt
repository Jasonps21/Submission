package com.ismail.submission2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListFollowingAdapter(private val listFollowing: ArrayList<Following>) : RecyclerView.Adapter<ListFollowingAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(following: Following) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(following.avatar)
                    .into(img_user_avatar)
                tv_user_name.text = following.name
            }

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFollowing.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollowing[position])

    }
}