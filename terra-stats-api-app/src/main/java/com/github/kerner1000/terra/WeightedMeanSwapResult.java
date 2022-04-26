package com.github.kerner1000.terra;

import lombok.Data;

import java.util.Objects;

@Data
public final class WeightedMeanSwapResult {
    private final double buy;
    private final double sell;

    public WeightedMeanSwapResult(double buy, double sell) {
        this.buy = buy;
        this.sell = sell;
    }

    @Override
    public String toString() {
        return '[' +
                "buy=" + String.format("%.2f", buy) + ", " +
                "sell=" + String.format("%.2f", sell) + ']';
    }


}
