package com.example.fullscreenenvironmentmonitoring.data.gps

interface LocationProvider {
    suspend fun getPreferredLocationString(): String
}