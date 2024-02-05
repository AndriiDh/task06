package com.task06;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.syndicate.deployment.annotations.events.DynamoDbTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.task06.dto.Response;
import com.task06.strategy.DynamoDbEventStrategy;
import com.task06.strategy.impl.InsertDynamoDbEventStrategy;
import com.task06.strategy.impl.ModifyDynamoDbEventStrategy;

import java.util.ArrayList;
import java.util.List;

@LambdaHandler(lambdaName = "audit_producer", roleName = "audit_producer-role")
@DynamoDbTriggerEventSource(targetTable = "Configuration", batchSize = 1)
@DependsOn(name = "Configuration", resourceType = ResourceType.DYNAMODB_TABLE)
public class AuditProducer implements RequestHandler<DynamodbEvent, Response> {
	private final List<DynamoDbEventStrategy> strategies;

	public AuditProducer() {
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withRegion("eu-central-1")
				.build();
		strategies = new ArrayList<>();
		strategies.add(new InsertDynamoDbEventStrategy(amazonDynamoDB));
		strategies.add(new ModifyDynamoDbEventStrategy(amazonDynamoDB));
	}

	@Override
	public Response handleRequest(DynamodbEvent dynamodbEvent, Context context) {
		DynamodbEvent.DynamodbStreamRecord record = dynamodbEvent.getRecords()
				.stream().findFirst().orElseThrow(IllegalStateException::new);
		strategies.stream()
				.filter(strategy -> strategy.isApplicable(record.getEventName()))
				.findAny()
				.ifPresent(strategy -> strategy.processRecord(record));

		return new Response.Builder()
				.statusCode(201)
				.message("Success")
				.build();
	}
}
