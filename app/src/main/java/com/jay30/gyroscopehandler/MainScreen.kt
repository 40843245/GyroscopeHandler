package com.jay30.gyroscopehandler

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.jay30.gyroscopehandler.util.enumclass.ResultEnum
import com.jay30.gyroscopehandler.util.gyroscope.GyroscopeHandler
import com.jay30.gyroscopehandler.util.gyroscope.MySensor

@Composable
fun MainScreen(){
    val TAG = "tag_MainScreen"

    Log.d(TAG,"MainScreen function was called.")

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    var result by remember { mutableStateOf("") }

    val gyroscopeHandler = GyroscopeHandler(
        sensorManager
    )

    val mySensor = MySensor(
        onSensorChanged = { event ->
            val sensor = event.sensor
            Log.d(TAG,"onSensorChanged callback of sensor -- ${sensor.name} is called.")
            val sensorInfo = StringBuilder()
            sensorInfo.append("sensor name: ${sensor.name}\n")
            sensorInfo.append("sensor type: ${sensor.type}\n")
            sensorInfo.append("used power: ${sensor.power} mA\n")
            sensorInfo.append("values:\n")
            val values = event.values
            for (i in values.indices) {
                sensorInfo.append(" values[$i] = ${Math.toDegrees(values[i].toDouble())}\n")
            }
            result = sensorInfo.toString()
            Log.d(TAG,"result:$result")
        },
        onAccuracyChanged = { sensor, accurancy ->
            Log.d(TAG,"onAccuracyChanged callback of sensor -- ${sensor.name} is called.")
            val sensorInfo = StringBuilder()
            sensorInfo.append("sensor name: ${sensor.name}\n")
            sensorInfo.append("sensor type: ${sensor.type}\n")
            sensorInfo.append("used power: ${sensor.power} mA\n")
            sensorInfo.append("accurancy:${accurancy}\n")
            result = sensorInfo.toString()
            Log.d(TAG,"result:$result")
        },
        sensorTypeId = Sensor.TYPE_GYROSCOPE,
        samplingPeriodUs = SensorManager.SENSOR_DELAY_NORMAL.toUInt(),
    )

    LaunchedEffect(Unit) {
        Log.d(TAG,"LaunchedEffect in MainScreen was called.")
        gyroscopeHandler.addListener(mySensor)
        // register the 0th sensor event of `gyroscopeHandler`
        val statusId = gyroscopeHandler.register(index = 0)
        Log.d(TAG,"gyroscopeHandler.register(index = 0) was finished called with returned value:${statusId}")
        if(statusId != ResultEnum.SUCCESS.statusId){
            // unregister the 0th sensor event of `gyroscopeHandler`
            gyroscopeHandler.unregister(index = 0)
            Log.d(TAG,"gyroscopeHandler.unregister(index = 0) was finished called.")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d(TAG,"onDispose in DisposableEffect in MainScreen was called.")
            // unregister the 0th sensor event of `gyroscopeHandler`
            gyroscopeHandler.unregister(index = 0)
            Log.d(TAG,"gyroscopeHandler.unregister(index = 0) was finished called.")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ){
            Text(result)
        }
    }
}