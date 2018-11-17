package com.adrian.ng;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import java.util.ArrayList;
import java.util.List;

public class HistoricalSimulation implements RiskMeasure {


    @Override
    public double getVar() {



        try {
            ArrayList<Stock> stockArrayList = VaR.stockArrayList;
            for (Stock stock : stockArrayList) {
                System.out.printf("\t%s:\n", stock.getName());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        return 1.0;
    }


}
