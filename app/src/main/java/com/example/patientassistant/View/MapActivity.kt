package com.example.patientassistant.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.patientassistant.R
import com.example.patientassistant.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONException


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object PlaceTypes {
        const val PHARMACY = "pharmacy"
        const val HOSPITAL = "hospital"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markerList: ArrayList<MarkerOptions>
    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var apiKey: String
    //default radius is 3000 meters
    private val radius = 3000
    //default location for Szczecin
    private var lat = 53.42
    private var lng = 14.55

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        markerList = ArrayList()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.btnHospital.setOnClickListener {
                getLastLocationAndAnimateCamera()
                mMap.clear()
                markerList.clear()
                getPlaces(HOSPITAL)
            }
            binding.btnPharmacies.setOnClickListener {
                getLastLocationAndAnimateCamera()
                mMap.clear()
                markerList.clear()
                getPlaces(PHARMACY)
            }
        } else {
            binding.btnHospital.setOnClickListener {
                mMap.clear()
                markerList.clear()
                getPlaces(HOSPITAL)
            }
            binding.btnPharmacies.setOnClickListener {
                mMap.clear()
                markerList.clear()
                getPlaces(PHARMACY)
            }
        }

        applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        ).apply {
            apiKey = metaData.getString("com.google.android.geo.API_KEY").toString()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this@MapActivity)
            getLastLocationAndAnimateCamera()
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.isMyLocationEnabled = true
        } else {
            val position = LatLng(lat, lng)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
        }

        mMap.setOnMarkerClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Navigate to")
            builder.setMessage(it.title)

            builder.setPositiveButton("ok") {
                dialog, which ->
                val gmmIntentUri = Uri.parse("google.navigation:q=${it.position.latitude},${it.position.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            builder.setNegativeButton("cancel") {
                dialog, which ->
                dialog.dismiss()
            }
            builder.show()
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocationAndAnimateCamera() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            lat = it.latitude
            lng = it.longitude
            val position = LatLng(lat, lng)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(position))
        }
    }

    private fun buildString(placeType: String): String {
        val retString = buildString {
            append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
            append("location=$lat,$lng")
            append("&radius=$radius")
            append("&types=$placeType")
            append("&key=$apiKey")
        }
        return retString
    }

    private fun getPlaces(placeType: String) {
        val url = buildString(placeType)
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val resultsArray = response.getJSONArray("results")
                repeat(resultsArray.length()) {
                    val place = resultsArray.getJSONObject(it)
                    val geometry = place.getJSONObject("geometry")
                    val location = geometry.getJSONObject("location")
                    val lat = location.getDouble("lat")
                    val lon = location.getDouble("lng")
                    val name = place.getString("name")
                    val position = LatLng(lat, lon)
                    val marker = MarkerOptions().position(position).title(name)
                    mMap.addMarker(marker)
                    markerList.add(marker)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })
        queue.add(request)
    }
}





