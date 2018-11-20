package com.adrian.ng;

import java.math.BigDecimal;

public class VolatilityGARCH extends VolatilityAbstract {

    @Override
    public double getVariance(double[] xVector, double[] yVector) {
        return 0;
    }

}
