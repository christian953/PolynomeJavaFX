package com.example.polynomgui;

public class TurningPoint extends SpecialPoint{
    public TurningPoint(double xValue,double yValue) {
    this.xValue = xValue;
    this.yValue = yValue;
    }

    public String toString(){
        return "(" + xValue + "|" + yValue + ")";
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    boolean isGlobal;
}
