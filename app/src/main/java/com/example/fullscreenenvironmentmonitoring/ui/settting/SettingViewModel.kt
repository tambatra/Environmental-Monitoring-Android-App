package com.example.fullscreenenvironmentmonitoring.ui.settting

import android.content.SharedPreferences
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fullscreenenvironmentmonitoring.data.mqtt.PubTopics
import com.example.fullscreenenvironmentmonitoring.data.repository.MqttClientRepository
import com.example.fullscreenenvironmentmonitoring.utils.PREFERENCES
import com.google.gson.Gson

class SettingViewModel (private val mqttClientRepository: MqttClientRepository) : Observable, ViewModel() {

    @Bindable
    val serverIP = MutableLiveData<String>()
    @Bindable
    val clientID = MutableLiveData<String>()

    @Bindable
    val temperatureL1 = MutableLiveData<String>()
    @Bindable
    val temperatureL2 = MutableLiveData<String>()
    @Bindable
    val humidityL1 = MutableLiveData<String>()
    @Bindable
    val humidityL2 = MutableLiveData<String>()
    @Bindable
    val airQualityL1 = MutableLiveData<String>()
    @Bindable
    val airQualityL2 = MutableLiveData<String>()
    @Bindable
    val radiationL1 = MutableLiveData<String>()
    @Bindable
    val radiationL2 = MutableLiveData<String>()

    @Bindable
    val fromDate = MutableLiveData<String>()
    @Bindable
    val toDate = MutableLiveData<String>()
    @Bindable
    val groupBy = MutableLiveData<String>()


    fun initializeUi(sharedPref: SharedPreferences){
        serverIP.value = sharedPref.getString(PREFERENCES.SERVER_IP, "192.168.43.223")
        clientID.value = sharedPref.getString(PREFERENCES.CLIENT_ID, "AndroidMQTT")

        temperatureL1.value = sharedPref.getString(PREFERENCES.TEMPERATURE_LEVEL1, "18.0")
        temperatureL2.value = sharedPref.getString(PREFERENCES.TEMPERATURE_LEVEL2, "30.0")
        humidityL1.value = sharedPref.getString(PREFERENCES.HUMIDITY_LEVEL1, "45.0")
        humidityL2.value = sharedPref.getString(PREFERENCES.HUMIDITY_LEVEL2, "75.0")
        airQualityL1.value = sharedPref.getString(PREFERENCES.AIR_QUALITY_LEVEL1, "600.0")
        airQualityL2.value = sharedPref.getString(PREFERENCES.AIR_QUALITY_LEVEL2, "1000.0")
        radiationL1.value = sharedPref.getString(PREFERENCES.RADIATION_LEVEL1, "150.0")
        radiationL2.value = sharedPref.getString(PREFERENCES.RADIATION_LEVEL2, "400.0")

        fromDate.value = sharedPref.getString(PREFERENCES.FROM_DATE, "12/18/08/2020")
        toDate.value = sharedPref.getString(PREFERENCES.TO_DATE, "18/18/08/2020")
        groupBy.value = sharedPref.getString(PREFERENCES.GROUP_BY, "1m")
    }

    fun save(sharedPref: SharedPreferences):Boolean{
        val edit = sharedPref.edit()
        with(edit){
            putString(PREFERENCES.SERVER_IP, serverIP.value?.trim())
            putString(PREFERENCES.CLIENT_ID, clientID.value)

            putString(PREFERENCES.TEMPERATURE_LEVEL1, temperatureL1.value?.trim())
            putString(PREFERENCES.TEMPERATURE_LEVEL2, temperatureL2.value?.trim())
            putString(PREFERENCES.HUMIDITY_LEVEL1, humidityL1.value?.trim())
            putString(PREFERENCES.HUMIDITY_LEVEL2, humidityL2.value?.trim())
            putString(PREFERENCES.AIR_QUALITY_LEVEL1, airQualityL1.value?.trim())
            putString(PREFERENCES.AIR_QUALITY_LEVEL2, airQualityL2.value?.trim())
            putString(PREFERENCES.RADIATION_LEVEL1, radiationL1.value?.trim())
            putString(PREFERENCES.RADIATION_LEVEL2, radiationL2.value?.trim())

            putString(PREFERENCES.FROM_DATE, fromDate.value?.trim())
            putString(PREFERENCES.TO_DATE, toDate.value?.trim())
            putString(PREFERENCES.GROUP_BY, groupBy.value?.trim())
        }
        return edit.commit()
    }

    fun publishThresholds(sharedPref: SharedPreferences){
        val temp1 = sharedPref.getString(PREFERENCES.TEMPERATURE_LEVEL1, "18.0")
        val temp2 = sharedPref.getString(PREFERENCES.TEMPERATURE_LEVEL2, "30.0")
        val hum1 = sharedPref.getString(PREFERENCES.HUMIDITY_LEVEL1, "45.0")
        val hum2 = sharedPref.getString(PREFERENCES.HUMIDITY_LEVEL2, "75.0")
        val air1 = sharedPref.getString(PREFERENCES.AIR_QUALITY_LEVEL1, "600.0")
        val air2 = sharedPref.getString(PREFERENCES.AIR_QUALITY_LEVEL2, "1000.0")
        val rad1 = sharedPref.getString(PREFERENCES.RADIATION_LEVEL1, "150.0")
        val rad2 = sharedPref.getString(PREFERENCES.RADIATION_LEVEL2, "400.0")
        val payloadArray = listOf(temp1, temp2, hum1, hum2, air1, air2, rad1, rad2)
        val payload = Gson().toJson(payloadArray)
        if(mqttClientRepository.isClientConnected.value == "Connected") {
            mqttClientRepository.publish(PubTopics.THRESHOLD.value, payload)
        }
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}