package com.example.fullscreenenvironmentmonitoring.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fullscreenenvironmentmonitoring.data.repository.MqttClientRepository

class DashboardViewModelFactory(
    private val mqttClientRepository: MqttClientRepository)
    : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(mqttClientRepository) as T
    }
}