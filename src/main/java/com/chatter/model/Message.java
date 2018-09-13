package com.chatter.model;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Type for defining a message within the chat
 */
public class Message {
    public final String id;
    public final String text;
    public final ZonedDateTime dateTime;
    public final String fromId;
    public final String toId;

    @JsonCreator
    public Message(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text,
            @JsonProperty("timestamp") Map<String, String> timestamp,
            @JsonProperty("from") Map<String, String> from,
            @JsonProperty("to") Map<String, String> to)
    {
        this.id = id;
        this.text = text;
        this.dateTime = ZonedDateTime.parse(timestamp.get("dateTime"));
        this.fromId = from.get("id");
        this.toId = to.get("id");
    }

    public Message(String text, String from, String to) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.dateTime = ZonedDateTime.now();
        this.fromId = from;
        this.toId = to;
    }

    public Message(String text, String from, String to, ZonedDateTime dateTime) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.dateTime = dateTime;
        this.fromId = from;
        this.toId = to;
    }
}