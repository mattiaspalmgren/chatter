package com.chatter.service;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.chatter.repository.ChannelRepository;
import com.chatter.repository.UserRepository;
import com.chatter.model.Channel;
import com.chatter.model.Entity;
import com.chatter.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handling of user and channels and their relation
 */
@Service
public class ChatAdminService {
    private static final Logger logger = LoggerFactory.getLogger(ChatAdminService.class);
    private UserRepository users;
    private ChannelRepository channels;
    public final Predicate<String> entityExist = id -> users.containsKey(id) || channels.containsKey(id);

    @Autowired
    public ChatAdminService(UserRepository users, ChannelRepository channels) {
        this.users = users;
        this.channels = channels;
    }

    public User addUser(User user) {
        users.put(user.id, user);
        logger.info("User {} added", user.name);
        return user;
    }

    public Channel addChannel(Channel channel) {
        channels.put(channel.id, channel);
        logger.info("Channel {} added", channel.name);
        return channel;
    }

    public Optional<User> getUser(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<Channel> getChannel(String id) {
        return Optional.ofNullable(channels.get(id));
    }

    public String getName(String id) {
        Optional<String> name = Stream.of(getUser(id), getChannel(id))
                                      .filter(Optional::isPresent)
                                      .map(Optional::get)
                                      .map(Entity::getName)
                                      .findAny();
        return name.orElse("Unknown");
    }

    public User subscribe(User user, Channel channel) {
        user.subscribe(channel.id);
        channel.addSubscriber(user.id);
        logger.info("User {} subscribed to {}", user.name, channel.name);
        return user;
    }

    public User unsubscribe(User user, Channel channel) {
        user.unsubscribe(channel.id);
        channel.removeSubscriber(user.id);
        logger.info("User {} unsubscribed Channel {}", user.name, channel.name);
        return user;
    }
}
