package com.adrian.ng;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class VolatilityEW extends VolatilityAbstract {

    @Override
    public BigDecimal getVariance(BigDecimal[] xVector, BigDecimal[] yVector) {

        BigDecimal sum = BigDecimal.ZERO;
        int elements = xVector.length;

        for (int i = 0; i < elements; i++)
            sum = sum.add(xVector[i].multiply(yVector[i]));
        return sum.divide(new BigDecimal(elements - 1), ROUND_HALF_UP);
    }

    @Override
    public BigDecimal getVolatility(BigDecimal[] xVector, BigDecimal[] yVector) {
        //https://stackoverflow.com/a/19743026/10526321
        BigDecimal variance = getVariance(xVector, yVector);

        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(variance.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = variance.divide(x0,  ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal(2),  ROUND_HALF_UP);
        }
        return x1;
    }
}
