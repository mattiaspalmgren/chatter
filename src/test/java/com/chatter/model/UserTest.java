package com.chatter.model;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private File userJson;
    private ObjectMapper mapper;

    @Before
    public void init() {
        userJson = new File("src/test/resources/user.json");
        mapper = new ObjectMapper();
    }

    @Test
    public void createUserFromJson() throws IOException {
        // WHEN
        User user = mapper.readValue(userJson, User.class);

        // THEN
        assertEquals("alice", user.name);
        assertEquals("alice@chatter.com", user.email);
    }
}
