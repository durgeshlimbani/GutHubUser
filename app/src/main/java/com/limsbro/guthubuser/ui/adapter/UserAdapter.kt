package com.limsbro.guthubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.limsbro.guthubuser.R
import com.limsbro.guthubuser.databinding.UserItemBinding
import com.limsbro.guthubuser.helper.UserItemClickListener
import com.limsbro.guthubuser.model.UserModel

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
class UserAdapter(private val listener: UserItemClickListener?) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var users = mutableListOf<UserModel>()
    fun setUserList(movies: List<UserModel>) {
        this.users = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = users[position]

        holder.binding.userItemTxtUsername.text = user.login
        holder.binding.userItemTxtHtmlUrl.text = user.html_url
        Glide
            .with(holder.itemView.context)
            .load(user.avatar_url)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.binding.userItemImgAvatar)

        if (user.notes.isNullOrEmpty())
            holder.binding.userItemImgNotes.visibility = View.GONE
        else
            holder.binding.userItemImgNotes.visibility = View.VISIBLE

        holder.itemView.setOnClickListener{
            listener?.onItemClickListener(user)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun getLastItemId(): Int {
        return if (itemCount > 0) users[itemCount - 1].id else 0
    }

    class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}