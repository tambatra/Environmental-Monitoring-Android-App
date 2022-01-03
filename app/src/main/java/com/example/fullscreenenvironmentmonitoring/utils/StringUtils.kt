package com.example.fullscreenenvironmentmonitoring.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import java.io.IOException
import java.util.*

object StringUtils {

    @JvmStatic
    fun round(v:Double):Double{
        return "%.1f".format(v).toDouble()
    }

    fun toString(d:Double, b:Boolean): String{
        val unit = if(b) "°C" else "%"
        return round(d).toString() + unit
    }

    fun maxToString(d:Double, b: Boolean): String{
        return if(b) "Maximum pollution level = ${round(d)} ppm at 12h30"
        else "Maximum radiation level = ${round(d)} cpm at 15h30"
    }

    fun toString(d:Double): String{
        return d.toInt().toString()
    }
}
