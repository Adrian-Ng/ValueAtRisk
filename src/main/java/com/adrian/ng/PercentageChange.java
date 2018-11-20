package com.adrian.ng;

import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PercentageChange {

    public static ArrayList<Double> getArrayList(List<HistoricalQuote> historicalQuotes) {
        ArrayList<Double> percentageChange = new ArrayList<>();

        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();
        BigDecimal a = iterator.next().getClose();
        while (iterator.hasNext()){
            BigDecimal b = iterator.next().getClose();
            BigDecimal PriceDiff = a
                                    .subtract(b)
                                    .divide(a, RoundingMode.HALF_UP);
            percentageChange.add(PriceDiff.doubleValue());
            a = b;
        }
        return percentageChange;
    }

    public static double[] getArray (List<HistoricalQuote> historicalQuotes){
        ArrayList<Double> percentageChange = getArrayList(historicalQuotes);
        int size = percentageChange.size();
        double[] doubles = new double[size];

        for(int i = 0; i < size; i++)
            doubles[i] = percentageChange.get(i);

        return doubles;
    }


}
