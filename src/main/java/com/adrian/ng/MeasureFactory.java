package com.adrian.ng;


public class MeasureFactory extends VaR {

    public RiskMeasure getMeasureType(String strMeasure){

        if (strMeasure == null)
            return null;

        if (strMeasure.equals("Historical Simulation"))
            return new HistoricalSimulation();

        if (strMeasure.equals("Analytical EW"))
            return new Analytical(strMeasure.split(" ")[1]);

        if (strMeasure.equals("Analytical EWMA"))
            return new Analytical(strMeasure.split(" ")[1]);

        if (strMeasure.equals("MonteCarlo EW"))
            return new MonteCarlo(strMeasure.split(" ")[1]);

        if (strMeasure.equals("MonteCarlo EWMA"))
            return new MonteCarlo(strMeasure.split(" ")[1]);

        return null;
    }

}
