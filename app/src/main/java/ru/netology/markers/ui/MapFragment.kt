package ru.netology.markers.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.markers.R
import ru.netology.markers.databinding.FragmentMapBinding
import ru.netology.markers.utils.DoubleArg
import ru.netology.markers.viewModel.PointsViewModel

@AndroidEntryPoint
class MapFragment : Fragment() {

    private val viewModel: PointsViewModel by activityViewModels()

    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: LocationListener
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initLocationManager()
            }
        }
    private val inputListener = object : InputListener {
        override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
            viewModel.edit(
                ru.netology.markers.model.Point(
                    id = 0L,
                    name = "",
                    latitude = point.latitude,
                    longitude = point.longitude
                )
            )
            CreatePointFragment().show(
                childFragmentManager, CreatePointFragment.TAG
            )
        }

        override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
            println("long tapped")
        }
    }
    private var myLocation: Point? = null

    companion object {
        const val DEFAULT_ZOOM_LEVEL = 16.0f
        var Bundle.pointLatArg: Double? by DoubleArg
        var Bundle.pointLongArg: Double? by DoubleArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(
            inflater,
            container,
            false,
        )
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                initLocationManager()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        mapView = binding.mapview
        val map = mapView.mapWindow.map
        map.isZoomGesturesEnabled = true
        map.addInputListener(inputListener)

        val mapkit = MapKitFactory.getInstance()

        //user location
        val locationLayer = mapkit.createUserLocationLayer(mapView.mapWindow)
        locationLayer.isVisible = true

        val toPointsButton = binding.toPoints
        toPointsButton.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_mapFragment_to_pointsFragment,
                )
        }

        binding.apply {
            val latitude = arguments?.pointLatArg
            val longitude = arguments?.pointLongArg
            if (latitude != null && longitude !== null) {
                val position = Point(latitude, longitude)
                myLocation = position
                moveCamera(position, DEFAULT_ZOOM_LEVEL)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapObjects = mapView.mapWindow.map.mapObjects

        viewModel.data.observe(viewLifecycleOwner) {
            mapObjects.clear()
            val pinsCollection = mapObjects.addCollection()
            val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.point)
            it.points.forEach { point ->
                val placemark = pinsCollection.addPlacemark().apply {
                    geometry = Point(point.latitude, point.longitude)
                    setIcon(imageProvider)
                    setText(point.name)
                }
                placemark.isVisible = true
                placemark.opacity = 0.5f
                placemark.setIconStyle(
                    IconStyle().apply {
                        anchor = PointF(0.5f, 1.0f)
                        scale = 1f
                        zIndex = 1000f
                        visible = true
                    }
                )
                placemark.setTextStyle(
                    TextStyle().apply {
                        placement = TextStyle.Placement.BOTTOM
                    }
                )
                addTapListenerToPlacemark(placemark, point)
            }
            pinsCollection.isVisible = true
        }

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()

        locationManager.subscribeForLocationUpdates(
            0.0,
            1000,
            1.0,
            false,
            FilteringMode.OFF,
            Purpose.NAVIGATION,
            myLocationListener
        )
    }

    override fun onStop() {
        locationManager.unsubscribe(myLocationListener)
        myLocation = null
        mapView.onStop()
        super.onStop()
    }

    private fun moveCamera(point: Point, zoom: Float) {
        mapView.mapWindow.map.move(
            CameraPosition(point, zoom, 150.0f, 30.0f),
            Animation(Animation.Type.SMOOTH, 1F),
            null
        )
    }

    private fun initLocationManager() {
        locationManager = MapKitFactory.getInstance().createLocationManager()
        myLocationListener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                if (myLocation == null) {
                    moveCamera(location.position, DEFAULT_ZOOM_LEVEL)
                }
                myLocation = location.position
            }

            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                    println("Location is not available")
                }
            }
        }
    }

    private fun addTapListenerToPlacemark(
        placemark: PlacemarkMapObject,
        point: ru.netology.markers.model.Point
    ) {
        placemark.addTapListener { _, _ ->
            viewModel.edit(point)
            EditPointNameFragment().show(
                childFragmentManager, EditPointNameFragment.TAG
            )
            true
        }
    }
}