package com.chatter.type;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    private File messageFile;
    private ObjectMapper mapper;

    @Before
    public void init() {
        messageFile = new File("src/test/resources/message.json");
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("MessageSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Message.class, new MessageSerializer());
        mapper.registerModule(module);
    }

    @Test
    public void roundTripTest() throws IOException {
        // WHEN
        JsonNode originalJson = mapper.readTree(messageFile);
        Message msg = mapper.readValue(messageFile, Message.class);
        String jsonString = mapper.writeValueAsString(msg);
        JsonNode resultingJson = mapper.readTree(jsonString);

        // THEN
        assertEquals(originalJson, resultingJson);
    }
}
