package com.decagon.android.sq009.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.model.CommentModel


class CommentAdapter(private var recyclerViewCommentList: MutableList<CommentModel>):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.comment_item_textView_name)
        val email: TextView = view.findViewById(R.id.comment_item_textView_email)
        val bodyTextView: TextView = view.findViewById(R.id.comment_item_textView_body)
    }


    //create the viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }


    //get the size of items populating the recycler view
    override fun getItemCount(): Int {
        return recyclerViewCommentList.size
    }


    //bind the list with the viewHolders
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = recyclerViewCommentList[position].name
        holder.email.text = recyclerViewCommentList[position].email
        holder.bodyTextView.text = recyclerViewCommentList[position].body

    }


    //function to add the new comment to the recyclerViewCommentList
    fun addNewComment(comment : CommentModel){
        recyclerViewCommentList.add(0,comment)
        notifyItemChanged(0)
    }


    //add the list of comment to the comment list
    fun addAllComment(comments: List<CommentModel>){
        this.recyclerViewCommentList = comments as MutableList<CommentModel>
        notifyDataSetChanged()
    }

}