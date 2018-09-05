
package com.chatter.service;

import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatServiceTest extends ChatMasterTest {
    @Test
    public void sendMessage() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addUser(user2);

        // WHEN
        chatService.sendMessage("Hello", user1.id, user2.id);

        // THEN
        assertEquals(1, messageBucket.getMessagesTo(user2.id).count());
    }

    @Test
    public void getMessagesForChannel() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addChannel(channel);

        // WHEN
        chatService.sendMessage("Hello, channel", user1.id, channel.id);

        // THEN
        assertEquals(1, chatService.getMessages(channel).collect(Collectors.toList()).size());
    }

    @Test
    public void getMessagesForUser() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addUser(user2);
        chatAdminService.addChannel(channel);
        chatAdminService.subscribe(user1, channel);

        // WHEN
        chatService.sendMessage("Hello, channel", user2.id, channel.id);
        chatService.sendMessage("Hello, user1", user2.id, user1.id);

        // THEN
        assertEquals(2, chatService.getAllMessages(user1).collect(Collectors.toList()).size());
    }
}