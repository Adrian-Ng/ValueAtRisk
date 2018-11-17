package com.adrian.ng;

import java.math.BigDecimal;

public abstract class Analytical implements RiskMeasure {





    @Override
    public BigDecimal getVar() {
        return new BigDecimal(0);
    }




}
