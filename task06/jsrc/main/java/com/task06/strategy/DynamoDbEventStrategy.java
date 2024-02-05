package com.task06.strategy;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface DynamoDbEventStrategy {
    static final String VALUE = "value";
    static final String KEY = "key";
    void processRecord(DynamodbEvent.DynamodbStreamRecord record);

    boolean isApplicable(String eventName);

    default String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        return LocalDateTime.now().format(formatter);
    }

}
