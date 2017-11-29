package com.amazonaws.lambda.demo;

public class RequestClass {
    String latitude;
    String longitude;
    public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	

    public RequestClass(String latitude, String longitude) {
    	this.latitude = latitude;
        this.longitude = longitude;
    }

    public RequestClass() {
    }
}
