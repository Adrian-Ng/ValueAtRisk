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
}
