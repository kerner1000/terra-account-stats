package com.github.kerner1000.terra.bot.message.average;

import lombok.Data;

@Data
public class ApiResponse {

    private double buy, sell;

    @Override
    public String toString() {
        return
                "buy: " + String.format("%.2f", buy) +
                ", sell: " + String.format("%.2f",sell);

    }
}
