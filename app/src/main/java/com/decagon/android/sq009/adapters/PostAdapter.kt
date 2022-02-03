package com.decagon.android.sq009.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.activities.PostActivity
import com.decagon.android.sq009.model.PostModel

class PostAdapter(private var recyclerViewPostList: MutableList<PostModel>, private val onClickListener: PostActivity):
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

      val titleTextView: TextView = view.findViewById(R.id.post_layout_title_textView)
      val bodyTextView:  TextView = view.findViewById(R.id.post_layout_body_textView)

      init { view.setOnClickListener(this) }

      //function to respond to click on the recycler item
      override fun onClick(v: View?) {
                  val position = adapterPosition
                  if (position != RecyclerView.NO_POSITION){
                  Log.i("Position","$adapterPosition")
                  onClickListener.onItemClick(recyclerViewPostList[adapterPosition], v!!)
              }
          }
        }

        fun filteredList(filteredPost: MutableList<PostModel>){
            recyclerViewPostList = filteredPost
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)
            return ViewHolder(view)
        }

    //Get the size of the list
        override fun getItemCount(): Int {
           return recyclerViewPostList.size
        }

    //Binds the view with the data
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.titleTextView.text = recyclerViewPostList[position].title
            holder.bodyTextView.text = recyclerViewPostList[position].body
        }


    //add the new post to the list of posts
        fun addNewPost(post : PostModel){
            recyclerViewPostList.add(0, post)
            notifyItemChanged(0)
        }

    //interface for the click on recyclerview
        interface OnItemClickListener{
            fun onItemClick(post: PostModel, view: View)
        }
}