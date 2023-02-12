package com.example.polynomgui;

public abstract class SpecialPoint{
    double xValue;
    double yValue;
    public String toString(){
        return "(" + xValue + "|" + yValue + ")";
    }
    public double getyValue() {
        return yValue;
    }

    public void setyValue(final double yValue) {
        this.yValue = yValue;
    }

    public double getxValue() {
        return xValue;
    }

    public void setxValue(final double xValue) {
        this.xValue = xValue;
    }
}
