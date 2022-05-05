package com.github.kerner1000.terra;

import org.junit.jupiter.api.Test;

import java.util.Base64;

public class DecodeTest {

    @Test
    public void test(){
        var text = "eyJhc3NlcnRfbGltaXRfb3JkZXIiOnsib2ZmZXJfY29pbiI6eyJkZW5vbSI6InVsdW5hIiwiYW1vdW50IjoiMTAwMDAwMDAifSwiYXNrX2Rlbm9tIjoidXVzZCIsIm1pbmltdW1fcmVjZWl2ZSI6IjE1NDgzNjMxNiJ9fQ==";
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        String decodedString = new String(decodedBytes);
        System.out.println(decodedString);
    }
}
