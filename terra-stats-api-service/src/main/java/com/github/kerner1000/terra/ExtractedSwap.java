package com.github.kerner1000.terra;

import com.github.kerner1000.terra.commons.BuySellMaps;

public record ExtractedSwap(SwapType swapType, BuySellMaps swapMaps) {

    @Override
    public String toString() {
        return swapType +
                ", swaps:\n" + swapMaps;
    }
}

