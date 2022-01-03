package com.example.fullscreenenvironmentmonitoring

import android.app.Application
import com.example.fullscreenenvironmentmonitoring.data.mqtt.MqttClient
import com.example.fullscreenenvironmentmonitoring.data.mqtt.MqttClientImpl
import com.example.fullscreenenvironmentmonitoring.data.repository.MqttClientRepository
import com.example.fullscreenenvironmentmonitoring.data.repository.MqttClientRepositoryImpl
import com.example.fullscreenenvironmentmonitoring.ui.dashboard.DashboardViewModelFactory
import com.example.fullscreenenvironmentmonitoring.ui.settting.SettingViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class DashboardApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@DashboardApplication))
        this.bind<MqttClient>() with this.singleton { MqttClientImpl(this.instance()) }
        this.bind<MqttClientRepository>() with this.singleton { MqttClientRepositoryImpl(this.instance()) }
        bind() from this.provider { SettingViewModelFactory(this.instance()) }
        bind() from this.provider { DashboardViewModelFactory(this.instance()) }
    }
}