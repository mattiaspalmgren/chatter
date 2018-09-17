package com.chatter.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MessageSerializer extends StdSerializer<Message> {

    public MessageSerializer() {
        this(null);
    }

    public MessageSerializer(Class<Message> message) {
        super(message);
    }

    @Override
    public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializer) {
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", message.getId());
            jsonGenerator.writeStringField("text", message.getText());
            jsonGenerator.writeObjectFieldStart("timestamp");
            jsonGenerator.writeStringField("dateTime", message.getDateTime().toString());
            jsonGenerator.writeEndObject();
            jsonGenerator.writeObjectFieldStart("from");
            jsonGenerator.writeStringField("id", message.getFromId());
            jsonGenerator.writeEndObject();
            jsonGenerator.writeObjectFieldStart("to");
            jsonGenerator.writeStringField("id", message.getToId());
            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}