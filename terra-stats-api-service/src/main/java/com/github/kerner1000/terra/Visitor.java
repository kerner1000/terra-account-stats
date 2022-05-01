package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.Transaction;

public interface Visitor<I,O> {

    O visit(I in);
}
