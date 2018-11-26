package com.adrian.ng;

import yahoofinance.Stock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;

public class MonteCarlo extends RiskMeasure {
    //private static Random epsilon = new Random();
    private static final int steps;
    private static final int paths;
    private static final double dt;

    static {
        steps = 24;
        paths = 1000000;
        dt = 1.0 / steps;
    }

    private String volatilityMeasure;

    public MonteCarlo(String volatilityMeasure) {
        this.volatilityMeasure = volatilityMeasure;
    }

    private double[] sampleCorrelatedVariables(double[][] choleskyDecomposition) {
        // Generate a vector of random variables, sampling from random Gaussian of mean 0 and sd 1
        Random epsilon = new Random();
        double[] correlatedRandomVariables = new double[countAsset];
        for (int i = 0; i < countAsset; i++)
            for (int j = 0; j < countAsset; j++)
                //multiply the Cholesky Decomposition by a random variable sampled from the standard gaussian
                correlatedRandomVariables[i] += choleskyDecomposition[i][j] * epsilon.nextGaussian();
        // our random variables are now correlated
        return correlatedRandomVariables;
    }

    private double[] randomWalk(double[] currentStockPrices, double[][] choleskyDecomposition) {
        double[] previousStockPrices = currentStockPrices;
        double[] predictedStockPrices = new double[countAsset];
        for (int i = 0; i < steps; i++) {
            double[] correlatedRandomVariables = sampleCorrelatedVariables(choleskyDecomposition);
            for (int j = 0; j < countAsset; j++)
                predictedStockPrices[j] = (correlatedRandomVariables[j] * previousStockPrices[j] * Math.sqrt(dt)) + previousStockPrices[j];
            previousStockPrices = predictedStockPrices;
        }
        return predictedStockPrices;
    }

    public double getVar() {
        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));
        //int size = getSize();
        ArrayList<Future<double[]>> list = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        double[] currentPrices = new double[countAsset];
        double[] stockDelta = new double[countAsset];

        double[][] matrixPcntChanges = new double[countAsset][size];
        try {
            for (int i = 0; i < countAsset; i++) {
                String sym = strSymbols[i];
                Stock stock = stockHashMap.get(sym);
                currentPrices[i] = stock.getQuote().getPreviousClose().doubleValue();
                stockDelta[i] = hashStockDeltas.get(sym);
                // get percentage changes of stock
                double[] percentageChanges = PercentageChange.getArray(stock.getHistory());
                matrixPcntChanges[i] = percentageChanges;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // get cholesky decomposition (square root of covariance matrix)
        VolatilityFactory volatilityFactory = new VolatilityFactory();
        VolatilityAbstract volatilty = volatilityFactory.getType(volatilityMeasure);
        double[][] choleskyMatrix = volatilty.getCholeskyDecomposition(matrixPcntChanges);

        // Callable Future - run random walks in parallel
        ArrayList<double[]> simulatedPrices = new ArrayList<>();
        for (int j = 0; j < paths; j++) {
            //System.out.println(j);
            Callable<double[]> callable = () -> {
                double[] rw = randomWalk(currentPrices, choleskyMatrix);
                return rw;
            };
            Future<double[]> future = executor.submit(callable);
            list.add(future);
        }
        // Get Futures when ready
        try {
            for (Future<double[]> fut : list) {
                // because Future.get() waits for task to get completed
                double[] randomWalk = fut.get();
                simulatedPrices.add(randomWalk);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            //shut down the executor service
            executor.shutdown();
        }

        ArrayList<Double> tomorrowPi = new ArrayList<>();
        for (double[] bd : simulatedPrices) {
            double sum = 0.0;
            for (int k = 0; k < countAsset; k++)
                sum += bd[k] * (stockDelta[k]);
            tomorrowPi.add(sum);
        }

        // Compute VaR
        Collections.sort(tomorrowPi);
        double index = (1 - Confidence) * paths;
        double VaR = currentPortfolio - tomorrowPi.get((int) index) * TimeHorizon;
        return VaR;
    }
}
