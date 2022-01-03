package com.example.fullscreenenvironmentmonitoring.data.gps

import androidx.lifecycle.LiveData

interface LocationAddressProvider {
    val mLocation:LiveData<String>
    fun updateLocation()
}