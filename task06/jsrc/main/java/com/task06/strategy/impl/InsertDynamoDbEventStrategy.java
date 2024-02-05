package com.task06.strategy.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.task06.entity.AuditCreatedRecord;
import com.task06.strategy.DynamoDbEventStrategy;

import java.util.Map;
import java.util.UUID;

public class InsertDynamoDbEventStrategy implements DynamoDbEventStrategy {
    private final AmazonDynamoDB amazonDynamoDB;

    public InsertDynamoDbEventStrategy(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @Override
    public void processRecord(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> image = record.getDynamodb().getNewImage();
        AuditCreatedRecord auditCreatedRecord = new AuditCreatedRecord();
        auditCreatedRecord.setId(UUID.randomUUID().toString());
        auditCreatedRecord.setItemKey(image.get(KEY).getS());
        auditCreatedRecord.setModificationTime(getCurrentTime());
        auditCreatedRecord.setNewValue(new AuditCreatedRecord.AuditNewValueRecord(image.get(KEY).getS(), image.get(VALUE).getS()));

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(auditCreatedRecord);
    }

    @Override
    public boolean isApplicable(String eventName) {
        return "INSERT".equals(eventName);
    }
}
