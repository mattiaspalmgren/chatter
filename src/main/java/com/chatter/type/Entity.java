package com.chatter.type;

import java.util.UUID;

/**
 * Superclass for defining named and identifiable entities
 */
public class Entity {
    public final String id;
    public final String name;

    Entity(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
