
package com.chatter.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.chatter.model.Channel;
import com.chatter.model.Message;
import com.chatter.model.User;
import com.chatter.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ChatServiceTest {
    @MockBean
    private ChatAdminService chatAdminService;
    @MockBean
    private MessageRepository messageRepository;
    private ChatService chatService;
    private User user1 = new User("alice", "alice@chatter.com");
    private User user2 = new User("bob", "bob@chatter.com");
    private Channel channel = new Channel("official");

    @Before
    public void setup() {
        when(chatAdminService.entityExist(user1.getId())).thenReturn(true);
        when(chatAdminService.entityExist(user2.getId())).thenReturn(true);
        when(chatAdminService.entityExist(channel.getId())).thenReturn(true);
        chatService = new ChatService(messageRepository, chatAdminService);
    }

    @Test
    public void sendMessage() {
        // GIVEN
        Message message = new Message("Hello", user1.getId(), user2.getId());

        // WHEN
        chatService.sendMessage(message.getText(), message.getFromId(), message.getToId());

        // THEN
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    public void getMessagesForChannel() {
        // GIVEN
        Message message = new Message("Hello", user1.getId(), channel.getId());
        when(messageRepository.findByToId(channel.getId())).thenReturn(Collections.singletonList(message));

        // WHEN
        List<Message> messages = chatService.getMessages(channel.getId()).collect(Collectors.toList());

        // THEN
        assertEquals(1, messages.size());
    }
}