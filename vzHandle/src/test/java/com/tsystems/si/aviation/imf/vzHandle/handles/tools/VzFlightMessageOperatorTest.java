/** 
 * Project Name:vzHandle 
 * File Name:VzFlightMessageOperatorTest.java 
 * Package Name:com.tsystems.si.aviation.imf.vzHandle.handles.tools 
 * Date:2016年8月22日上午9:10:25
 * version:v1.0
 * Copyright (c) 2016, Bolo.Fang@t-systems.com All Rights Reserved. 
 * 
 */ 


package com.tsystems.si.aviation.imf.vzHandle.handles.tools;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext_test.xml")
public class VzFlightMessageOperatorTest {
	private static final Logger     logger               = LoggerFactory.getLogger(VzFlightMessageOperatorTest.class);
	
	@Autowired
	private VzFlightMessageOperator vzFlightMessageOperator;
	
	
	String jsonString =null;
	
	@Before
	public void setUp() throws Exception {
		
		//File text = new File("file/dynamicExample.json");
		File text = new File("file/HU7047_D.json");
		jsonString = FileUtils.readFileToString(text, "utf-8");
	}	
	@Test
	public void testProcess(){
		vzFlightMessageOperator.process(jsonString);
	}
}
