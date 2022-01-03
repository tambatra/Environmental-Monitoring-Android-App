package com.example.fullscreenenvironmentmonitoring.data.mqtt

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone1
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone2
import com.example.fullscreenenvironmentmonitoring.data.models.ParamZone3
import com.example.fullscreenenvironmentmonitoring.ui.settting.PreferenceProvider
import com.example.fullscreenenvironmentmonitoring.utils.*
import com.google.gson.Gson
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MqttClientImpl(context: Context) : PreferenceProvider(context), MqttClient {
    private val appContext = context.applicationContext
    private val ip = preference.getString(PREFERENCES.SERVER_IP, "192.168.43.223")
    private val clientID = preference.getString(PREFERENCES.CLIENT_ID, "AndroidMQTT")
    private val serverUri = "tcp://$ip:1883"

    private val mqttAndroidClient: MqttAndroidClient =
        MqttAndroidClient(appContext, serverUri, clientID)

    private var _isConnected = MutableLiveData("Not connected")
    private var _param1 = MutableLiveData(ParamZone1(0,456.5, 27.8, 64.9))
    private var _param2 = MutableLiveData(ParamZone2(0,643.5, 26.4, 75.2))
    private var _param3 = MutableLiveData(ParamZone3(0,423.4,134.0))

    private var _progressBar1 = MutableLiveData<Int>()
    private var _progressBar2 = MutableLiveData<Int>()
    private var _progressBar3 = MutableLiveData<Int>()

    private var _paramsZone1 = MutableLiveData<List<ParamZone1>>()
    private var _paramsZone2 = MutableLiveData<List<ParamZone2>>()
    private var _paramsZone3 = MutableLiveData<List<ParamZone3>>()

    init {
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                _isConnected.value = "Connected"
                if(reconnect)
                    SubTopics.values().forEach { topic->
                        subscribe(topic.value)
                    }
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val gson = Gson()
                val payload = message.toString()
                if(payload.isNotEmpty()) {
                    when (topic) {
                        SubTopics.PARAM1.value -> {
                            val paramZone1 =
                                gson.fromJson(payload, ParamZone1::class.java)
                            _param1.value = paramZone1
                            _progressBar1.value = progressValue(paramZone1.airQuality, true)
                            return
                        }
                        SubTopics.PARAM2.value -> {
                            val paramZone2 =
                                gson.fromJson(payload, ParamZone2::class.java)
                            _param2.value = paramZone2
                            _progressBar2.value = progressValue(paramZone2.airQuality, true)
                            return
                        }
                        SubTopics.PARAM3.value -> {
                            val paramZone3 =
                                gson.fromJson(payload, ParamZone3::class.java)
                            _param3.value = paramZone3
                            _progressBar3.value = progressValue(paramZone3.radiation, false)
                            return
                        }

                        SubTopics.PARAMS1.value -> {
                            _paramsZone1.value =
                                gson.fromJson(payload, Array<ParamZone1>::class.java)
                                    .toList()
                            return
                        }
                        SubTopics.PARAMS2.value -> {
                            _paramsZone2.value =
                                gson.fromJson(payload, Array<ParamZone2>::class.java)
                                    .toList()
                            return
                        }
                        SubTopics.PARAMS3.value -> {
                            _paramsZone3.value =
                                gson.fromJson(payload, Array<ParamZone3>::class.java)
                                    .toList()
                            return
                        }
                    }
                }
            }

            override fun connectionLost(cause: Throwable?) {
                _isConnected.value = "Connection lost"
            }
            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })
        connect()
    }

    private fun connect() {
        val mqttConnectOptions = MqttConnectOptions().apply{
            isAutomaticReconnect = MqttClientConstant.CONNECTION_RECONNECT
            isCleanSession = MqttClientConstant.CONNECTION_CLEAN_SESSION
            connectionTimeout = MqttClientConstant.CONNECTION_TIMEOUT
            keepAliveInterval = MqttClientConstant.CONNECTION_KEEP_ALIVE_INTERVAL
        }
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    _isConnected.value = "Connected"
                    SubTopics.values().forEach { topic->
                        subscribe(topic.value)
                    }
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    _isConnected.value = "Not connected"
                }

            })
        } catch (e: MqttException) {

        }
    }


    override val isClientConnected: LiveData<String>
        get() = _isConnected
    override val paramZone1: LiveData<ParamZone1>
        get() = _param1
    override val paramZone2: LiveData<ParamZone2>
        get() = _param2
    override val paramZone3: LiveData<ParamZone3>
        get() = _param3

    override val progressBar1: LiveData<Int>
        get() = _progressBar1
    override val progressBar2: LiveData<Int>
        get() = _progressBar2
    override val progressBar3: LiveData<Int>
        get() = _progressBar3

    override fun getParamsZone1(): LiveData<List<ParamZone1>> {
        return _paramsZone1
    }

    override fun getParamsZone2(): LiveData<List<ParamZone2>> {
        return _paramsZone2
    }

    override fun getParamsZone3(): LiveData<List<ParamZone3>> {
        return _paramsZone3
    }

    override fun publish(topic: String, msg: String, qos: Int) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            mqttAndroidClient.publish(topic, message.payload, qos, false)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun subscribe(topic: String, qos: Int=0) {
        try {
            mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {}
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {}
            })
        } catch (ex: MqttException) {
        }
    }

    override fun destroy() {
        mqttAndroidClient.unregisterResources()
        mqttAndroidClient.close()
    }

    private fun progressValue(v:Double, isAirQ:Boolean):Int{
        return if(isAirQ) progressBarAirMax - v.toInt()
        else progressBarRadMax - v.toInt()
    }
}