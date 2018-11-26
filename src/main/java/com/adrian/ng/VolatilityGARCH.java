package com.adrian.ng;

public class VolatilityGARCH extends VolatilityAbstract {
    private static double alpha, beta, omega;
    static{
        alpha = 0.08339;
        beta = 0.9101;
        omega = 0.000001346;
    }

    @Override
    public double getVariance(double[] xVector, double[] yVector) {
        int elements = xVector.length;
        double uSquared[] = new double [elements];
        double sigmaSquared = uSquared[0];
        for (int i = 1; i < uSquared.length;i++)
            sigmaSquared = omega + (alpha*uSquared[i]) + (beta*sigmaSquared);
        return sigmaSquared;
    }
}
