package com.example.marksapp.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.marksapp.R
import com.example.marksapp.data.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddEventActivity : AppCompatActivity(), SuggestSession.SuggestListener,
    CoroutineScope by MainScope() {

    private val RESULT_NUMBER_LIMIT = 5

    private var searchManager: SearchManager? = null
    private var suggestSession: SuggestSession? = null
    private var suggestResultView: RecyclerView? = null
    private var resultAdapter: SuggestAdapter? = null
    private var suggestResult: ArrayList<String>? = null

    private lateinit var btnAddEvent: FloatingActionButton
    private lateinit var etName: EditText

    private var viewModel: AddEventViewModel? = null

    private val CENTER = Point(55.75, 37.62)
    private val BOX_SIZE = 0.2
    private val BOUNDING_BOX = BoundingBox(
        Point(CENTER.latitude - BOX_SIZE, CENTER.longitude - BOX_SIZE),
        Point(CENTER.latitude + BOX_SIZE, CENTER.longitude + BOX_SIZE)
    )
    private val SEARCH_OPTIONS = SuggestOptions().setSuggestTypes(
        SuggestType.GEO.value or
                SuggestType.BIZ.value or
                SuggestType.TRANSIT.value
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        MapKitFactory.setApiKey("c6e96dca-7701-431c-8465-eeda635c1bd7")
        SearchFactory.initialize(this)
        setContentView(R.layout.activity_add_event)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        suggestSession = searchManager!!.createSuggestSession()
        val queryEdit = findViewById<View>(R.id.suggest_query) as EditText
        suggestResultView = findViewById<View>(R.id.suggest_result) as RecyclerView
        suggestResult = ArrayList()
        resultAdapter = SuggestAdapter()
        resultAdapter!!.mData = suggestResult!!
        suggestResultView!!.adapter = resultAdapter
        resultAdapter!!.onSuggestClickListener = (object : SuggestAdapter.OnSuggestClickListener {
            override fun onSuggestClick(suggest: String?) {
                queryEdit.setText(suggest)
                resultAdapter!!.mData = emptyList()
            }
        })
        queryEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                requestSuggest(editable.toString())
            }
        })

        viewModel = ViewModelProvider(this).get(AddEventViewModel::class.java)

        etName = findViewById(R.id.et_event_name)
        btnAddEvent = findViewById(R.id.btn_add_event)
        btnAddEvent.setOnClickListener {
            val address = queryEdit.text.toString()
            val name = etName.text.toString()
            val event = Event(name, address)
            launch {
                viewModel?.saveEvent(event)
            }
        }

        viewModel?.shouldCloseScreen?.observe(this) {
            finish()
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, AddEventActivity::class.java)
        }
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onResponse(suggest: List<SuggestItem>) {
        suggestResult?.clear()
        for (i in 0 until Math.min(RESULT_NUMBER_LIMIT, suggest.size)) {
            suggest[i].displayText?.let { suggestResult?.add(it) }
        }
        resultAdapter!!.notifyDataSetChanged()
        suggestResultView!!.visibility = View.VISIBLE
    }

    override fun onError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun requestSuggest(query: String) {
        suggestResultView!!.visibility = View.GONE
        suggestSession!!.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this)
    }
}