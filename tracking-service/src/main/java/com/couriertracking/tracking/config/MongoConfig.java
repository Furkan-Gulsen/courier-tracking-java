package com.couriertracking.tracking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.couriertracking.tracking.adapters.out.mongodb")
public class MongoConfig {
}