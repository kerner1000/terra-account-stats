package com.github.kerner1000.terra.data;

import lombok.Data;

import java.util.List;

@Data
public class Sender {

    Account account;

    List<Amount> amounts;
}
