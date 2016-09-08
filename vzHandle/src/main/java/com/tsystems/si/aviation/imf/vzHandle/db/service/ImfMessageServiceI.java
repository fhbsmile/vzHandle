package com.tsystems.si.aviation.imf.vzHandle.db.service;


import java.util.List;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.ImfMessage;



public interface  ImfMessageServiceI {
   
	public void saveOrUpdate(ImfMessage imfMessage);
	public List<ImfMessage> findImfMessageByStatus(String status);
	public List<ImfMessage> findImfMessageByStatusAndOwer(String status,String owner);
}
