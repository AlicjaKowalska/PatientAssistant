package com.example.patientassistant.Fragment

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.patientassistant.View.MapActivity
import com.example.patientassistant.databinding.FragmentMapHelperBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class MapHelperFragment : Fragment() {

    private lateinit var binding: FragmentMapHelperBinding
    private lateinit var mapActivity: MapActivity
    private val checkedListTypes = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapHelperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapActivity = activity as MapActivity
        binding.chipLocation.isChecked = true

        setupChipGroup()

        binding.buttonFind.setOnClickListener {
            mapActivity.mMap.clear()
            if (binding.chipLocation.isChecked) {
                markLocalPlacesMoveCamera()
            }
            if (binding.chipSelectedLocation.isChecked) {
                markPlacesMoveCameraSelectedLocation()
            }

            mapActivity.binding.btnFind.visibility = View.VISIBLE
            mapActivity.supportFragmentManager.popBackStack()

        }
    }

    private fun setupChipGroup() {
        setupChipListener(binding.chipHospital, MapActivity.HOSPITAL)
        setupChipListener(binding.chipPharmacy, MapActivity.PHARMACY)
        setupChipListener(binding.chipDoctor, MapActivity.DOCTOR)
        setupChipListener(binding.chipDentist, MapActivity.DENTIST)
        setupChipListener(binding.chipDrugStore, MapActivity.DRUGSTORE)
    }

    private fun setupChipListener(chip: Chip, placeType: String) {
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedListTypes.add(placeType)
            } else {
                checkedListTypes.remove(placeType)
            }
        }
    }

    private fun markLocalPlacesMoveCamera() {
        for (type in checkedListTypes) {
            Timber.i("requesting list of $type")
            val url = buildString(type, mapActivity.lat, mapActivity.lng)
            getPlaces(url)
        }
        val latLng = LatLng(mapActivity.lat, mapActivity.lng)
        mapActivity.mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun markPlacesMoveCameraSelectedLocation() {
        val latLng = searchLocation()
        if (latLng != null) {
            val lat = latLng.latitude
            val lng = latLng.longitude

            mapActivity.mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            for (type in checkedListTypes) {
                Timber.i("requesting list of $type")
                val url = buildString(type, lat, lng)
                getPlaces(url)
            }
        }
    }

    private fun searchLocation(): LatLng? {
        val location = binding.editTextLocation.text.toString().trim()
        var addressList: List<Address>? = null
        if (location == "") {
            Toast.makeText(mapActivity, "Please provide location", Toast.LENGTH_LONG).show()
            return null
        } else {
            val geocoder = Geocoder(mapActivity)
            try {
                //We are getting only first result of searched locations
                //this creates problems for cities with the same name
                //another issue will be created to address this
                addressList = geocoder.getFromLocationName(location, 1)
            } catch (e: IOException) {
                Timber.e("exception during location search", e)
            }
        }
        if (addressList != null) {
            val address = addressList[0]
            return LatLng(address.latitude, address.longitude)
        } else {
            Toast.makeText(mapActivity, "Provided location was not found", Toast.LENGTH_LONG).show()
        }
        return null
    }

    private fun buildString(placeType: String, lat: Double, lng: Double): String {
        val retString = buildString {
            append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
            append("location=$lat,$lng")
            append("&radius=${mapActivity.radius}")
            append("&types=$placeType")
            append("&key=${mapActivity.apiKey}")
        }
        return retString
    }

    private fun getPlaces(url: String) {
        val queue: RequestQueue = Volley.newRequestQueue(mapActivity)
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
                    mapActivity.mMap.addMarker(marker)
                }
            } catch (e: JSONException) {
                Timber.e("exception during parsing places API JSON result", e)
            }
        }, { error ->
            Timber.e("Exception during sending request to places API", error)
        })
        queue.add(request)
    }
}