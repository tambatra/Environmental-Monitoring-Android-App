package com.example.fullscreenenvironmentmonitoring.data.repository

import androidx.lifecycle.LiveData
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone1
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone2
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone3
import com.example.fullscreenenvironmentmonitoring.data.mqtt.MqttClient

class MqttClientRepositoryImpl(
    private val mqttClient: MqttClient)
    : MqttClientRepository {

    override val isClientConnected: LiveData<String>
        get() = mqttClient.isClientConnected
    override val paramZone1: LiveData<ParamZone1>
        get() = mqttClient.paramZone1
    override val paramZone2: LiveData<ParamZone2>
        get() = mqttClient.paramZone2
    override val paramZone3: LiveData<ParamZone3>
        get() = mqttClient.paramZone3
    override val progressBar1: LiveData<Int>
        get() = mqttClient.progressBar1
    override val progressBar2: LiveData<Int>
        get() = mqttClient.progressBar2
    override val progressBar3: LiveData<Int>
        get() = mqttClient.progressBar3


    override fun publish(topic: String, msg: String, qos: Int) {
        mqttClient.publish(topic, msg, qos)
    }

    override fun destroyClient() {
       mqttClient.destroy()
    }

    override fun getParamsZone1(): LiveData<List<ParamZone1>> {
        return mqttClient.getParamsZone1()
    }

    override fun getParamsZone2(): LiveData<List<ParamZone2>> {
        return mqttClient.getParamsZone2()
    }

    override fun getParamsZone3(): LiveData<List<ParamZone3>> {
        return mqttClient.getParamsZone3()
    }
}