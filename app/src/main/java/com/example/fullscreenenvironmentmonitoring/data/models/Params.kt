package com.example.fullscreenenvironmentmonitoring.data.models

import androidx.lifecycle.LiveData

data class Params (
    val getParamZone1: LiveData<ParamZone1>,
    val getParamZone2: LiveData<ParamZone2>,
    val getParamZone3: LiveData<ParamZone3>
)