package com.example.fullscreenenvironmentmonitoring.ui.dashboard

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone1
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone2
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone3
import com.example.fullscreenenvironmentmonitoring.data.repository.MqttClientRepository
import com.google.android.material.snackbar.Snackbar

class DashboardViewModel(private val mqttClientRepository: MqttClientRepository) : ViewModel() {
    var isConnected = mqttClientRepository.isClientConnected
    var paramZone1 = mqttClientRepository.paramZone1
    var paramZone2 = mqttClientRepository.paramZone2
    var paramZone3 = mqttClientRepository.paramZone3

    var progressBar1 = mqttClientRepository.progressBar1
    var progressBar2 = mqttClientRepository.progressBar2
    var progressBar3 = mqttClientRepository.progressBar3

    var isPublished = MutableLiveData(true)

    fun getParamsZone1():LiveData<List<ParamZone1>>{
        return mqttClientRepository.getParamsZone1()
    }
    fun getParamsZone2():LiveData<List<ParamZone2>>{
        return mqttClientRepository.getParamsZone2()
    }
    fun getParamsZone3():LiveData<List<ParamZone3>>{
        return mqttClientRepository.getParamsZone3()
    }

    fun publish(topic: String, msg:String, qos: Int = 0){
        if(mqttClientRepository.isClientConnected.value == "Connected") {
            mqttClientRepository.publish(topic, msg, qos)
            isPublished.value = true
        }
        else{
            isPublished.value = false
        }
    }

    fun destroyClient(){
        mqttClientRepository.destroyClient()
    }
}