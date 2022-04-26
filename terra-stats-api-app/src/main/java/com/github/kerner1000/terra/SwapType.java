package com.github.kerner1000.terra;

import lombok.Data;

import java.util.Objects;

@Data
final class SwapType {
    private final Coin give;
    private final Coin get;

    SwapType(Coin give, Coin get) {
        this.give = give;
        this.get = get;
    }

    @Override
    public String toString() {
        return "[" +
                "give=" + give + ", " +
                "get=" + get + ']';
    }


}
