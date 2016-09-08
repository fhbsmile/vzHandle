package com.tsystems.si.aviation.imf.vzHandle.db.service;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.ComFlight;



public interface  ComFlightServiceI {
   
	public void saveOrUpdate(ComFlight comFlight);
	
	public void delete(ComFlight comFlight);
	public int deleteComFlightByOwner(String owner);
}
