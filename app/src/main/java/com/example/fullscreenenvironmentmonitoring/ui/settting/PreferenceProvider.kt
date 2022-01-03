package com.example.fullscreenenvironmentmonitoring.ui.settting

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferenceProvider(context: Context) {
    private val appContex = context.applicationContext
    protected val preference:SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContex)
}