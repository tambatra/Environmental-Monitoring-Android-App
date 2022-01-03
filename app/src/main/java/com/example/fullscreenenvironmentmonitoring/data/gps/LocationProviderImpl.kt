package com.example.fullscreenenvironmentmonitoring.data.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.fullscreenenvironmentmonitoring.ui.settting.PreferenceProvider
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.IOException
import java.util.*

class LocationProviderImpl(context: Context,
private val fusedLocationProviderClient: FusedLocationProviderClient)
    : PreferenceProvider(context), LocationProvider {
    private val appContext = context.applicationContext

    override suspend fun getPreferredLocationString(): String {
        return try {
            val deviceLocation = getLastDeviceLocation()
            Log.d(TAG, "{${deviceLocation?.latitude} ,${deviceLocation?.longitude}}")
            getAddress(deviceLocation!!)
        } catch (ex:Exception){
            "no Location found"
        }
    }



    @SuppressLint("MissingPermission")
    private suspend fun getLastDeviceLocation(): Location? {
        return (if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw Exception()).await()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getAddress(location: Location):String{
        val geocoder = Geocoder(appContext, Locale.getDefault())
        var addresses:List<Address> = listOf()
        var result : String = ""
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        }catch (ex: IOException){
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
        Log.d(TAG, result)
        return "${addresses[0].countryCode}, ${addresses[0].countryName}, ${addresses[0].featureName}, ${addresses[0].locality}, ${addresses[0].postalCode}, ${addresses[0].url}"
    }
}