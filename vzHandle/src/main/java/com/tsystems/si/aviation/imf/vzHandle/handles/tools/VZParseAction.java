/** 
 * Project Name:vzHandle 
 * File Name:VZParseAction.java 
 * Package Name:com.tsystems.si.aviation.imf.vzHandle.handles.tools 
 * Date:2016年11月22日下午8:12:11
 * version:v1.0
 * Copyright (c) 2016, Bolo.Fang@t-systems.com All Rights Reserved. 
 * 
 */ 


package com.tsystems.si.aviation.imf.vzHandle.handles.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.OrgMessage;
import com.tsystems.si.aviation.imf.vzHandle.db.service.OrgMessageServiceI;

public class VZParseAction {
private static final Logger     logger               = LoggerFactory.getLogger(VZParseAction.class);

	@Autowired
	private OrgMessageServiceI orgMessageServiceI;
	@Autowired
	private VzFlightMessageOperator2 vzFlightMessageOperator2;
	
	public void parse(){
		Calendar cnow = Calendar.getInstance();
		cnow.add(Calendar.HOUR, -12);
		 Date creatDateTimeAfter = cnow.getTime();
		 List<OrgMessage> orgMessages =orgMessageServiceI.findOrgMessageByStatusAndOwerAndCreateDateTimeAfter("U","VZ",creatDateTimeAfter);
		 logger.info("UnParsed  OrgMessage Count:[{}]",new Object[]{orgMessages.size()});
		 for(OrgMessage orgMessage:orgMessages){
			 vzFlightMessageOperator2.process(orgMessage);
		 }
	}
}
