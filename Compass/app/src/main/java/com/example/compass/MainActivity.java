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
    TextView tv_direction;
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
        tv_direction = (TextView) findViewById(R.id.tv_direction);
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

                // Normalize to 0-360 range
                if (degreesInt < 0) {
                    degreesInt += 360;
                }

                // Update text display
                tv_degrees.setText(Integer.toString(degreesInt) + "°");

                // Get and display direction
                String direction = getDirection(degreesInt);
                tv_direction.setText(direction);

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

    /**
     * Converts degrees (0-360) to cardinal direction
     * @param degrees The azimuth angle in degrees
     * @return Cardinal direction string (N, NE, E, SE, S, SW, W, NW)
     */
    private String getDirection(int degrees) {
        // Normalize degrees to 0-360
        degrees = degrees % 360;
        if (degrees < 0) {
            degrees += 360;
        }

        // Define direction ranges (each direction covers 45 degrees)
        // N: 337.5-22.5, NE: 22.5-67.5, E: 67.5-112.5, etc.
        if (degrees >= 337.5 || degrees < 22.5) {
            return "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            return "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            return "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            return "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            return "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            return "NW";
        }

        return "N";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this app but required by SensorEventListener interface
    }
}