package com.rcdevelopment.phsensor;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LinearRegression {

    private double m;
    private double b;

    private ArrayList<Double> xValues;
    private ArrayList<Double> yValues;


    public LinearRegression(ArrayList<Double> xValues, ArrayList<Double> yValues) throws IllegalArgumentException {
        this.xValues = xValues;
        this.yValues = yValues;
        if(xValues.size() != yValues.size())
            throw new IllegalArgumentException("xValues and yValues must have the same size.");
        this.calcLinearRegression();
    }


    public double GetM() {
        return this.m;
    }


    public double GetB() {
        return this.b;
    }


    private void calcLinearRegression() {
        int n = xValues.size();

        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        double sumXSquared = 0.0;

        for(int i = 0; i < n; i++) {
            double xValue = xValues.get(i);
            double yValue = yValues.get(i);
            sumX += xValue;
            sumY += yValue;
            sumXY += xValue * yValue;
            sumXSquared += xValue * xValue;
        }

        // least squares formulas for m and b
        m = ((n * sumXY) - (sumX * sumY)) / ((n * sumXSquared) - (sumX * sumX)));

        b = (((sumY) - (m * sumX)) / n);

    }





}
