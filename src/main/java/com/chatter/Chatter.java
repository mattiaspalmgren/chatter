package com.chatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Chatter extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Chatter.class, args);
    }
}
