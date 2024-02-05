package com.task06.strategy.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.task06.entity.AuditUpdatedRecord;
import com.task06.strategy.DynamoDbEventStrategy;

import java.util.Map;
import java.util.UUID;

public class ModifyDynamoDbEventStrategy implements DynamoDbEventStrategy {
    private final AmazonDynamoDB amazonDynamoDB;

    public ModifyDynamoDbEventStrategy(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }
    @Override
    public void processRecord(DynamodbEvent.DynamodbStreamRecord record) {
        Map<String, AttributeValue> oldImage = record.getDynamodb().getOldImage();
        Map<String, AttributeValue> newImage = record.getDynamodb().getNewImage();

        AuditUpdatedRecord auditUpdatedRecord = new AuditUpdatedRecord();
        auditUpdatedRecord.setId(UUID.randomUUID().toString());
        auditUpdatedRecord.setItemKey(newImage.get(KEY).getS());
        auditUpdatedRecord.setUpdatedAttribute(VALUE);
        auditUpdatedRecord.setNewValue(newImage.get(VALUE).getS());
        auditUpdatedRecord.setOldValue(oldImage.get(VALUE).getS());
        auditUpdatedRecord.setModificationTime(getCurrentTime());

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(auditUpdatedRecord);
    }

    @Override
    public boolean isApplicable(String eventName) {
        return "MODIFY".equals(eventName);
    }
}
