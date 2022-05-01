package com.github.kerner1000.terra;

public record SwapPrices(double buy, double sell) {

    @Override
    public String toString() {
        return
                "buy=" + String.format("%.2f", buy) + ", " +
                        "sell=" + String.format("%.2f", sell);
    }


}
