package com.example.fullscreenenvironmentmonitoring.utils

import android.content.Context
import com.example.fullscreenenvironmentmonitoring.data.mqtt.MqttClientImpl
import com.example.fullscreenenvironmentmonitoring.data.repository.MqttClientRepositoryImpl
import com.example.fullscreenenvironmentmonitoring.ui.dashboard.DashboardViewModel
import com.example.fullscreenenvironmentmonitoring.ui.dashboard.DashboardViewModelFactory

object Injector {
    fun provideDashboardViewModelFactory(context : Context) : DashboardViewModelFactory{
        val mqttClient = MqttClientImpl(context)
        val mqttClientRepository = MqttClientRepositoryImpl(mqttClient)
        return DashboardViewModelFactory(mqttClientRepository)
    }
}