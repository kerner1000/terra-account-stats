package com.github.kerner1000.terra;

import com.github.kerner1000.terra.transactions.Transactions;

import java.util.ArrayList;

public record Stats(BuySellMaps buySellMaps) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
//        var prices = new ArrayList<>(buySellMaps.getBuyMap().keySet());
//        var amounts = new ArrayList<>(buySellMaps.getBuyMap().values());
//        int max = prices.stream().mapToInt(i -> i).max().orElse(0);
//        Hans hans = new Hans(prices, amounts, max);
//        sb.append("Buy histogram:");
//        sb.append("\n");
//        sb.append(hans.printHistrogram(3));
//        sb.append("Average prices:\n");
//        sb.append(Transactions.getWeightedMean(buySellMaps));
        var result = sb.toString();
        return result;
    }
}
