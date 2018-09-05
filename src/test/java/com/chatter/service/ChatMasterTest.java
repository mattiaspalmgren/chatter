package com.chatter.service;

import com.chatter.repository.ChannelRepository;
import com.chatter.repository.MessageBucket;
import com.chatter.repository.UserRepository;
import com.chatter.type.Channel;
import com.chatter.type.User;
import org.junit.Before;

public abstract class ChatMasterTest {
    ChatAdminService chatAdminService;
    ChatService chatService;
    MessageBucket messageBucket;
    User user1 ;
    User user2;
    Channel channel;

   @Before
    public void setup() {
        UserRepository users = new UserRepository();
        ChannelRepository channels = new ChannelRepository();
        messageBucket = new MessageBucket();
        chatAdminService = new ChatAdminService(users, channels);
        chatService = new ChatService(messageBucket, chatAdminService);
        channel = new Channel("noise");
        user1 = new User("Bob", "bob@test.com");
        user2 = new User("Alice", "alice@test.com");
    }
}
