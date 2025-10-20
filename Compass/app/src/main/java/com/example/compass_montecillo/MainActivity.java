package com.example.compass_montecillo;

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

import com.example.compass.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Float azimuth_angle;
    private SensorManager compassSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView tv_degrees;
    TextView tv_direction;
    ImageView iv_compass;
    private float current_degree = 0f;

    // For smooth rotation
    private static final int SMOOTHING_FACTOR = 5;
    private float[] smoothedValues = new float[SMOOTHING_FACTOR];
    private int smoothIndex = 0;

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

        // Initialize smoothing array
        for (int i = 0; i < SMOOTHING_FACTOR; i++) {
            smoothedValues[i] = 0f;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listeners when activity resumes
        if (accelerometer != null) {
            compassSensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if (magnetometer != null) {
            compassSensorManager.registerListener(this, magnetometer,
                    SensorManager.SENSOR_DELAY_GAME);
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
            accel_read = event.values.clone();
        }

        // Read magnetometer data
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetic_read = event.values.clone();
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

                // Apply smoothing for smoother rotation
                smoothedValues[smoothIndex] = degrees;
                smoothIndex = (smoothIndex + 1) % SMOOTHING_FACTOR;

                float smoothedDegrees = 0;
                for (float value : smoothedValues) {
                    smoothedDegrees += value;
                }
                smoothedDegrees /= SMOOTHING_FACTOR;

                int degreesInt = Math.round(smoothedDegrees);

                // Normalize to 0-360 range
                if (degreesInt < 0) {
                    degreesInt += 360;
                }

                // Get direction and offset from cardinal points
                DirectionInfo dirInfo = getDirectionWithOffset(degreesInt);

                // Update text displays
                tv_direction.setText(dirInfo.cardinalDirection);

                if (dirInfo.offset == 0) {
                    // At cardinal point (N, S, E, W)
                    tv_degrees.setText("0° " + dirInfo.fullDirectionName);
                } else {
                    // Between cardinal points
                    tv_degrees.setText(dirInfo.offset + "° " + dirInfo.fullDirectionName);
                }

                // Animate compass rotation with interpolator for smoother animation
                RotateAnimation rotate = new RotateAnimation(
                        current_degree,
                        -smoothedDegrees,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                rotate.setDuration(200);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new android.view.animation.LinearInterpolator());

                iv_compass.startAnimation(rotate);
                current_degree = -smoothedDegrees;
            }
        }
    }

    /**
     * Inner class to hold direction information
     */
    private class DirectionInfo {
        String cardinalDirection;  // N, NE, E, SE, S, SW, W, NW
        String fullDirectionName;  // North, Northeast, etc.
        int offset;                // Degrees from nearest cardinal point
    }

    /**
     * Converts degrees (0-360) to cardinal direction with offset
     * @param degrees The azimuth angle in degrees
     * @return DirectionInfo object containing direction and offset
     */
    private DirectionInfo getDirectionWithOffset(int degrees) {
        DirectionInfo info = new DirectionInfo();

        // Normalize degrees to 0-360
        degrees = degrees % 360;
        if (degrees < 0) {
            degrees += 360;
        }

        // Determine which cardinal/intercardinal direction we're closest to
        if (degrees >= 337.5 || degrees < 22.5) {
            // North (0°)
            info.cardinalDirection = "N";
            info.fullDirectionName = "North";
            if (degrees > 180) {
                info.offset = 360 - degrees;
            } else {
                info.offset = degrees;
            }
        } else if (degrees >= 22.5 && degrees < 67.5) {
            // Northeast (45°)
            info.cardinalDirection = "NE";
            info.fullDirectionName = "Northeast";
            info.offset = Math.abs(degrees - 45);
        } else if (degrees >= 67.5 && degrees < 112.5) {
            // East (90°)
            info.cardinalDirection = "E";
            info.fullDirectionName = "East";
            info.offset = Math.abs(degrees - 90);
        } else if (degrees >= 112.5 && degrees < 157.5) {
            // Southeast (135°)
            info.cardinalDirection = "SE";
            info.fullDirectionName = "Southeast";
            info.offset = Math.abs(degrees - 135);
        } else if (degrees >= 157.5 && degrees < 202.5) {
            // South (180°)
            info.cardinalDirection = "S";
            info.fullDirectionName = "South";
            info.offset = Math.abs(degrees - 180);
        } else if (degrees >= 202.5 && degrees < 247.5) {
            // Southwest (225°)
            info.cardinalDirection = "SW";
            info.fullDirectionName = "Southwest";
            info.offset = Math.abs(degrees - 225);
        } else if (degrees >= 247.5 && degrees < 292.5) {
            // West (270°)
            info.cardinalDirection = "W";
            info.fullDirectionName = "West";
            info.offset = Math.abs(degrees - 270);
        } else if (degrees >= 292.5 && degrees < 337.5) {
            // Northwest (315°)
            info.cardinalDirection = "NW";
            info.fullDirectionName = "Northwest";
            info.offset = Math.abs(degrees - 315);
        }

        return info;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this app but required by SensorEventListener interface
    }
}