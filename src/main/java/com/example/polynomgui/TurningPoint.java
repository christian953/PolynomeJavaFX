package com.example.polynomgui;

public class TurningPoint extends SpecialPoint{
    public TurningPoint(final double xValue, final double yValue) {
        super();
        this.xValue = xValue;
        this.yValue = yValue;
    }


    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(final boolean global) {
        isGlobal = global;
    }

    boolean isGlobal;
}
