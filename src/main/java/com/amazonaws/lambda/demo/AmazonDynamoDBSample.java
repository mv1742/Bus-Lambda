package com.amazonaws.lambda.demo;
import java.util.ArrayList;
/*
 * Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.geo.model.PutPointResult;
import com.amazonaws.geo.model.QueryRadiusRequest;
import com.amazonaws.geo.model.QueryRadiusResult;
import com.amazonaws.geo.model.QueryRectangleRequest;
import com.amazonaws.geo.model.QueryRectangleResult;
import com.amazonaws.geo.util.GeoTableUtil;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class AmazonDynamoDBSample {

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (C:\\Users\\rewak\\.aws\\credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    static AmazonDynamoDB dynamoDB;


    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */

    public static void main(String[] args) throws Exception {
    	String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
		String secretKey = System.getenv("AWS_SECRET_KEY");
		String tableName = "DEVICE";
		String regionName = "us-east-2";

        try {
            
        	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    		AmazonDynamoDBClient ddb = new AmazonDynamoDBClient(credentials);
    		Region region = Region.getRegion(Regions.fromName(regionName));
    		ddb.setRegion(region);
            GeoDataManagerConfiguration config = new GeoDataManagerConfiguration(ddb, tableName).withRangeKeyAttributeName("DeviceId");
            GeoDataManager geoDataManager = new GeoDataManager(config);
            List<KeySchemaElement> keySchema = new ArrayList<>();
            keySchema.add(new KeySchemaElement("hashKey", KeyType.HASH));
            keySchema.add(new KeySchemaElement("DeviceId", KeyType.RANGE));
            
            List<AttributeDefinition> attributeDefinition = new ArrayList<>();
            attributeDefinition.add(new AttributeDefinition("hashKey", ScalarAttributeType.N));
            attributeDefinition.add(new AttributeDefinition("DeviceId", ScalarAttributeType.S));
            attributeDefinition.add(new AttributeDefinition("geohash", ScalarAttributeType.N));
            CreateTableRequest createTableRequest = GeoTableUtil.getCreateTableRequest(config).withKeySchema(keySchema)
            		.withAttributeDefinitions(attributeDefinition);
            // Create a table with a primary hash key named 'TIMESTAMP', which holds a string
//            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
//                .withKeySchema(new KeySchemaElement().withAttributeName("TIMESTAMP").withKeyType(KeyType.HASH))
//                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("TIMESTAMP").withAttributeType(ScalarAttributeType.S))
//                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L));

            // Create table if it does not exist yet
            TableUtils.createTableIfNotExists(ddb, createTableRequest);
            // wait for the table to move into ACTIVE state
            TableUtils.waitUntilActive(ddb, tableName);

            // Describe our new table
            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
            TableDescription tableDescription = ddb.describeTable(describeTableRequest).getTable();
            System.out.println("Table Description: " + tableDescription);

            // Add an item
            newItem(geoDataManager,"1", "2", 47.61121, -122.3350);

            // Add another item
            newItem(geoDataManager,"2", "1", 68.61121, -112.31846);;

            // Scan items
            GeoPoint centerPoint = new GeoPoint(47.6205, -122.3492);  
            QueryRadiusRequest queryRadiusRequest = new QueryRadiusRequest(centerPoint, 2500);  
            QueryRadiusResult queryRadiusResult = geoDataManager.queryRadius(queryRadiusRequest);     
            for (Map<String, AttributeValue> item : queryRadiusResult.getItem()) 
            {      
            	System.out.println("item: " + item);  
            }
            
//            GeoPoint minPoint = new GeoPoint(45.5, -120.3);  
//            GeoPoint maxPoint = new GeoPoint(49.5, -124.3);  
//            QueryRectangleRequest queryRectangleRequest = new QueryRectangleRequest(minPoint, maxPoint);  
//            QueryRectangleResult queryRectangleResult = geoDataManager.queryRectangle(queryRectangleRequest);     
//            for (Map<String, AttributeValue> item : queryRectangleResult.getItem()) 
//            {      
//            	System.out.println("item: " + item);  
//            }  
//            HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//            Condition condition = new Condition()
//                .withComparisonOperator(ComparisonOperator.GT.toString())
//                .withAttributeValueList(new AttributeValue().withN("1"));
//            scanFilter.put("DeviceId", condition);
//            ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
//            ScanResult scanResult = dynamoDB.scan(scanRequest);
//            System.out.println("Result: " + scanResult);

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void newItem(GeoDataManager geoDataManager, String timestamp, String deviceId, double latitude, double longitude) {
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
        AttributeValue rangeKeyValue = new AttributeValue().withS(deviceId);
        PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyValue);
        
        putPointRequest.getPutItemRequest().addItemEntry("TIMESTAMP", new AttributeValue(timestamp));
        //putPointRequest.getPutItemRequest().addItemEntry("DeviceId", new AttributeValue().withN(Integer.toString(deviceId)));
        PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);
        PutItemResult putItemResult = putPointResult.getPutItemResult();
        System.out.println("Result: " + putItemResult);
//        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//        item.put("TIMESTAMP", new AttributeValue(timestamp));
//        item.put("DeviceId", new AttributeValue().withN(Integer.toString(deviceId)));
//        item.put("location", new AttributeValue(location));
    }

}
