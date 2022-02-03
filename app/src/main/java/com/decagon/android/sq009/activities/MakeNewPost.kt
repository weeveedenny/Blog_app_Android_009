package com.decagon.android.sq009.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.decagon.android.sq009.R
import kotlinx.android.synthetic.main.activity_make_newpost.*

class MakeNewPost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_newpost)

        //sends the new post to the main Activity
        post_button.setOnClickListener{
            val newPost = findViewById<EditText>(R.id.make_newpost_editText).text.toString()
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("newPost", newPost).flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

    }

}