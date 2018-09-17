package com.chatter.controllers;

import java.util.stream.Stream;

import com.chatter.model.MessageUtils;
import com.chatter.service.ChatService;
import com.chatter.model.Message;
import com.chatter.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(ChatController.class)
public class ChatControllerTest {
    private final static User USER1 = new User("alice", "alice@chatter.com");
    private final static User USER2 = new User("bob", "bob@chatter.com");
    private final static String CHAT_URL = "/chat";
    private final static String MESSAGES_URL = CHAT_URL + "/messages";

    @Autowired
    private MockMvc client;

    @MockBean
    private ChatService chatService;

    @Test
    public void sendMessage() throws Exception {
        // GIVEN
        String text = "Hello";
        Message message = new Message(text, USER1.getId(), USER2.getId());
        when(chatService.sendMessage(text, USER1.getId(), USER2.getId())).thenReturn(message);

        // WHEN
        MockHttpServletResponse response = client.perform(
                post(MESSAGES_URL).contentType(APPLICATION_JSON_UTF8)
                                  .content(MessageUtils.messageToJson(message))
        ).andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("Hello"));
        assertTrue(response.getContentAsString().contains(USER1.getId()));
        assertTrue(response.getContentAsString().contains(USER2.getId()));
        assertTrue(response.getContentAsString().contains("dateTime"));
    }

    @Test
    public void getMessages() throws Exception {
        // GIVEN
        Stream<Message> messages = Stream.of(
            new Message("Hello", USER1.getId(), USER2.getId()),
            new Message("Hello, again", USER1.getId(), USER2.getId())
        );
        when(chatService.getMessages(USER2.getId())).thenReturn(messages);

        // WHEN
        MockHttpServletResponse response = client.perform(get(MESSAGES_URL + "/" + USER2.getId()))
                                                 .andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("Hello"));
        assertTrue(response.getContentAsString().contains("Hello, again"));
    }
}