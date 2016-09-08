package com.tsystems.si.aviation.imf.vzHandle.db.service;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.RefAirport;


public interface  RefAirportServiceI {
   
	public void saveOrUpdate(RefAirport refAirport);
	
	public void delete(RefAirport refAirport);
	
	
    public int getFlyTimeByAirportICAO(String airportIcaocode);
    public int getFlyTimeByAirportIATA(String airportIatacode);
    public String getAirportIATAByICAO(String airportIcaocode);
}
