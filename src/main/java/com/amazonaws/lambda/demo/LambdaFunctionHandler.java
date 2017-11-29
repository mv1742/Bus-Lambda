package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.QueryRadiusRequest;
import com.amazonaws.geo.model.QueryRadiusResult;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<RequestClass, ResponseClass> {
	
	String accessKey = "your access key";
	String secretKey = "Your secret key";
	String tableName = "DEVICE";
	String regionName = "us-east-2";
    @Override
    public ResponseClass handleRequest(RequestClass request, Context context) {
    	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonDynamoDBClient ddb = new AmazonDynamoDBClient(credentials);
		Region region = Region.getRegion(Regions.fromName(regionName));
		ddb.setRegion(region);
        GeoDataManagerConfiguration config = new GeoDataManagerConfiguration(ddb, tableName);
        GeoDataManager geoDataManager = new GeoDataManager(config);
        double latitude = Double.parseDouble(request.latitude);
        double longitude = Double.parseDouble(request.longitude);
        GeoPoint centerPoint = new GeoPoint(latitude, longitude);  
        QueryRadiusRequest queryRadiusRequest = new QueryRadiusRequest(centerPoint, 2500);  
        QueryRadiusResult queryRadiusResult = geoDataManager.queryRadius(queryRadiusRequest); 
        
        List<String> output = new ArrayList<>();
        for (Map<String, AttributeValue> item : queryRadiusResult.getItem()) 
        {      
        	AttributeValue attrLoc = item.get("geoJson");
        	String[] location = attrLoc.getS().split(":")[2].split(",");
        	String lat = location[0].substring(1);
        	String longi = location[1].substring(0, location[1].length()-2);
        	
        	AttributeValue attrDevId = item.get("DeviceId");
        	int deviceId = Integer.parseInt(attrDevId.getN());
        	
        	System.out.println("Location: "+lat+", "+longi+"\nDeviceId: "+deviceId);
        	output.add("Location: "+lat+", "+longi+"\nDeviceId: "+deviceId);
        	
        }
        return new ResponseClass(output);
    }
}