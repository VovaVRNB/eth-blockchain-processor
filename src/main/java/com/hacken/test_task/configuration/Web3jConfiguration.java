package com.hacken.test_task.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfiguration {

    @Value(value = "${system.public-node-url}")
    private String publicNodeUrl;

    @Value(value = "${system.app-id}")
    private String applicationId;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(publicNodeUrl + applicationId));
    }
}
