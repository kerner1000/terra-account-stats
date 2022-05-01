package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Swap {

    @JsonProperty("max_spread")
    Number maxSpread;

    @JsonProperty("belief_price")
    Number beliefPrice;

    @JsonProperty("offer_asset")
    OfferAsset offerAsset;
}
