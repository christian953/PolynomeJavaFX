package com.example.polynomgui;

public abstract class SpecialPoint {
    double xValue;
    double yValue;

    public double getyValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public double getxValue() {
        return xValue;
    }

    public void setxValue(double xValue) {
        this.xValue = xValue;
    }
}
