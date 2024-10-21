package com.jay30.gyroscopehandler.util.gyroscope

import android.hardware.Sensor
import android.hardware.SensorEvent

class MySensor(
    val onSensorChanged: ((SensorEvent) -> Unit),
    val onAccuracyChanged: ((Sensor, Int) -> Unit),
    val sensorTypeId: Int,
    val samplingPeriodUs: UInt,
)