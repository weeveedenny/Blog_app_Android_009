package com.decagon.android.sq009.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq009.model.CommentModel
import com.decagon.android.sq009.repository.Repository

class CommentViewModel(private val repository: Repository) : ViewModel() {

   var comments: MutableLiveData<MutableList<CommentModel>> = repository.listOfComments

    private var _commentNew = MutableLiveData<CommentModel>()
    val commentNew : LiveData<CommentModel>
    get() = _commentNew

    //get the list of response and store in a variable
    fun getComment(position: Int) {
        comments = repository.getComments(position)
    }

    //listen for changes in the addNewComment function
    fun postComment(position: Int, comment: CommentModel){
        _commentNew = repository.addNewComment(position, comment)
    }



}