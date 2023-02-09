package com.example.polynomgui;

import java.lang.Math;
import java.util.ArrayList;

public class Polynomial {
    double[] coefficients;

    ArrayList<TurningPoint> maxima = new ArrayList<>();

    ArrayList<TurningPoint> minima = new ArrayList<>();

    InflectionPoint[] inflectionPoints = new InflectionPoint[getDegree()-2];

    private static final String[] exponentCharacters = {"\u00B2" , "\u00B3", "\u2074"};

    Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
        if(this.getDegree() >= 2) {
            this.specialPoints();
        }
    }

    public String toString() {
        StringBuilder outputStringBuilder = new StringBuilder();
        outputStringBuilder.append("f(x) = ");
        int segments = 0;
        for(int i = coefficients.length - 1; i >= 0; i--){
            if (coefficients[i] != 0){
                if(i != coefficients.length-1 & segments != 0){
                    if(!(coefficients[i] < 0)){
                    outputStringBuilder.append(" + ");}
                    else {
                        outputStringBuilder.append(" - ");
                    }

                }
            if (i == 0){
                outputStringBuilder.append(positive(coefficients[i]));
            }
            else if (i == 1) {
                outputStringBuilder.append(positive(coefficients[i])).append("x");
            } else{
                outputStringBuilder.append(positive(coefficients[i])).append("x").append(exponentCharacters[i-2]);
            }
            segments += 1;
        }}
        return outputStringBuilder.toString();
    }

    public double calculateValue(Double xValue) {
        double sum = 0;
        for(int i = 0; i < coefficients.length; i++){
            sum += coefficients[i] * Math.pow(xValue, i);
        }
        return sum;
    }

    public int getDegree() {
        for(int i = coefficients.length-1 ; i > 0; i--){
            if(coefficients[i] != 0){
                return i;
            }
        }
        return 0;
    }

    public boolean isAxisSymmetrical() {
        for (int i = 0; i < coefficients.length; i++) {
            if(coefficients[i] != 0) {
                if(i % 2 != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isPointSymmetrical() {
        for (int i = 0; i < coefficients.length; i++) {
            if(coefficients[i] != 0) {
                if(i % 2 == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public double[] getDerivationCoefficients() {
        double[] derivationCoefficients = {0,0,0,0,0};
        for(int i = 1; i < coefficients.length; i++){
            derivationCoefficients[i-1] = coefficients[i] * i;
        }
        return derivationCoefficients;
    }

    public Polynomial getDerivation() {
        double[] derivationCoefficients = getDerivationCoefficients();
        return new Polynomial(derivationCoefficients);
    }

    public double[] findZeroPoints() {
        final int degree = this.getDegree();
        double[] zeroPoints = new double[degree];
        if (degree == 0) {
            return zeroPoints;
        } else if (degree == 1) {
            double zeroPoint = -(coefficients[0] / coefficients[1]);
            zeroPoints[0] = zeroPoint;
        } else if (degree == 2) {
            if (coefficients[2] == 1) {
                zeroPoints[0] = -(coefficients[1] / 2) + Math.sqrt(Math.pow(coefficients[1] / 2, 2) - coefficients[0]);
                zeroPoints[1] = -(coefficients[1] / 2) - Math.sqrt(Math.pow(coefficients[1] / 2, 2) - coefficients[0]);
            } else {
                zeroPoints[0] = -(coefficients[1] / coefficients[2] / 2) + Math.sqrt(Math.pow(coefficients[1] / coefficients[2] / 2, 2) - coefficients[0] / coefficients[2]);
                zeroPoints[1] = -(coefficients[1] / coefficients[2] / 2) - Math.sqrt(Math.pow(coefficients[1] / coefficients[2] / 2, 2) - coefficients[0] / coefficients[2]);
            }
            return zeroPoints;
        }
        return zeroPoints;
    }

    private double positive(double num) {
        if (num > 0){
            return num;
        }else {
            return num - num * 2;
        }
    }

    private void specialPoints() {
        Polynomial firstDerivation = this.getDerivation();
        Polynomial secondDerivation = firstDerivation.getDerivation();
        double[] firstDerivationZeroPoints = firstDerivation.findZeroPoints();
        //TurningPoints
        for (double firstDerivationZeroPoint : firstDerivationZeroPoints) {
            double secondDerivationValue = secondDerivation.calculateValue(firstDerivationZeroPoint);
            if (secondDerivationValue > 0) {
                minima.add(new TurningPoint(firstDerivationZeroPoint, this.calculateValue(firstDerivationZeroPoint)));
            }
            else if (secondDerivationValue < 0) {
                maxima.add(new TurningPoint(firstDerivationZeroPoint, this.calculateValue(firstDerivationZeroPoint)));
            }
        }
        //InflectionPoints

    }

}