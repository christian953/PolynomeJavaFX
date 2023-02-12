package com.example.polynomgui;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class Polynomial {
    final double[] coefficients;


    public double[] getCoefficients() {
        return coefficients;
    }

    ArrayList<TurningPoint> maxima = new ArrayList<>();

    ArrayList<TurningPoint> minima = new ArrayList<>();
    InflectionPoint[] inflectionPoints;

    private static final String[] exponentCharacters = {"\u00B2" , "\u00B3", "\u2074"};
    Polynomial(final double[] coefficients) {
        super();
        this.coefficients = coefficients;
        if (this.getDegree() >= 2) {
            this.findSpecialPoints();
            inflectionPoints = new InflectionPoint[getDegree() - 2];

        }
    }

    public boolean equals(Polynomial polynomial){
        return Arrays.equals(polynomial.getCoefficients(), this.coefficients);
    }

    public String toString() {
        final StringBuilder outputStringBuilder = new StringBuilder();
        outputStringBuilder.append("ƒ(x) = ");
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

    public TurningPoint[] getMaxima() {
        return maxima.toArray(new TurningPoint[0]);
    }

    public TurningPoint[] getMinima() {
        return minima.toArray(new TurningPoint[0]);
    }

    public InflectionPoint[] getInflectionPoints() {
        return inflectionPoints;
    }

    public double calculateValue(final Double xValue) {
        double sum = 0;
        for(int i = 0; i < coefficients.length; i++){
            sum += coefficients[i] * Math.pow(xValue, i);
        }
        return sum;
    }

    public int getDegree() {
        for(int i = this.coefficients.length-1 ; i > 0; i--){
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
        final double[] derivationCoefficients = {0,0,0,0,0};
        for(int i = 1; i < coefficients.length; i++){
            derivationCoefficients[i-1] = coefficients[i] * i;
        }
        return derivationCoefficients;
    }

    public Polynomial getDerivation() {
        final double[] derivationCoefficients = getDerivationCoefficients();
        return new Polynomial(derivationCoefficients);
    }

    public double[] findZeroPoints() {
        final int degree = this.getDegree();
        if (degree <= 2) {
            final double[] zeroPoints = new double[degree];
            if (degree == 0) {
                return zeroPoints;
            } else if (degree == 1) {
                final double zeroPoint = -(coefficients[0] / coefficients[1]);
                zeroPoints[0] = zeroPoint;
            } else {
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
        return new double[0];
    }

    private double positive(final double num) {
        if (num > 0){
            return num;
        }else {
            return num - num * 2;
        }
    }

    private void findSpecialPoints() {
        final Polynomial firstDerivation = this.getDerivation();
        final Polynomial secondDerivation = firstDerivation.getDerivation();
        final double[] firstDerivationZeroPoints = firstDerivation.findZeroPoints();
        //TurningPoints
        for (final double firstDerivationZeroPoint : firstDerivationZeroPoints) {
            final double secondDerivationValue = secondDerivation.calculateValue(firstDerivationZeroPoint);
            if (secondDerivationValue > 0) {
                minima.add(new TurningPoint(firstDerivationZeroPoint, this.calculateValue(firstDerivationZeroPoint)));
            }
            else if (secondDerivationValue < 0) {
                maxima.add(new TurningPoint(firstDerivationZeroPoint, this.calculateValue(firstDerivationZeroPoint)));
            }
        }
    }



}