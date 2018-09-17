package com.chatter.service;

import java.util.Optional;

import com.chatter.model.Channel;
import com.chatter.model.User;
import com.chatter.repository.ChannelRepository;
import com.chatter.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ChatAdminServiceTest {
    @MockBean
    private UserRepository userRepo;
    @MockBean
    private ChannelRepository channelRepo;
    private ChatAdminService chatAdminService;
    private User user1;
    private Channel channel;

    @Before
    public void setup() {
        chatAdminService = new ChatAdminService(userRepo, channelRepo);
        user1 = new User("Bob", "bob@test.com");
        channel = new Channel("noise");
        when(userRepo.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(channelRepo.findById(channel.getId())).thenReturn(Optional.of(channel));
    }

    @Test
    public void getUser() {
        // GIVEN
        chatAdminService.addUser(user1);

        // WHEN
        Optional<User> optionalUser = chatAdminService.getUser(user1.getId());

        // THEN
        assertTrue(optionalUser.isPresent());
        assertEquals(user1.getId(), optionalUser.get().getId());
    }

    @Test
    public void getChannel() {
        //GIVEN
        chatAdminService.addChannel(channel);

        // WHEN
        Optional<Channel> optionalChannel = chatAdminService.getChannel(channel.getId());

        // THEN
        assertTrue(optionalChannel.isPresent());
        assertEquals(channel.getId(), optionalChannel.get().getId());
    }

   @Test
   public void subscribe() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addChannel(channel);
        chatAdminService.subscribe(user1, channel);

        // WHEN
        Optional<User> optionalUser = chatAdminService.getUser(user1.getId());
        Optional<Channel> optionalChannel = chatAdminService.getChannel(channel.getId());

        // THEN
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertTrue(optionalChannel.isPresent());
        Channel channel = optionalChannel.get();
        assertEquals(1, user.getSubscriptions().size());
        assertTrue(user.getSubscriptions().contains(channel.getId()));
        assertEquals(1, optionalChannel.get().getSubscribers().size());
   }

   @Test
    public void unsubscribe() {
        // GIVEN
        chatAdminService.addUser(user1);
        chatAdminService.addChannel(channel);
        chatAdminService.subscribe(user1, channel);

        // WHEN
        Optional<User> optionalUser = chatAdminService.getUser(user1.getId());
        chatAdminService.unsubscribe(user1, channel);

        // THEN
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertTrue(user.getSubscriptions() == null);
   }
}