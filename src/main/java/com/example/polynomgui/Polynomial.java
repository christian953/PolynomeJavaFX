package com.example.polynomgui;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class Polynomial {
    final double[] coefficients;

    public double[] getCoefficients() {
        return coefficients;
    }

    private final ArrayList<TurningPoint> maxima = new ArrayList<>();

    private final ArrayList<TurningPoint> minima = new ArrayList<>();
    private final ArrayList<InflectionPoint> inflectionPoints = new ArrayList<>();

    private ZeroPoint[] zeroPoints;

    private static final String[] exponentCharacters = {"\u00B2" , "\u00B3", "\u2074"};

    public ZeroPoint[] getZeroPoints() {
        return zeroPoints;
    }

    Polynomial(final double[] coefficients) {
        super();
        this.coefficients = coefficients;
            this.findSpecialPoints();
    }

    public boolean equals(Polynomial polynomial){
        return Arrays.equals(polynomial.getCoefficients(), this.coefficients);
    }

    public String toString() {
        final StringBuilder outputStringBuilder = new StringBuilder();
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

    public TurningPoint[] getMaxima() {
        return maxima.toArray(new TurningPoint[0]);
    }

    public TurningPoint[] getMinima() {
        return minima.toArray(new TurningPoint[0]);
    }

    public InflectionPoint[] getInflectionPoints() {
        return inflectionPoints.toArray(new InflectionPoint[0]);
    }

    public double calculateValue(final double xValue) {
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

    public ZeroPoint[] findZeroPoints() {
        final int degree = this.getDegree();
        if (degree <= 2) {
            final ZeroPoint[] zeroPointsFound = new ZeroPoint[degree];
            if (degree == 0) {
                return zeroPointsFound;
            } else if (degree == 1) {
                final ZeroPoint zeroPoint = new ZeroPoint(-(coefficients[0] / coefficients[1]));
                zeroPointsFound[0] = zeroPoint;
            } else {
                return findZeroPointsWithPQFormula(zeroPointsFound);
            }
            return zeroPointsFound;
        }
        return new ZeroPoint[0];
    }

    private ZeroPoint[] findZeroPointsWithPQFormula(ZeroPoint[] zeroPoints) {
        if (coefficients[2] == 1) {
            zeroPoints[0] = new ZeroPoint(-(coefficients[1] / 2) + Math.sqrt(Math.pow(coefficients[1] / 2, 2) - coefficients[0]));
            zeroPoints[1] = new ZeroPoint( -(coefficients[1] / 2) - Math.sqrt(Math.pow(coefficients[1] / 2, 2) - coefficients[0]));
        } else {
            zeroPoints[0] = new ZeroPoint(-(coefficients[1] / coefficients[2] / 2) + Math.sqrt(Math.pow(coefficients[1] / coefficients[2] / 2, 2) - coefficients[0] / coefficients[2]));
            zeroPoints[1] = new ZeroPoint(-(coefficients[1] / coefficients[2] / 2) - Math.sqrt(Math.pow(coefficients[1] / coefficients[2] / 2, 2) - coefficients[0] / coefficients[2]));
        }
        return zeroPoints;
    }

    private void findInflectionPoints(){
        Polynomial firstDerivation = this.getDerivation();
        Polynomial secondDerivation = firstDerivation.getDerivation();
        ZeroPoint[] secondDerivationZeroPoints = secondDerivation.getZeroPoints();
        for(ZeroPoint secondDerivationZeroPoint : secondDerivationZeroPoints){
            InflectionPoint foundInflectionPoint = new InflectionPoint(secondDerivationZeroPoint.getxValue(), this.calculateValue(secondDerivationZeroPoint.getxValue()));
            inflectionPoints.add(foundInflectionPoint);
        }
    }

    private double positive(final double num) {
        if (num > 0){
            return num;
        }else {
            return num - num * 2;
        }
    }

    private void findSpecialPoints() {
        this.zeroPoints = findZeroPoints();
        if(this.getDegree() >= 2){
            findTurningPoints();
        }
        if(this.getDegree() >= 2) {
            findInflectionPoints();
        }
    }

    private void findTurningPoints() {
        Polynomial firstDerivation = this.getDerivation();
        Polynomial secondDerivation = firstDerivation.getDerivation();
        ZeroPoint[] firstDerivationZeroPoints = firstDerivation.findZeroPoints();
        for (final ZeroPoint firstDerivationZeroPoint : firstDerivationZeroPoints) {
            final double secondDerivationValue = secondDerivation.calculateValue(firstDerivationZeroPoint.getxValue());
            if (secondDerivationValue > 0) {
                minima.add(new TurningPoint(firstDerivationZeroPoint.getxValue(), this.calculateValue(firstDerivationZeroPoint.getxValue())));
            }
            else if (secondDerivationValue < 0) {
                maxima.add(new TurningPoint(firstDerivationZeroPoint.getxValue(), this.calculateValue(firstDerivationZeroPoint.getxValue())));
            }
        }
    }


}