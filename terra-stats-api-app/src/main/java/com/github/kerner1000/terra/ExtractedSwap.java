package com.github.kerner1000.terra;

import lombok.Data;

@Data
public class ExtractedSwap {
    private final SwapType swapType;
    private final double price;
    private final double giveAmount;

    public ExtractedSwap(SwapType swapType, double price, double giveAmount) {
        this.swapType = swapType;
        this.price = price;
        this.giveAmount = giveAmount;
    }

    @Override
    public String toString() {
        return swapType +
                ", price=" + price +
                ", giveAmount=" + giveAmount;
    }
}

