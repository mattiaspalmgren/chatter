package com.chatter.repository;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import com.chatter.model.Message;
import org.springframework.stereotype.Repository;

/**
 * Stores message within a hash map, messageId -> Message
 */
@Repository
public class MessageBucket {
    private HashMap<String, Message> bucket;
    private BiPredicate<Message, String> isFrom = (message, fromId) -> message.fromId.equals(fromId);
    private BiPredicate<Message, String> isTo = (message, toId) -> message.toId.equals(toId);
    private BiPredicate<Message, String> hasParticipant = ((message, id) -> isFrom.test(message, id) || isTo.test(message, id));
    private Comparator<Message> messageTimeComparator = Comparator.comparing(message -> message.dateTime.truncatedTo(ChronoUnit.SECONDS));

    public MessageBucket() {
        bucket = new HashMap<>();
    }

    public void addMessage(Message message) {
        bucket.put(message.id, message);
    }

    public Optional<Message> getMessage(String messageId) {
        return Optional.ofNullable(bucket.get(messageId));
    }

    public Stream<Message> getDirectMessages(String fromId, String toId) {
        return getMessageStream()
                    .filter(message -> hasParticipant.test(message, fromId))
                    .filter(message -> hasParticipant.test(message, toId))
                    .sorted(messageTimeComparator.reversed());
    }

    public Stream<Message> getMessagesTo(String to) {
        return getMessageStream()
                .filter(message -> isTo.test(message, to));
    }

    private Stream<Message> getMessageStream() {
        return bucket.values().stream();
    }
}
