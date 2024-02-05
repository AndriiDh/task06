package com.task06.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName = "cmtr-4a1bbc9a-Audit-test")
public class AuditCreatedRecord {
    private String id;
    private String itemKey;
    private String modificationTime;
    private AuditNewValueRecord newValue;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "itemKey")
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @DynamoDBAttribute(attributeName = "modificationTime")
    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    @DynamoDBAttribute(attributeName = "newValue")
    public AuditNewValueRecord getNewValue() {
        return newValue;
    }

    public void setNewValue(AuditNewValueRecord newValue) {
        this.newValue = newValue;
    }

    @DynamoDBDocument
    public static class AuditNewValueRecord implements Serializable {
        private String key;
        private String value;

        public AuditNewValueRecord(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @DynamoDBAttribute(attributeName = "key")
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @DynamoDBAttribute(attributeName = "value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
