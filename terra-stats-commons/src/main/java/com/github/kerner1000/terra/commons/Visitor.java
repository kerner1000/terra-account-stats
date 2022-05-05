package com.github.kerner1000.terra.commons;

public interface Visitor<I,O> {

    O visit(I in);
}
