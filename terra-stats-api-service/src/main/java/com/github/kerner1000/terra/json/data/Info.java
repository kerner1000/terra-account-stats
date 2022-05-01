package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Info {

    @JsonProperty("native_token")
    NativeToken nativeToken;
}
