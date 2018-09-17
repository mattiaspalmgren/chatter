
package com.chatter.service;

import java.util.stream.Stream;

import com.chatter.controllers.EntityNotFoundException;
import com.chatter.model.Message;
import com.chatter.repository.MessageRepository;
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
    private MessageRepository messageRepository;
    private ChatAdminService chatAdminService;

    @Autowired
    ChatService(MessageRepository messageRepository, ChatAdminService chatAdminService) {
        this.messageRepository = messageRepository;
        this.chatAdminService = chatAdminService;
    }

    public Stream<Message> getMessages(String id) {
        return messageRepository.findByToId(id).stream();
    }

    public Message sendMessage(String text, String fromId, String toId) {
        validate(fromId, toId);
        Message message = new Message(text, fromId, toId);
        messageRepository.save(message);
        logger.info("{} sent a message to {}",
                chatAdminService.getName(message.getFromId()), chatAdminService.getName(message.getToId()) );
        return message;
    }

    private void validate(String toId, String fromId) {
        boolean fromExists = chatAdminService.entityExist(fromId);
        if (!fromExists) {
            throw new EntityNotFoundException(fromId);
        }
        boolean toExists = chatAdminService.entityExist(toId);
        if (!toExists) {
            throw new EntityNotFoundException(toId);
        }
    }
}