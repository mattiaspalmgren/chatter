package com.chatter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.chatter.config.DynamoConfig;
import com.chatter.model.Channel;
import com.chatter.model.Message;
import com.chatter.model.MessageUtils;
import com.chatter.model.User;
import com.chatter.repository.ChannelRepository;
import com.chatter.repository.MessageRepository;
import com.chatter.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = { Chatter.class, DynamoConfig.class })
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:4000/",
        "amazon.aws.accesskey=key1",
        "amazon.aws.secretkey=key2"
})
public class IntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @ClassRule
    public static LocalDynamoRule dynamoDB = new LocalDynamoRule("4000");
    private DynamoDBMapper dynamoDBMapper;
    private List<Class> models = Arrays.asList(User.class, Channel.class, Message.class);

    @Before
    public void setup() {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        models.forEach(type -> {
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(type);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableRequest);
        });
    }

    @Test
    public void scenario() throws Exception {
        // GIVEN
        ObjectMapper mapper = new ObjectMapper();
        User user = new User("alice", "alice@chatter.com");
        Channel channel = new Channel("channel");
        Message message = new Message("Hello", user.getId(), channel.getId());

        // THEN
        mvc.perform(post("/admin/users").contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(user)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(user.getName())))
           .andExpect(jsonPath("$.email", is(user.getEmail())));

        mvc.perform(post("/admin/channels").contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(channel)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(channel.getName())));

        mvc.perform(post("/admin/subscriptions/" + user.getId() + "/" + channel.getId()))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(user.getName())));

        mvc.perform(post("/chat/messages").contentType(MediaType.APPLICATION_JSON)
                                           .content(MessageUtils.messageToJson(message)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.text", is(message.getText())));
    }

    @After
    public void tearDown() {
        models.forEach(type -> {
            DeleteTableRequest tableRequest = dynamoDBMapper.generateDeleteTableRequest(type);
            amazonDynamoDB.deleteTable(tableRequest);
        });
    }
}

