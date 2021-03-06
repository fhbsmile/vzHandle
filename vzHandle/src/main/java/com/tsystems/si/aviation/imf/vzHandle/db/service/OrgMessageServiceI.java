package com.tsystems.si.aviation.imf.vzHandle.db.service;

import java.util.Date;
import java.util.List;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.OrgMessage;




public interface  OrgMessageServiceI {
   
	public void saveOrUpdate(OrgMessage orgMessage);
	public List<OrgMessage> findOrgMessageByStatusAndOwer(String status,String owner);
	public List<OrgMessage> findOrgMessageByStatusAndOwerAndCreateDateTimeAfter(String status, String owner,Date createDateTimeAfter);
}
