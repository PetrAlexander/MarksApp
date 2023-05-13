package com.example.marksapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.marksapp.R
import com.example.marksapp.data.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    lateinit var btnAddEvent: FloatingActionButton

    lateinit var rvEvents: RecyclerView

    lateinit var resultAdapter: MainAdapter

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        initViews()
        setupClickListeners()
        resultAdapter = MainAdapter()
        rvEvents.adapter = resultAdapter
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getEvents()?.observe(this) {
            resultAdapter.eventList = it
        }

        resultAdapter.onEventClickListener = (object : MainAdapter.OnEventClickListener {
            override fun onEventClick(event: Event) {
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                intent.putExtra("QUERY", event.address)
                intent.putExtra("NAME", event.name)
                startActivity(intent)
            }
        })
    }

    private fun initViews() {
        btnAddEvent = findViewById(R.id.btn_add_event)
        rvEvents = findViewById(R.id.rv_events)
    }

    private fun setupClickListeners() {
        btnAddEvent.setOnClickListener {
            val intent = AddEventActivity.newInstance(this)
            startActivity(intent)
        }
    }
}