package com.chatter.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Type for defining a user and its subscriptions
 */
public class User extends Entity {
    public final String email;
    public final Set<String> subscriptions;

    @JsonCreator
    public User(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email)
    {
        super(name);
        this.email = email;
        this.subscriptions = new HashSet<>();
    }

    public void subscribe(String channelId) {
        subscriptions.add(channelId);
    }

    public void unsubscribe(String channelId) {
        subscriptions.remove(channelId);
    }
}
