package com.chatter.controllers;

import java.util.function.BiFunction;

import com.chatter.service.ChatAdminService;
import com.chatter.model.Channel;
import com.chatter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/admin")
public class AdminController {
    private final ChatAdminService chatAdminService;

    @Autowired
    public AdminController (ChatAdminService chatAdminService) {
        this.chatAdminService = chatAdminService;
    }

    @PostMapping(value="/users")
    public User addUser(@RequestBody User user) {
        return chatAdminService.addUser(user);
    }

    @GetMapping(value="/users/{id}")
    public User getUser(@PathVariable String id) {
        return chatAdminService.getUser(id)
                               .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @PostMapping(value="/channels")
    public Channel addChannel(@RequestBody Channel channel) {
        return chatAdminService.addChannel(channel);
    }

    @GetMapping(value="/channels/{id}")
    public Channel getChannel(@PathVariable String id) {
        return chatAdminService.getChannel(id)
                               .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @PostMapping(value="subscriptions/{user}/{channel}")
    public User subscribe(@PathVariable("user") String userId, @PathVariable("channel") String channelId) {
        return findAndApply(userId, channelId, chatAdminService::subscribe);
    }

    @DeleteMapping(value="subscriptions/{user}/{channel}")
    public User unsubscribe(@PathVariable("user") String userId, @PathVariable("channel") String channelId) {
        return findAndApply(userId, channelId, chatAdminService::unsubscribe);
    }

    private <T> T findAndApply(String userId, String channelId, BiFunction<User, Channel, T> supplier) {
        User user = chatAdminService.getUser(userId)
                                    .orElseThrow(() -> new EntityNotFoundException(userId));
        Channel channel = chatAdminService.getChannel(channelId)
                                          .orElseThrow(() -> new EntityNotFoundException(channelId));
        return supplier.apply(user, channel);
    }
}
