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

    // For smooth rotation - increased smoothing
    private static final int SMOOTHING_FACTOR = 10;
    private float[] smoothedValues = new float[SMOOTHING_FACTOR];
    private int smoothIndex = 0;
    private boolean isInitialized = false;

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
        isInitialized = false;
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

                // Normalize to 0-360 range
                if (degrees < 0) {
                    degrees += 360;
                }

                // Initialize smoothing array with first value
                if (!isInitialized) {
                    for (int i = 0; i < SMOOTHING_FACTOR; i++) {
                        smoothedValues[i] = degrees;
                    }
                    current_degree = -degrees;
                    isInitialized = true;
                }

                // Apply circular smoothing for angles
                smoothedValues[smoothIndex] = degrees;
                smoothIndex = (smoothIndex + 1) % SMOOTHING_FACTOR;

                // Calculate smoothed angle using circular mean
                float smoothedDegrees = calculateCircularMean(smoothedValues);

                int degreesInt = Math.round(smoothedDegrees);

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

                // Calculate rotation with shortest path
                float targetDegree = -smoothedDegrees;

                // Normalize current_degree to 0-360 range for comparison
                float normalizedCurrent = current_degree % 360;
                if (normalizedCurrent < 0) normalizedCurrent += 360;

                float normalizedTarget = targetDegree % 360;
                if (normalizedTarget < 0) normalizedTarget += 360;

                // Calculate the shortest angular distance
                float diff = normalizedTarget - normalizedCurrent;

                if (diff > 180) {
                    diff -= 360;
                } else if (diff < -180) {
                    diff += 360;
                }

                // Only animate if difference is significant (reduces jitter)
                if (Math.abs(diff) > 0.5f) {
                    float newDegree = current_degree + diff;

                    // Animate compass rotation
                    RotateAnimation rotate = new RotateAnimation(
                            current_degree,
                            newDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);

                    rotate.setDuration(100);
                    rotate.setFillAfter(true);
                    rotate.setInterpolator(new android.view.animation.LinearInterpolator());

                    iv_compass.startAnimation(rotate);
                    current_degree = newDegree;
                }
            }
        }
    }

    /**
     * Calculate circular mean for angles to handle 0/360 boundary properly
     */
    private float calculateCircularMean(float[] angles) {
        float sinSum = 0;
        float cosSum = 0;

        for (float angle : angles) {
            double radians = Math.toRadians(angle);
            sinSum += Math.sin(radians);
            cosSum += Math.cos(radians);
        }

        float meanRadians = (float) Math.atan2(sinSum / angles.length, cosSum / angles.length);
        float meanDegrees = (float) Math.toDegrees(meanRadians);

        if (meanDegrees < 0) {
            meanDegrees += 360;
        }

        return meanDegrees;
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

        if (degrees >= 0 && degrees <= 89) {
            if (degrees == 0){
                info.cardinalDirection = "N";
                info.fullDirectionName = "North";
                info.offset = 0;
            }else {
                // Northeast (45°)
                info.cardinalDirection = "NE";
                info.fullDirectionName = "Northeast";
                info.offset = degrees;
            }
        } else if (degrees >= 90 && degrees <= 179) {
            if (degrees == 90){
                info.cardinalDirection = "E";
                info.fullDirectionName = "East";
                info.offset = 0;
            }else{
                // Southeast
                info.cardinalDirection = "SE";
                info.fullDirectionName = "Southeast";
                info.offset = degrees - 90;
            }
        } else if (degrees >= 180 && degrees <= 269) {
            if (degrees == 180){
                info.cardinalDirection = "S";
                info.fullDirectionName = "South";
                info.offset = 0;
            }else{
                // Southwest
                info.cardinalDirection = "SW";
                info.fullDirectionName = "SouthWest";
                info.offset = degrees - 180;
            }
        } else if (degrees >= 270 && degrees <= 359) {
            if (degrees == 270) {
                info.cardinalDirection = "W";
                info.fullDirectionName = "West";
                info.offset = 0;
            } else {
                info.cardinalDirection = "NW";
                info.fullDirectionName = "NorthWest";
                info.offset = degrees - 270;
            }
        }

        return info;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this app but required by SensorEventListener interface
    }
}