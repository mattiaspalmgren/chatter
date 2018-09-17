package com.chatter.repository;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.chatter.LocalDynamoRule;
import com.chatter.config.DynamoConfig;
import com.chatter.model.Message;
import com.chatter.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DynamoConfig.class)
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:4000/",
        "amazon.aws.accesskey=key1",
        "amazon.aws.secretkey=key2"
})
public class MessageRepositoryTest {
    @Autowired
    private MessageRepository repository;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @ClassRule
    public static LocalDynamoRule dynamoDB = new LocalDynamoRule("4000");
    private DynamoDBMapper dynamoDBMapper;
    private User user1 = new User("alice", "alice@chatter.com");
    private User user2 = new User("bob", "bob@chatter.com");

    @Before
    public void setup() {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Message.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);
    }

    @Test
    public void getDirectMessages() {
        // GIVEN
        List<Message> messages = Arrays.asList(
                new Message("hello", user1.getId(), user2.getId()),
                new Message("hello again", user1.getId(), user2.getId()),
                new Message("hello, other", user1.getId(), "3")
        );

        // WHEN
        messages.forEach(repository::save);

        // THEN
        List<Message> foundMessages = repository.findByFromIdAndToId(user1.getId(), user2.getId());
        assertTrue(foundMessages.size() == 2);
    }

    @Test
    public void getToMessages() {
        // GIVEN
        List<Message> messages = Arrays.asList(
                new Message("hello", user1.getId(), user2.getId()),
                new Message("hello again", user1.getId(), user2.getId()),
                new Message("hello, other", user1.getId(), "3")
        );

        // WHEN
        messages.forEach(repository::save);

        // THEN
        List<Message> foundMessages = repository.findByToId(user2.getId());
        assertTrue(foundMessages.size() == 2);
    }

    @After
    public void tearDown() {
        DeleteTableRequest tableRequest = dynamoDBMapper.generateDeleteTableRequest(Message.class);
        amazonDynamoDB.deleteTable(tableRequest);
    }
}

