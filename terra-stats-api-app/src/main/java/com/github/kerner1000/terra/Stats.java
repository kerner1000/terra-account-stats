package com.github.kerner1000.terra;

import com.github.kerner1000.terra.transactions.Transactions;

import java.util.ArrayList;

public record Stats(WeightedMeanSwapMaps weightedMeanSwapMaps) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        var prices = new ArrayList<>(weightedMeanSwapMaps.getBuyMap().keySet());
        var amounts = new ArrayList<>(weightedMeanSwapMaps.getBuyMap().values());
        int max = prices.stream().mapToInt(i -> i).max().orElse(0);
        Hans hans = new Hans(prices, amounts, max);
        sb.append("Buy histogram:");
        sb.append("\n");
        sb.append(hans.printHistrogram(3));
        sb.append("Average prices:\n");
        sb.append(Transactions.getWeightedMean(weightedMeanSwapMaps));
        var result = sb.toString();
        return result;
    }
}
