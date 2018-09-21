package com.chatter.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.chatter.LocalDynamoRule;
import com.chatter.config.DynamoConfig;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DynamoConfig.class)
@TestPropertySource(properties = {
        "AWS_DYNAMODB_ENDPOINT=http://localhost:4000/",
        "AWS_ACCESS_KEY_ID=key1",
        "AWS_ACCESS_SECRET_KEY=key2"
})
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @ClassRule
    public static LocalDynamoRule dynamoDB = new LocalDynamoRule("4000");
    private DynamoDBMapper dynamoDBMapper;

    @Before
    public void setup() {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(User.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);
    }

    @Test
    public void getUsers() {
        // GIVEN
        User user = new User("Alice", "alice@chatter.com");
        repository.save(user);

        // THEN
        List<User> users = repository.findAll();
        assertEquals("Alice", users.get(0).getName());
    }

    @Test
    public void getUserById() {
        // GIVEN
        User user = new User("Alice", "alice@chatter.com");
        repository.save(user);

        // THEN
        Optional<User> foundUser = repository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Alice", foundUser.get().getName());
    }

    @After
    public void tearDown() {
        DeleteTableRequest tableRequest = dynamoDBMapper.generateDeleteTableRequest(User.class);
        amazonDynamoDB.deleteTable(tableRequest);
    }
}

