package com.github.kerner1000.terra.data;

import lombok.Data;

import java.util.List;

@Data
public class Sub {

    List<String> type;

    List<Sender> sender;

     Additional additional;
}
