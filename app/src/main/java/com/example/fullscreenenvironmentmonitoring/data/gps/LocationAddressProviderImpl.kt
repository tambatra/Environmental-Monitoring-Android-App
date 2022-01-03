package com.example.fullscreenenvironmentmonitoring.data.gps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.IOException
import java.util.*

const val REQUEST_LOCATION_PERMISSION = 1
const val TAG = "MY_LOCATION_TAG"
class LocationAddressProviderImpl(
    private val activity:Activity,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationAddressProvider {

    var _location = MutableLiveData<String>()

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        if(!hasLocationPermission()) requestLocationPermission()
        else {
            Log.d(TAG, "getLocation: permissions granted")
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                Log.d(TAG, "getLocation: addOnSuccessListener")
                if (location != null) {
                    Log.d(TAG, "getLocation: location != null")
                    _location.postValue(getAddress(location))
                }
                else Log.d(TAG, "getLocation: location is null")
            }
        }
    }

    private fun getAddress(location: Location):String{
        val geocoder = Geocoder(activity.applicationContext, Locale.getDefault())
        var addresses:List<Address> = listOf()
        var result : String = ""
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        }catch (ex:IOException){
            result = "Service not available!"
        }
        if(addresses.isNotEmpty()){
            val address = addresses[0]
            val out = StringBuilder()
            for (i in 0 until address.maxAddressLineIndex){
                out.append(address.getAddressLine(i))
            }
            result = out.toString()
        }
        return result
    }

    override val mLocation: LiveData<String>
        get() = _location

    override fun updateLocation() {
        getLocation()
    }
}