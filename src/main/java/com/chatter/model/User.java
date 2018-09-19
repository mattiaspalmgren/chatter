package com.chatter.model;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Objects.isNull;

/**
 * Type for defining a user and its subscriptions
 */
@DynamoDBTable(tableName = "User")
public class User extends Entity {
    @DynamoDBAttribute
    private String email;
    @DynamoDBAttribute
    private Set<String> subscriptions;

    public User() {}

    @JsonCreator
    public User(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email)
    {
        super(name);
        this.email = email;
    }

    public void subscribe(String channelId) {
        if (isNull(subscriptions)) {
            subscriptions = new HashSet<>();
        }
        subscriptions.add(channelId);
    }

    public void unsubscribe(String channelId) {
        subscriptions.remove(channelId);
        if (subscriptions.isEmpty()) {
            subscriptions = null;
        }
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getSubscriptions() {
        return subscriptions;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubscriptions(Set<String> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
