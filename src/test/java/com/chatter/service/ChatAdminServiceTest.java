package com.chatter.service;

import java.util.Optional;

import com.chatter.model.Channel;
import com.chatter.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatAdminServiceTest extends ChatMasterTest {

    @Test
    public void getUser() {
        // GIVEN
        chatAdminService.addUser(user1);

        // WHEN
        Optional<User> optionalUser = chatAdminService.getUser(user1.id);

        // THEN
        assertTrue(optionalUser.isPresent());
        assertEquals(user1.id, optionalUser.get().id);
    }

    @Test
    public void getChannel() {
        //GIVEN
        chatAdminService.addChannel(channel);

        // WHEN
        Optional<Channel> optionalChannel = chatAdminService.getChannel(channel.id);

        // THEN
        assertTrue(optionalChannel.isPresent());
        assertEquals(channel.id, optionalChannel.get().id);
    }

   @Test
   public void subscribe() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addChannel(channel);
        chatAdminService.subscribe(user1, channel);

        // WHEN
        Optional<User> optionalUser = chatAdminService.getUser(user1.id);
        Optional<Channel> optionalChannel = chatAdminService.getChannel(channel.id);

        // THEN
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertTrue(optionalChannel.isPresent());
        Channel channel = optionalChannel.get();
        assertEquals(1, user.subscriptions.size());
        assertTrue(user.subscriptions.contains(channel.id));
        assertEquals(1, optionalChannel.get().subscribers.size());
   }

   @Test
    public void unsubscribe() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addChannel(channel);
        chatAdminService.subscribe(user1, channel);

        // WHEN
        Optional<User> optionalUser = chatAdminService.getUser(user1.id);
        chatAdminService.unsubscribe(user1, channel);

        // THEN
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(0, user.subscriptions.size());
   }
}