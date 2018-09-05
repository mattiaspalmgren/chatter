package com.chatter.type;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Type for defining a channel and its subscribers
 */
public class Channel extends Entity {
    public final List<String> subscribers;

    @JsonCreator
    public Channel(
            @JsonProperty("name") String name
    ) {
        super(name);
        this.subscribers = new ArrayList<>();
    }

    public void addSubscriber(String userId) {
        subscribers.add(userId);
    }

    public void removeSubscriber(String userId) {
        subscribers.remove(userId);
    }
}
