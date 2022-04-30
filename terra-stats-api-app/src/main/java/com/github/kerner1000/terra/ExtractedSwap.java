package com.github.kerner1000.terra;

public record ExtractedSwap(SwapType swapType, WeightedMeanSwapMaps swapMaps) {

    @Override
    public String toString() {
        return swapType +
                ", swaps:\n" + swapMaps;
    }
}

