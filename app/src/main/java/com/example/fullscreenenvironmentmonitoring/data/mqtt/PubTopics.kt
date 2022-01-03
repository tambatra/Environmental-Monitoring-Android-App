package com.example.fullscreenenvironmentmonitoring.data.mqtt

import com.example.fullscreenenvironmentmonitoring.utils.ANDROID_ID

enum class PubTopics(val value:String) {
    PARAMS1("android/$ANDROID_ID/params1"),
    PARAMS2("android/$ANDROID_ID/params2"),
    PARAMS3("android/$ANDROID_ID/params3"),
    THRESHOLD("android/$ANDROID_ID/paramThreshold")
}