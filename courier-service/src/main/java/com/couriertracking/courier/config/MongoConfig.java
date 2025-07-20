package com.couriertracking.courier.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.couriertracking.courier.adapters.out.mongodb")
public class MongoConfig {
}