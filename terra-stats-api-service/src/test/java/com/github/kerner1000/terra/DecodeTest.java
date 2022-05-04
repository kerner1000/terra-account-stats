package com.github.kerner1000.terra;

import org.junit.jupiter.api.Test;

import java.util.Base64;

public class DecodeTest {

    @Test
    public void test(){
        var text = "eyJkZXBvc2l0X2NvbGxhdGVyYWwiOnt9fQ==";
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        String decodedString = new String(decodedBytes);
        System.out.println(decodedString);
    }
}
