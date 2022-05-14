package com.example.mireaproject.ui.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mireaproject.R;


public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;

    private Sensor acelSensor;
    private Sensor tempSensor;
    private Sensor lightSensor;

    private TextView tempValue;
    private TextView acelSensorValueX;
    private TextView acelSensorValueY;
    private TextView acelSensorValueZ;
    private TextView lightValue;


    public SensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        acelSensorValueX = view.findViewById(R.id.acelValueX);
        acelSensorValueY = view.findViewById(R.id.acelValueY);
        acelSensorValueZ = view.findViewById(R.id.acelValueZ);

        tempValue = view.findViewById(R.id.tempValue);

        lightValue = view.findViewById(R.id.lightValue);

        if (getActivity() != null){
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

            acelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acelSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, tempSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEventevent) {
        if (sensorEventevent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float valueAzimuth = sensorEventevent.values[0];
            float valuePitch = sensorEventevent.values[1];
            float valueRoll = sensorEventevent.values[2];
            acelSensorValueX.setText("Azimuth: " + valueAzimuth);
            acelSensorValueY.setText("Pitch: " + valuePitch);
            acelSensorValueZ.setText("Roll: " + valueRoll);
        }
        if(sensorEventevent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float external_temperature = sensorEventevent.values[0];
            tempValue.setText("External Temperature:\n " + String.valueOf(external_temperature));
        }
        if (tempSensor == null) {
            tempValue.setText("NOT_SUPPORTED_SENSOR");
        }
        if(sensorEventevent.sensor.getType() == Sensor.TYPE_LIGHT){
            String newValue = sensorEventevent.values[0] + "";
            lightValue.setText(newValue);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}