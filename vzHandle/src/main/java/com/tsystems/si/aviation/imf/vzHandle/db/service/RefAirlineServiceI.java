package com.tsystems.si.aviation.imf.vzHandle.db.service;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.RefAirline;


public interface  RefAirlineServiceI {
   
	public void saveOrUpdate(RefAirline refAirline);
	
	public void delete(RefAirline refAirline);
	
	public String getIATAByICAO(String airlineICAO);
}
