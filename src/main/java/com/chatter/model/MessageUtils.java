package com.chatter.model;

import java.time.ZonedDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtils {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);
    private static ObjectMapper mapper;

    private MessageUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("MessageSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Message.class, new MessageSerializer());
        mapper.registerModule(module);
    }

    public static class ZonedDateTimeConverter implements DynamoDBTypeConverter<String, ZonedDateTime> {
        @Override
        public String convert( final ZonedDateTime time ) {
            return time.toString();
        }

        @Override
        public ZonedDateTime unconvert( final String stringValue ) {
            return ZonedDateTime.parse(stringValue);
        }
    }

    public static String messageToJson(Message message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.error("Error when serializing message");
            return "";
        }
    }
}
