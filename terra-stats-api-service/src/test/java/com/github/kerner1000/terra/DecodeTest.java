package com.github.kerner1000.terra;

import org.junit.jupiter.api.Test;

import java.util.Base64;

public class DecodeTest {

    @Test
    public void test(){
        var text = "eyJzd2FwIjp7Im1heF9zcHJlYWQiOiIwLjAwNSIsImJlbGllZl9wcmljZSI6IjEuMTc3Njg3MzQ3MDkyMDE5MDcxIn19";
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        String decodedString = new String(decodedBytes);
        System.out.println(decodedString);
    }
}
