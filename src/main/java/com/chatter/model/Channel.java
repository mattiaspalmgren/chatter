package com.chatter.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Type for defining a channel and its subscribers
 */
@DynamoDBTable(tableName = "Channel")
public class Channel extends Entity {
    @DynamoDBAttribute
    private List<String> subscribers;

    public Channel() {}

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

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }
}
