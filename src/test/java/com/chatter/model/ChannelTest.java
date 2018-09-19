package com.chatter.model;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChannelTest {
    private File channelJson;
    private ObjectMapper mapper;

    @Before
    public void init() {
        channelJson = new File("src/test/resources/channel.json");
        mapper = new ObjectMapper();
    }

    @Test
    public void createChannelFromJson() throws IOException {
        // WHEN
        Channel channel = mapper.readValue(channelJson, Channel.class);

        // THEN
        assertEquals("official", channel.getName());
    }
}