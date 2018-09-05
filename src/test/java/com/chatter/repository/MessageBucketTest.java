package com.chatter.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chatter.type.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageBucketTest {
    private MessageBucket messageBucket;

    @Before
    public void init() {
        messageBucket = new MessageBucket();
    }

    @Test
    public void getDirectMessage() {
        // GIVEN
        Message msg1 = new Message("Hello", "usr1", "usr2");
        Message msg2 = new Message("Well, Hello", "usr2", "usr1");
        Stream.of(msg1, msg2).forEach(messageBucket::addMessage);

        // WHEN
        Optional<Message> message = messageBucket.getMessage(msg1.id);
        List<Message> messages = messageBucket.getDirectMessages("usr1", "usr2").collect(Collectors.toList());

        // THEN
        assert(message.isPresent());
        assertEquals(2, messages.size());
    }

    @Test
    public void getChannelMessages() {
        // GIVEN
        messageBucket.addMessage(new Message("Hello", "usr1", "noise"));

        // WHEN
        List<Message> messages = messageBucket.getMessagesTo("noise").collect(Collectors.toList());

        // THEN
        assertEquals(1, messages.size());
    }

    @Test
    public void getMessageInDateOrder() {
        // GIVEN
        ZonedDateTime now = ZonedDateTime.now();
        Message message1 = new Message("Hello", "usr1", "usr2", now);
        Message message2 = new Message("Hello, again", "usr1", "usr2", now.plusMinutes(1));

        // WHEN
        messageBucket.addMessage(message1);
        messageBucket.addMessage(message2);

        // THEN
        List<Message> messages = messageBucket.getDirectMessages("usr1", "usr2").collect(Collectors.toList());
        assertTrue("First message should be the latest", messages.get(0).dateTime.isAfter(messages.get(1).dateTime));
    }
}
