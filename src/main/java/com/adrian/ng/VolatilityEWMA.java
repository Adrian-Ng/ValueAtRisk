package com.adrian.ng;

public class VolatilityEWMA extends VolatilityAbstract {

    private static double lambda;
    private static double adbmal;

    static {
        lambda = 0.94;
    }

    @Override
    public double getVariance(double[] xVector, double[] yVector) {
        int elements = xVector.length;
        double EWMA = xVector[elements - 1] * yVector[elements - 1];
        for (int i = 1; i < elements; i++)
            EWMA = (lambda * EWMA) + ((1-lambda) * xVector[elements -1 - i]* yVector[elements -1 - i]);
        return EWMA;
    }
}
