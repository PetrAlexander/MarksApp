package com.example.marksapp.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.marksapp.R

class EventActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        tvName = findViewById(R.id.tv_name)
        val name = intent.getStringExtra("NAME")
        tvName.text = name
    }

    companion object {
        fun newInstance(context: Context, name: String): Intent {
            val intent = Intent(context, EventActivity::class.java)
            intent.putExtra("NAME", name)
            return intent
        }
    }
}