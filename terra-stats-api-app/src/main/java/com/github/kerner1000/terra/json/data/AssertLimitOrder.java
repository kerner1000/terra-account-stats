package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssertLimitOrder {

    @JsonProperty("ask_denom")
    String askDenom;

    @JsonProperty("offer_coin")
    OfferCoin offerCoin;

    @JsonProperty("minimum_receive")
    Number minimumReceive;
}
