package com.adrian.ng;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.util.List;

public abstract class BackTest extends VaR {

    public static int countYears;
    public static int countMoments;
    public static int intervals;
    public static double[] historicalPortfolio = new double[size];
    public static double[] absoluteChangePortfolio = new double[size];
    static {
        countYears = 5;         //Get Five Years of Data for BackTest
        countMoments = 1000;    //Number of VaRs to Calculate
        intervals = 252;        //Number of Working Days in One Year
        // value portfolio at every moment
        try {
            for (int i = 0; i < size; i++) {
                double sum = 0.0;
                for (String sym : strSymbols) {
                    Stock stock = stockHashMap.get(sym);
                    List<HistoricalQuote> historicalQuotes = stock.getHistory();
                    sum += hashStockDeltas.get(sym) * historicalQuotes.get(i).getClose().doubleValue();
                }
                historicalPortfolio[i] = sum;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    abstract public int[] doCoverage();
}