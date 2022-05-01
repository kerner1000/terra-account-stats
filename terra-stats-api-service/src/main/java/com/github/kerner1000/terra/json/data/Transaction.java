package com.github.kerner1000.terra.json.data;

import lombok.Data;

@Data
public class Transaction {

    String txhash;

    Tx tx;
}
