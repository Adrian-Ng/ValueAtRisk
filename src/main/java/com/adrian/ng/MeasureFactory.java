package com.adrian.ng;

import yahoofinance.Stock;

import java.util.ArrayList;

public class MeasureFactory extends VaR {

    public RiskMeasure getMeasureType(String strMeasure){

        if (strMeasure == null)
            return null;

        if (strMeasure.equals("Historical Simulation"))
            return new HistoricalSimulation();

        return null;
    }

}
