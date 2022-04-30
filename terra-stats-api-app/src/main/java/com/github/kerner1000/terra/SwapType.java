package com.github.kerner1000.terra;

public record SwapType(Coin give, Coin get) {

    @Override
    public String toString() {
        return "[" +
                "give=" + give + ", " +
                "get=" + get + ']';
    }


}
