package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Txs {

    private List<Transaction> txs;
}
