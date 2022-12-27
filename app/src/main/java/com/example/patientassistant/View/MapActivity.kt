package com.example.patientassistant.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.patientassistant.Fragment.MapHelperFragment
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


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object PlaceTypes {
        const val PHARMACY = "pharmacy"
        const val HOSPITAL = "hospital"
        const val DOCTOR = "doctor"
        const val DRUGSTORE = "drugstore"
        const val DENTIST = "dentist"
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markerList: ArrayList<MarkerOptions>
    lateinit var binding: ActivityMapBinding
    lateinit var mMap: GoogleMap
    lateinit var apiKey: String
    //default radius is 3000 meters
    val radius = 3000
    //default location for Szczecin
    var lat = 53.42
    var lng = 14.55

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProgressBar()

        markerList = ArrayList()

        binding.bottomNavigation.selectedItemId = R.id.map
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    Log.i("Menu", "Home selected")
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.drugs -> {
                    Log.i("Menu", "Drugs selected")
                    return@setOnItemSelectedListener true
                }
                R.id.appointments -> {
                    Log.i("Menu", "Appointments selected")
                    startActivity(Intent(this, MedicalAppointmentsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.map -> {
                    Log.i("Menu", "Map selected")
                    startActivity(Intent(this, MapActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnFind.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.map_fragment, MapHelperFragment())
        fragmentTransaction.commit()
            fragmentTransaction.addToBackStack("places")
            binding.btnFind.visibility = View.GONE
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
        hideProgressBar()
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

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.mapFragment.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.mapFragment.visibility = View.VISIBLE
    }
}





