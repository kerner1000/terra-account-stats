package com.github.kerner1000.terra.commons;

import org.openapitools.model.BuySellSwaps;

import java.util.stream.Collectors;

public record Stats(BuySellSwaps buySellSwaps, SwapPrices swapPrices) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        var prices = buySellSwaps.getBuy().getSwaps().stream().mapToInt(e -> e.getPrice().intValue()).boxed().collect(Collectors.toList());
        var amounts = buySellSwaps.getBuy().getSwaps().stream().mapToInt(e -> e.getAmount().intValue()).boxed().collect(Collectors.toList());
        int max = prices.stream().mapToInt(i -> i).max().orElse(0);
        Hans hans = new Hans(prices, amounts, max);
        sb.append("Buy histogram:");
        sb.append("\n");
        sb.append(hans.printHistrogram(3));
        sb.append("Average prices:\n");
        sb.append(swapPrices);
        var result = sb.toString();
        return result;
    }
}
