package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Float azimuth_angle;
    private SensorManager compassSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView tv_degrees;
    ImageView iv_compass;
    private float current_degree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize sensor manager and sensors
        compassSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = compassSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = compassSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Initialize UI components
        tv_degrees = (TextView) findViewById(R.id.tv_degrees);
        iv_compass = (ImageView) findViewById(R.id.iv_compass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listeners when activity resumes
        if (accelerometer != null) {
            compassSensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
        if (magnetometer != null) {
            compassSensorManager.registerListener(this, magnetometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners to save battery when activity pauses
        compassSensorManager.unregisterListener(this);
    }

    float[] accel_read;
    float[] magnetic_read;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Read accelerometer data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accel_read = event.values;
        }

        // Read magnetometer data
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetic_read = event.values;
        }

        // Process sensor data if both readings are available
        if (accel_read != null && magnetic_read != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            // Get rotation matrix from accelerometer and magnetometer data
            boolean successful_read = SensorManager.getRotationMatrix(R, I,
                    accel_read, magnetic_read);

            if (successful_read) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                // Get azimuth angle (angle to north)
                azimuth_angle = orientation[0];

                // Convert radians to degrees
                float degrees = ((azimuth_angle * 180f) / 3.14159f);
                int degreesInt = Math.round(degrees);

                // Update text display
                tv_degrees.setText(Integer.toString(degreesInt) +
                        (char) 0x00B0 + " to absolute north.");

                // Animate compass rotation
                RotateAnimation rotate = new RotateAnimation(
                        current_degree,
                        -degreesInt,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                rotate.setDuration(100);
                rotate.setFillAfter(true);

                iv_compass.startAnimation(rotate);
                current_degree = -degreesInt;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this app but required by SensorEventListener interface
    }
}