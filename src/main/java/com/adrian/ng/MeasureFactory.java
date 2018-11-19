package com.adrian.ng;


public class MeasureFactory extends VaR {

    public RiskMeasure getMeasureType(String strMeasure){

        if (strMeasure == null)
            return null;

        if (strMeasure.equals("Historical Simulation"))
            return new HistoricalSimulation();

        if (strMeasure.equals("Analytical"))
            return new Analytical();

        return null;
    }

}
