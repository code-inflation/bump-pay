package me.sixhackathon.bumppay;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.Set;

import ch.uepaa.p2pkit.discovery.entity.Peer;

/**
 * Detectes if the device has been bumped
 */
public class BumpDetector implements SensorEventListener {

    // Minimum acceleration needed to count as a shake movement
    private static final int MIN_BUMP_ACCELERATION = 4;

    // Start time for the bump detection
    long startTime = 0;

    // Minimum delay between individual bumps in millis
    private static final long BUMP_DELAY = 200;

    // Arrays to store gravity and linear acceleration values
    private float[] mGravity = { 0.0f, 0.0f, 0.0f };
    private float[] mLinearAcceleration = { 0.0f, 0.0f, 0.0f };

    // Indexes for x, y, and z values
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    // listener that will be notified when the bump is detected
    private OnBumpListener bumpListener;

    public BumpDetector(OnBumpListener bumpListener) {
        this.bumpListener = bumpListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This method will be called when the accelerometer detects a change.

        setCurrentAcceleration(event);
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();

        // Check if the acceleration is greater than our minimum threshold
        if (maxLinearAcceleration > MIN_BUMP_ACCELERATION) {

            long now = System.currentTimeMillis();

            if (startTime == 0) {
                startTime = now;
            }

            if (now - startTime > BUMP_DELAY) {
                startTime = 0;
            } else {
                // valid bump, find out which peer
                Log.i(BumpDetector.class.toString(), "Detected valid bump");

                // notify the listener
                bumpListener.onBump();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void setCurrentAcceleration(SensorEvent event) {
        /*
         *  BEGIN SECTION from Android developer site. This code accounts for
         *  gravity using a high-pass filter
         */

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;

        // Gravity components of x, y, and z acceleration
        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];

        // Linear acceleration along the x, y, and z axes (gravity effects removed)
        mLinearAcceleration[X] = event.values[X] - mGravity[X];
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];

        /*
         *  END SECTION from Android developer site
         */
    }

    /**
     * Calculates the max linear acceleration
     * @return greatest linear acceleration in any direction
     */
    private float getMaxCurrentLinearAcceleration() {

        float maxLinearAcceleration = mLinearAcceleration[X];

        if (mLinearAcceleration[Y] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Y];
        }

        if (mLinearAcceleration[Z] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Z];
        }

        return maxLinearAcceleration;
    }
}
