package com.example.fullscreenenvironmentmonitoring.data.mqtt

import androidx.lifecycle.LiveData
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone1
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone2
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone3

interface MqttClient {
    val isClientConnected: LiveData<String>
    val paramZone1: LiveData<ParamZone1>
    val paramZone2: LiveData<ParamZone2>
    val paramZone3: LiveData<ParamZone3>
    val progressBar1: LiveData<Int>
    val progressBar2: LiveData<Int>
    val progressBar3: LiveData<Int>
    fun publish(topic: String, msg: String, qos: Int = 0)
    fun destroy()

    fun getParamsZone1(): LiveData<List<ParamZone1>>

    fun getParamsZone2(): LiveData<List<ParamZone2>>

    fun getParamsZone3(): LiveData<List<ParamZone3>>
}