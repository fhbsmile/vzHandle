package com.tsystems.si.aviation.imf.vzHandle.db.service;

import java.util.List;
import java.util.Map;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.Flight;



public interface  FlightServiceI {
   
	 public void saveOrUpdate(Flight flight);
	 public void saveOrUpdate(Flight flight,boolean overWriteFuid);
	 public void delete(Flight flight);
	 public int updateFuid(Flight flight,String fuid);
	 public Flight getFlightById(int id);
	 public List<Flight> getFlightByFuid(String fuid);
	 public List<Flight> getFlightByCallsign(Map<String, Object> params);
     public List<Flight> getFlightByFlightNumberWithDateTimePeriod(Map<String, Object> params);
     public List<Flight> getFlightByFlightNumberWithDate(Map<String, Object> params);
     public List<Flight> getFlightByThreePrimaryKeys(Map<String, Object> params);
}
