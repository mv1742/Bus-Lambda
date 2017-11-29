package com.amazonaws.lambda.demo;

import java.util.List;

public class ResponseClass {
    List<String> output;
    String finalOutput;

	public List<String> getOutput() {
		return output;
	}

	public void setOutput(List<String> output) {
		this.output = output;
	}

	public ResponseClass(List<String> output) {
		for(String out : output){
			finalOutput+="\n"+out;
		}
    }

    public ResponseClass() {
    }

}