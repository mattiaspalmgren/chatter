package com.chatter.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.chatter.model.Channel;
import com.chatter.model.Entity;
import com.chatter.model.User;
import com.chatter.repository.ChannelRepository;
import com.chatter.repository.UserRepository;
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

    @Autowired
    public ChatAdminService(UserRepository users, ChannelRepository channels) {
        this.users = users;
        this.channels = channels;
    }

    public boolean entityExist(String id) {
        return users.findById(id).isPresent() || channels.findById(id).isPresent();
    }

    public User addUser(User user) {
        users.save(user);
        logger.info("User {} added", user.getName());
        return user;
    }

    public Channel addChannel(Channel channel) {
        channels.save(channel);
        logger.info("Channel {} added", channel.getName());
        return channel;
    }

    public Optional<User> getUser(String id) {
        return users.findById(id);
    }

    public Optional<Channel> getChannel(String id) {
        return channels.findById(id);
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
        user.subscribe(channel.getId());
        channel.addSubscriber(user.getId());
        logger.info("User {} subscribed to {}", user.getName(), channel.getName());
        return user;
    }

    public User unsubscribe(User user, Channel channel) {
        user.unsubscribe(channel.getId());
        channel.removeSubscriber(user.getId());
        logger.info("User {} unsubscribed Channel {}", user.getName(), channel.getName());
        return user;
    }
}
