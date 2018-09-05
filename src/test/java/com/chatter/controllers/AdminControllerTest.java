package com.chatter.controllers;

import java.util.Optional;

import com.chatter.service.ChatAdminService;
import com.chatter.type.Channel;
import com.chatter.type.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    private final static User USER = new User("alice", "alice@chatter.com");
    private final static String USERS_URL = "/admin/users";
    private final static Channel CHANNEL = new Channel("official");
    private final static String CHANNELS_URL = "/admin/channels";
    private final static String SUBSCRIPTIONS_URL = "/admin/subscriptions";

    @Autowired
    private MockMvc client;

    @MockBean
    private ChatAdminService chatAdminService;

    @Test
    public void addUser() throws Exception {
        // GIVEN
        when(chatAdminService.addUser(any())).thenReturn(USER);

        // WHEN
        MockHttpServletResponse response = client.perform(
                post(USERS_URL).contentType(APPLICATION_JSON_UTF8)
                               .content("{ \"name\": \"alice\", \"email\": \"alice@chatter.com\"}")

        ).andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("alice"));
        assertTrue(response.getContentAsString().contains("subscriptions"));
    }

    @Test
    public void getUser() throws Exception {
        // GIVEN
        when(chatAdminService.getUser(USER.id)).thenReturn(Optional.of(USER));

        // WHEN
        MockHttpServletResponse response = client.perform(get(USERS_URL + "/" + USER.id))
                                                 .andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("alice"));
    }

    @Test
    public void addChannel() throws Exception {
        // GIVEN
        when(chatAdminService.addChannel(any())).thenReturn(CHANNEL);

        // WHEN
        MockHttpServletResponse response = client.perform(
                post(CHANNELS_URL).contentType(APPLICATION_JSON_UTF8)
                                  .content("{\"name\": \"official\"}")
        ).andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("official"));
        assertTrue(response.getContentAsString().contains("id"));
        assertTrue(response.getContentAsString().contains("subscribers"));
    }

    @Test
    public void getChannel() throws Exception {
        // GIVEN
        when(chatAdminService.getChannel(CHANNEL.id)).thenReturn(Optional.of(CHANNEL));

        // WHEN
        MockHttpServletResponse response = client.perform(get(CHANNELS_URL + "/" + CHANNEL.id))
                                                 .andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("official"));
    }

    @Test
    public void subscribe() throws Exception {
        // GIVEN
        when(chatAdminService.getUser(USER.id)).thenReturn(Optional.of(USER));
        when(chatAdminService.getChannel(CHANNEL.id)).thenReturn(Optional.of(CHANNEL));
        when(chatAdminService.subscribe(USER, CHANNEL)).thenCallRealMethod();

        // WHEN
        MockHttpServletResponse response = client.perform(
                post(SUBSCRIPTIONS_URL + "/" + USER.id + "/" + CHANNEL.id)
        ).andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("alice"));
        assertTrue(response.getContentAsString().contains(CHANNEL.id));
    }

    @Test
    public void unsubscribe() throws Exception {
        // GIVEN
        USER.subscribe(CHANNEL.id);
        when(chatAdminService.getUser(USER.id)).thenReturn(Optional.of(USER));
        when(chatAdminService.getChannel(CHANNEL.id)).thenReturn(Optional.of(CHANNEL));
        when(chatAdminService.unsubscribe(USER, CHANNEL)).thenCallRealMethod();

        // WHEN
        MockHttpServletResponse response = client.perform(
                delete(SUBSCRIPTIONS_URL + "/" + USER.id + "/" + CHANNEL.id)
        ).andReturn().getResponse();

        // THEN
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("alice"));
        assertFalse(response.getContentAsString().contains(CHANNEL.id));
    }
}
