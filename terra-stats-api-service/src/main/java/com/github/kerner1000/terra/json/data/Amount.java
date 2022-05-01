package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class Amount {

    String text;

    @ToString.Include
    String currency;

    Number numeric;

    @ToString.Include
    Number numericNormalized(){
        return numeric.doubleValue() / 100000;
    }
}
