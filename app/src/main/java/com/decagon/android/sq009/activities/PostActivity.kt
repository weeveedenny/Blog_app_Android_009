package com.decagon.android.sq009.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.adapters.PostAdapter
import com.decagon.android.sq009.model.PostModel
import com.decagon.android.sq009.repository.Repository
import com.decagon.android.sq009.viewmodels.PostViewModel
import com.decagon.android.sq009.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*
import kotlin.collections.ArrayList

class PostActivity : AppCompatActivity(), PostAdapter.OnItemClickListener {
    lateinit var progressDialog : ProgressDialog
    lateinit var viewModel: PostViewModel
    lateinit var postRecyclerView: RecyclerView
    lateinit var recyclerViewAdapter: PostAdapter
    lateinit var viewModelFactory: ViewModelFactory
    private val repository = Repository() // one

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        viewModelFactory = ViewModelFactory(repository) // three

        //initialise the viewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java) // four
        postRecyclerView = findViewById(R.id.postActivity_recyclerView)
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postRecyclerView.isNestedScrollingEnabled = false

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please hold ....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        observeViewModel()
        receiveIntent()
        listenToSearch()
        fab()

    }


    //open another activity for a new post
        private fun fab(){
            postActivity_FAB.setOnClickListener{
                val intent = Intent(this, MakeNewPost::class.java)
                startActivity(intent)
            }
        }

    //observe the list of post from the view model and attach to adapter
        private fun observeViewModel(){
            viewModel.post.observe(this, Observer {

                if (it != null) {
                    recyclerViewAdapter = PostAdapter(it, this)
                    postRecyclerView.adapter = recyclerViewAdapter
                    recyclerViewAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                } else {
                    progressDialog.dismiss()
                }
            })
        }



    //receive the intent passing the new post from the AddPostActivity
        private fun receiveIntent(){
            val receiver: Bundle? = intent.extras
            if (receiver != null){
                val newPost = receiver.getString("newPost")
                val postObject = newPost?.let { PostModel(1,0,"Recent Post", it) }

                if (postObject != null){
                    viewModel.addPost(postObject)
                }

                if (newPost!!.isNotEmpty() ) {
                    //observe the new postList and update changes to the adapter
                    viewModel.newPost.observe(this, Observer {
                        if (it != null) {

                            recyclerViewAdapter.addNewPost(it)
                            postRecyclerView.adapter = recyclerViewAdapter
                            recyclerViewAdapter.notifyDataSetChanged()
                        }
                    })
                }
            }
        }


    //Listen to SearchView for input values
            private fun listenToSearch(){
                    postActivity_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String): Boolean {
                            if (postActivity_searchView.isEmpty()){
                                postRecyclerView.adapter = recyclerViewAdapter
                            }
                            else{
                                filter(newText)
                                postActivity_searchView.setIconifiedByDefault(true)
                            }
                            return false
                        }
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }
                    } )
            }



                // Search post list by title and attach the filtered list to the adapter
                fun filter (text: String){

                    val filteredPost = ArrayList<PostModel>()
                    viewModel.post.observe(this, Observer { post ->
                        post.filterTo(filteredPost) {postModel->
                        postModel.title.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
                        }
                        recyclerViewAdapter.filteredList(filteredPost)
                    })
                }


    //On click of a button, pass the content on the post to the comments activity
                override fun onItemClick(post: PostModel, view: View) {
                    val intent = Intent(this, CommentsActivity::class.java)
                    intent.putExtra("userId", post.userId)
                    intent.putExtra("id", post.id)
                    intent.putExtra("title", post.title)
                    intent.putExtra("body", post.body)
                    intent.putExtra("position", post.id)
                    startActivity(intent)
                }


}
