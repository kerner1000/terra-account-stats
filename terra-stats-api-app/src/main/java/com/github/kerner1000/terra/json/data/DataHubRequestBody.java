package com.github.kerner1000.terra.json.data;

import lombok.Data;

import java.util.List;

@Data
public class DataHubRequestBody {

    String network;
    List<String> account;
    List<String> chainIds;
    List<String> type;
    int limit;
    int offset;

}
