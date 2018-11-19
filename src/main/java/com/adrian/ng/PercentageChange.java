package com.adrian.ng;

import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PercentageChange {

    public static ArrayList<BigDecimal> getArrayList(List<HistoricalQuote> historicalQuotes) {
        ArrayList<BigDecimal> percentageChange = new ArrayList<>();

        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();
        BigDecimal a = iterator.next().getClose();
        while (iterator.hasNext()){
            BigDecimal b = iterator.next().getClose();
            BigDecimal PriceDiff = a
                                    .subtract(b)
                                    .divide(a, RoundingMode.HALF_UP);
            percentageChange.add(PriceDiff);
            a = b;
        }
        return percentageChange;
    }

    public static BigDecimal[] getArray (List<HistoricalQuote> historicalQuotes){
        ArrayList<BigDecimal> percentageChange = getArrayList(historicalQuotes);
        int size = percentageChange.size();
        return percentageChange.toArray(new BigDecimal[size]);
    }


}
