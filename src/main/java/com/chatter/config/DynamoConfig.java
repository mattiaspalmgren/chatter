package com.chatter.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.chatter.repository")
public class DynamoConfig {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String amazonAWSAccessKey;
    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String amazonAWSSecretKey;
    @Value("${AWS_DYNAMODB_ENDPOINT:}")
    private String amazonDynamoDBEndpoint;
    @Value("${AWS_DEFAULT_REGION}")
    private String amazonAWSRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return StringUtils.isEmpty(amazonDynamoDBEndpoint) ?
                getDb() :
                getLocalDb();
    }

    private AmazonDynamoDB getDb() {
        return AmazonDynamoDBClientBuilder.standard()
                                          .withRegion(amazonAWSRegion)
                                          .build();
    }

    private AmazonDynamoDB getLocalDb() {
        return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonAWSRegion)
        ).build();
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
}

