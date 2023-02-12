package com.example.polynomgui;

public class InflectionPoint extends SpecialPoint {

    public InflectionPoint(double xValue, double yValue) {
        super();
    }

    public boolean isRightLeftTurning() {
        return isRightLeftTurning;
    }

    public void setRightLeftTurning(final boolean rightLeftTurning) {
        isRightLeftTurning = rightLeftTurning;
    }

    public boolean isRightLeftTurning;
}
