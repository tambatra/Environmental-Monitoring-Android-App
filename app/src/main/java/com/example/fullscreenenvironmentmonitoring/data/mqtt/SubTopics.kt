package com.example.fullscreenenvironmentmonitoring.data.mqtt

import com.example.fullscreenenvironmentmonitoring.utils.ANDROID_ID

enum class SubTopics(val value:String) {
    PARAM1("broker/param1"),
    PARAM2("broker/param2"),
    PARAM3("broker/param3"),
    PARAMS1("broker/$ANDROID_ID/params1"),
    PARAMS2("broker/$ANDROID_ID/params2"),
    PARAMS3("broker/$ANDROID_ID/params3"),
}