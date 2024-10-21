package com.jay30.gyroscopehandler.util.gyroscope

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.jay30.gyroscopehandler.util.enumclass.ResultEnum
import com.jay30.gyroscopehandler.util.enumclass.StatusEnum

class GyroscopeHandler(
    val sensorManager: SensorManager,
) {
    val TAG = "tag_GyroscopeHandler"

    private var isEnabled: Boolean = false
    private val sensorEventListeners = mutableListOf<SensorEventListener>()
    private val sensorTypeIds = mutableListOf<Int>()
    private val samplingPeriods = mutableListOf<UInt>()


    fun addListener(mySensor: MySensor) {
        Log.d(TAG,"addListener method of GyroscopeHandler class was called.")

        val event = object : SensorEventListener {
            override fun onSensorChanged(event: android.hardware.SensorEvent?) {
                event?.let {
                    mySensor.onSensorChanged(event)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                sensor?.let {
                    mySensor.onAccuracyChanged(sensor, accuracy)
                }
            }
        }

        this.sensorEventListeners.add(event)
        this.sensorTypeIds.add(mySensor.sensorTypeId)
        this.samplingPeriods.add(mySensor.samplingPeriodUs)
    }

    fun addListeners(mySensors: List<MySensor>) {
        for (mySensor in mySensors) {
            this.addListener(mySensor)
        }
    }

    fun removeListener(index: Int): Int {
        if (index !in this.sensorEventListeners.indices) {
            return StatusEnum.NOT_FOUND.statusId
        }
        this.sensorEventListeners.removeAt(index)
        this.sensorTypeIds.removeAt(index)
        this.samplingPeriods.removeAt(index)
        return StatusEnum.FOUND.statusId
    }

    fun clearListeners(){
        this.sensorEventListeners.clear()
        this.sensorTypeIds.clear()
        this.samplingPeriods.clear()
    }

    fun register(index: Int): Int {
        Log.d(TAG,"register method of GyroscopeHandler class was called.")
        if (index !in this.sensorEventListeners.indices) {
            return StatusEnum.NOT_FOUND.statusId
        }

        Log.d(TAG,"ready to execute sensorManager.registerListener in register method of GyroscopeHandler class was called.")
        this.isEnabled = sensorManager.registerListener(
            this.sensorEventListeners[index],
            this.sensorManager.getDefaultSensor(this.sensorTypeIds[index]),
            this.samplingPeriods[index].toInt()
        )

        Log.d(TAG,"finish to execute sensorManager.registerListener in register method with returned value ${this.isEnabled}.")

        val retValue = if (this.isEnabled)
            ResultEnum.SUCCESS.statusId
        else
            ResultEnum.FAILURE.statusId


        return retValue
    }

    fun unregister(index: Int): Int {
        Log.d(TAG,"unregister method of GyroscopeHandler class was called.")
        if (index !in this.sensorEventListeners.indices) {
            return StatusEnum.NOT_FOUND.statusId
        }
        val event = this.sensorEventListeners[index]
        sensorManager.unregisterListener(event)
        return ResultEnum.SUCCESS.statusId
    }
}