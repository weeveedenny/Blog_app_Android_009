package com.decagon.android.sq009.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.adapters.CommentAdapter
import com.decagon.android.sq009.model.CommentModel
import com.decagon.android.sq009.repository.Repository
import com.decagon.android.sq009.viewmodels.CommentViewModel
import com.decagon.android.sq009.viewmodels.CommentViewModelFactory
import kotlinx.android.synthetic.main.activity_comments.*



class CommentsActivity : AppCompatActivity() {

    lateinit var commentViewModel: CommentViewModel
    lateinit var progressDialog : ProgressDialog
    lateinit var commentRecyclerView: RecyclerView
    lateinit var commentRecyclerViewAdapter: CommentAdapter
    lateinit var commentViewModelFactory: CommentViewModelFactory
    private var commentList = mutableListOf<CommentModel>()


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_comments)

        //initialise the view model repository class
            val repository = Repository()
            commentViewModelFactory = CommentViewModelFactory(repository)
            commentViewModel = ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel::class.java)

        //build the progressbar Dialog
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Please hold ...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            getViews()
            viewModelToAdapter()
            observeComment()

    }

    // get the views and the input
        private fun getViews(){
            val postTitle = findViewById<TextView>(R.id.comments_cardView_textView)
            val postBody = findViewById<TextView>(R.id.comments_cardView_textViewbody)

            postTitle.text = intent.getStringExtra("title")
            postBody.text = intent.getStringExtra("body")
            val posId = intent.getIntExtra("position", 0)

            commentViewModel.getComment(posId)
            commentRecyclerView = findViewById(R.id.comment_recyclerView)

        }



    //observe the list of comments from the view model. if its not empty, attach the list to populate the adapter.
        private fun viewModelToAdapter(){
            val posId = intent.getIntExtra("position", 0)
            commentViewModel.comments.observe(this, Observer {
                if (it != null){
                    commentRecyclerView.setHasFixedSize(true)
                    commentRecyclerView.layoutManager = LinearLayoutManager(this)
                    commentRecyclerViewAdapter = CommentAdapter(it)
                    commentRecyclerViewAdapter.addAllComment(getCommentsList(it,posId))
                    commentRecyclerView.adapter = commentRecyclerViewAdapter
                    progressDialog.dismiss()
                }else{
                    Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show()
                }
            })
        }



    //onclick of the comment button, observe the new comment input
        private fun observeComment(){
                    val posId = intent.getIntExtra("position", 0)
                    comment_button.setOnClickListener{
                        val newComment = findViewById<EditText>(R.id.comment_editText).text.toString()
                        val comment_model =   CommentModel(0,0,"Weeveedenny", "dennisodalonu@gmail.com", newComment)

                        if (newComment.isNotEmpty()) {
                            commentViewModel.postComment(posId, comment_model)

                            commentViewModel.commentNew.observe(this, Observer {
                                Log.d("NewComment", "onCreate: $it")
                                if (it != null) {
                                    commentList.add(it)
                                    val posId = intent.getIntExtra("position", 0)
                                    commentRecyclerView.setHasFixedSize(true)
                                    commentRecyclerView.layoutManager = LinearLayoutManager(this)
                                    commentRecyclerViewAdapter.addNewComment(it)
                                    commentRecyclerView.adapter = commentRecyclerViewAdapter
                                    comment_editText.text.clear()
                        }
                    })

                } else {
                    Toast.makeText(this, "Please add a new comment", Toast.LENGTH_SHORT).show()
                }
            }
        }


    //get the list of comments and filter it by post id to display comment related to the post
    private fun getCommentsList (comments: List<CommentModel>, postId: Int): List<CommentModel> {
        return comments.filter{ it.postId == postId}
    }

}




