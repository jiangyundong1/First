package com.wamdy.coder;

import java.io.IOException;

public class TestMain {
	public static void main(String[] args) throws IOException {
		String URL="jdbc:oracle:thin:@10.185.16.203:1531:TEST";
	    String USER="apps";
	    String PASSWORD="apps";
	    String TABLE_NAME = "PJI_TIME_MV";
	    String TABLE_CHINESE_NAME = "资产";
	    String MODEL_NAME = "TimeMv";
	    String PACKAGE_NAME_PREFIX  = "cn.cmschina.ibp.client.client";
	    
		CreateModelXml createModelXml = new CreateModelXml();
		CodeMaker codeMaker = new CodeMaker();
		createModelXml.createModelXml(TABLE_NAME, TABLE_CHINESE_NAME, MODEL_NAME, PACKAGE_NAME_PREFIX, URL, USER, PASSWORD);
		
		codeMaker.createFreeMarkerModel();
		
	}
}
