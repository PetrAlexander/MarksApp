package com.example.marksapp.presentation

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.example.marksapp.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

class MapActivity : Activity(), Session.SearchListener,
    CameraListener {

    private lateinit var mapView: MapView
    private lateinit var query: String
    private lateinit var name: String
    private var searchManager: SearchManager? = null
    private var searchSession: Session? = null

    private fun submitQuery(query: String) {
        searchSession = searchManager!!.submit(
            query,
            VisibleRegionUtils.toPolygon(mapView!!.map.visibleRegion),
            SearchOptions(),
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchFactory.initialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        query = intent.getStringExtra(QUERY).toString()
        name = intent.getStringExtra(NAME).toString()
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        mapView = findViewById(R.id.mapview)
        query?.let { submitQuery(it) }
        mapView.getMap().addCameraListener(this)
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects = mapView.map.mapObjects
        mapObjects.clear()

        for (searchResult in response.getCollection().getChildren()) {
            val resultLocation = searchResult.obj!!.geometry[0].point
            if (resultLocation != null) {
                val mark = mapObjects.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(this, R.drawable.search_result)
                )
                mark.userData = name

                val mapObjectTapListener =
                    MapObjectTapListener { mapObject, point ->
                        if (mapObject is PlacemarkMapObject) {
                            val name = mapObject.userData as String
                            val intent = EventActivity.newInstance(this, name)
                            startActivity(intent)
                        }
                        true
                    }

                mark.addTapListener(mapObjectTapListener)

                mapView.map.move(
                    CameraPosition(resultLocation, 17.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 1f),
                    null
                )
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCameraPositionChanged(
        p0: Map,
        p1: CameraPosition,
        p2: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            query.let { submitQuery(it) }
        }
    }

    companion object {
        private const val QUERY = "QUERY"
        private const val NAME = "NAME"
    }
}