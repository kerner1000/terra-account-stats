package com.github.kerner1000.terra.data;

import lombok.Data;
import lombok.ToString;

@Data
public class Account {

    @ToString.Exclude
    String id;
}
