package com.github.kerner1000.terra.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExecuteMessage {

    Swap swap;

    @JsonProperty("assert_limit_order")
    AssertLimitOrder assertLimitOrder;

}
