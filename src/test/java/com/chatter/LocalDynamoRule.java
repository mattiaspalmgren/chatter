package com.chatter;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.junit.rules.ExternalResource;

public class LocalDynamoRule extends ExternalResource {
    private DynamoDBProxyServer server;
    private String port;

    public LocalDynamoRule(String port) {
        this.port = port;
    }

    @Override
    protected void before() throws Exception {
        server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", port});
        server.start();
    }

    @Override
    protected void after() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
