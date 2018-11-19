package com.adrian.ng;

import java.math.BigDecimal;

public class VolatilityEWMA extends VolatilityAbstract {

    private static BigDecimal lambda;
    private static BigDecimal adbmal;

    static {
        lambda = new BigDecimal(0.94);
        adbmal = new BigDecimal(0.06);
    }

    @Override
    public BigDecimal getVariance(BigDecimal[] xVector, BigDecimal[] yVector) {

        int elements = xVector.length;

        BigDecimal EWMA = xVector[elements - 1].multiply(yVector[elements - 1]);
        for (int i = 1; i < elements; i++) {

            BigDecimal xElement = xVector[elements - 1 - i];
            BigDecimal yElement = yVector[elements - 1 - i];

            BigDecimal multiplicand1 = lambda.multiply(EWMA);
            BigDecimal multiplicand2 = adbmal.multiply(xElement).multiply(yElement);

            EWMA = multiplicand1.add(multiplicand2);
        }
        return EWMA;

    }
}
