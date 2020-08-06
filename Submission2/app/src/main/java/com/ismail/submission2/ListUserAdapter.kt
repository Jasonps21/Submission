package com.ismail.submission2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListUserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(img_user_avatar)
                tv_user_name.text = user.username
                tv_user_location.text = user.location
                tv_user_company.text = user.company

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }

            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListUserAdapter.ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])

        val data = listUser[position]
        holder.itemView.setOnClickListener {
            val user = User (
                data.avatar,
                data.name,
                data.username,
                data.company,
                data.location,
                data.repository,
                data.following,
                data.followers
            )
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USER, user)
            it.context.startActivity(intent)



        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }



}