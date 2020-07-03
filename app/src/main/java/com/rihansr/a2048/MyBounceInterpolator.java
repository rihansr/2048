package com.rihansr.a2048;

class MyBounceInterpolator implements android.view.animation.Interpolator {

    private double mAmplitude, mFrequency;

    MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) * Math.cos(mFrequency * time) + 1);
    }
}
