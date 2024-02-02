package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class hashTest {
    @Test
    public void test() {
        String test = "test";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(test);
        System.out.println(hash);
    }
}
