
package com.chatter.service;

import java.util.stream.Stream;

import com.chatter.controllers.EntityNotFoundException;
import com.chatter.repository.MessageBucket;
import com.chatter.model.Channel;
import com.chatter.model.Message;
import com.chatter.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handling of sending and fetching messages between users
 */
@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private MessageBucket messageBucket;
    private ChatAdminService chatAdminService;

    @Autowired
    public ChatService(MessageBucket messageBucket, ChatAdminService chatAdminService) {
        this.messageBucket = messageBucket;
        this.chatAdminService = chatAdminService;
    }

    public Stream<Message> getMessages(User user) {
        return getMessages(user.id);
    }

    public Stream<Message> getMessages(Channel channel) {
        return getMessages(channel.id);
    }

    public Stream<Message> getMessages(String id) {
        return messageBucket.getMessagesTo(id);
    }

    public Stream<Message> getAllMessages(User user) {
        Stream<Message> subscriptionMessages = user.subscriptions.stream().flatMap(this::getMessages);
        Stream<Message> directMessages = getMessages(user);
        return Stream.concat(subscriptionMessages, directMessages);
    }

    public Message sendMessage(String text, String fromId, String toId) {
        validate(fromId, toId);
        Message message = new Message(text, fromId, toId);
        messageBucket.addMessage(message);
        logger.info("{} sent a message to {}",
                chatAdminService.getName(message.fromId), chatAdminService.getName(message.toId) );
        return message;
    }

    private void validate(String toId, String fromId) {
        boolean fromExists = chatAdminService.entityExist.test(fromId);
        if (!fromExists) {
            throw new EntityNotFoundException(fromId);
        }
        boolean toExists = chatAdminService.entityExist.test(toId);
        if (!toExists) {
            throw new EntityNotFoundException(toId);
        }
    }
}