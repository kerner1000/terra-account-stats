package com.github.kerner1000.terra.data;

import lombok.Data;

import java.util.List;

@Data
public class Event {

    String kind;

    List<Sub> sub;
}
