package com.example.polynomgui;

public class InflectionPoint extends SpecialPoint {

    public InflectionPoint(double xValue, double yValue) {
        super();
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public boolean isRightLeftTurning() {
        return isRightLeftTurning;
    }

    public void setRightLeftTurning(final boolean rightLeftTurning) {
        isRightLeftTurning = rightLeftTurning;
    }

    public boolean isRightLeftTurning;
}
