package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExecuteMessage {

    Swap swap;

    Send send;

    @JsonProperty("assert_limit_order")
    AssertLimitOrder assertLimitOrder;

}
