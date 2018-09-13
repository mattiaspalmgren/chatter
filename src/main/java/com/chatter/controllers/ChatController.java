package com.chatter.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.chatter.service.ChatService;
import com.chatter.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value="/messages")
    public Message sendMessage(@RequestBody JsonNode message) {
        return chatService.sendMessage(
                message.get("text").asText(),
                message.get("fromId").asText(),
                message.get("toId").asText()
        );
    }

    @GetMapping(value="messages/{id}")
    public List<Message> getMessages(@PathVariable String id) {
        return chatService.getMessages(id).collect(Collectors.toList());
    }
}
