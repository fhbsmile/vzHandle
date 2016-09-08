package com.tsystems.si.aviation.imf.vzHandle.db.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.Flight;
import com.tsystems.si.aviation.imf.vzHandle.db.dao.FlightDaoI;
import com.tsystems.si.aviation.imf.vzHandle.db.service.FlightServiceI;



@Service
public class FlightServiceImpl implements FlightServiceI {
	private static final Logger     logger               = LoggerFactory.getLogger(FlightServiceImpl.class);
	@Autowired
    private FlightDaoI flightDao;
	@Override
	public void saveOrUpdate(Flight flight) {
		// TODO Auto-generated method stub
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flightNumber", flight.getFlightNumber());
		params.put("flightScheduledDate", flight.getFlightScheduledDate());
		params.put("flightDirection", flight.getFlightDirection());
		String sql = "from Flight f where f.flightNumber = :flightNumber and f.flightScheduledDate=:flightScheduledDate and f.flightDirection =:flightDirection";
		List<Flight> flights =flightDao.find(sql, params);
		
		if(flights.isEmpty()){
			logger.info("Can not find the flight to update, insert new flight......");
			flightDao.save(flight);
			logger.info("Insert new flight success......");
		}else{
			logger.info("find the flight to update, update old flight......");
			Flight flightQ = flights.get(0);
			logger.info("Old flight :{}",flightQ.toString());
			logger.info("New flight :{}",flight.toString());
			String[] a = {"id","createtime"};
	        BeanUtils.copyProperties(flight, flightQ,a);
	        flightDao.update(flightQ);
	        logger.info("update old flight success......");
		}
	}

	@Override
	public void saveOrUpdate(Flight flight, boolean overWriteFuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flightNumber", flight.getFlightNumber());
		params.put("flightScheduledDate", flight.getFlightScheduledDate());
		params.put("flightDirection", flight.getFlightDirection());
		String sql = "from Flight f where f.flightNumber = :flightNumber and f.flightScheduledDate=:flightScheduledDate and f.flightDirection =:flightDirection";
		List<Flight> flights =flightDao.find(sql, params);
		
		if(flights.isEmpty()){
			logger.info("Can not find the flight to update, insert new flight......");
			flightDao.save(flight);
			logger.info("Insert new flight success......");
		}else{
			logger.info("find the flight to update, update old flight......");
			Flight flightQ = flights.get(0);
			logger.info("Old flight :{}",JSON.toJSONString(flightQ,SerializerFeature.WriteDateUseDateFormat));
			logger.info("New flight :{}",JSON.toJSONString(flight,SerializerFeature.WriteDateUseDateFormat));
			if(overWriteFuid){
				//覆盖旧的fuid,所以Fuid不在忽略列表中
				String[] a = {"id","createDateTime"};			
		        BeanUtils.copyProperties(flight, flightQ,a);
			}else{
				  String[] a = {"id","createDateTime","fuid"};
				  BeanUtils.copyProperties(flight, flightQ,a);
			}

	        flightDao.update(flightQ);
	        logger.info("update old flight success......");
		}
	}

	@Override
	public void delete(Flight flight) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flightNumber", flight.getFlightNumber());
		params.put("flightScheduledDate", flight.getFlightScheduledDate());
		params.put("flightDirection", flight.getFlightDirection());
		String sql = "from Flight f where f.flightNumber = :flightNumber and f.flightScheduledDate=:flightScheduledDate and f.flightDirection =:flightDirection";
		List<Flight> flights =flightDao.find(sql, params);
		if(flights.isEmpty()){
			logger.error("Can not find the flight to delete!!!! Ingnore delete message!");
		}else{
			logger.info("Flight is deleting........");
          for(Flight flightQ: flights){
        	  flightDao.delete(flightQ);
          }
          logger.info("Flight delete success........");
		}
	}

	@Override
	public List<Flight> getFlightByCallsign(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String sql = "from Flight f where f.callSign = :callsign and f.flightDirection = :direction and f.flightScheduledDateTime > :beforeDateTime and f.flightScheduledDateTime < :afterDateTime order by f.flightScheduledDateTime asc";
		List<Flight> flights= flightDao.find(sql, params);
		return flights;
	}

	@Override
	public List<Flight> getFlightByFlightNumberWithDateTimePeriod(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String sql = "from Flight f where f.flightNumber = :flightNumber and f.flightDirection = :direction and f.flightScheduledDateTime > :beforeDateTime and f.flightScheduledDateTime < :afterDateTime order by f.flightScheduledDateTime asc";
		List<Flight> flights= flightDao.find(sql, params);
		return flights;
	}
	@Override
	public List<Flight> getFlightByFlightNumberWithDate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String sql = "from Flight f where f.flightNumber = :flightNumber and f.flightDirection = :direction and f.flightScheduledDate = :flightScheduledDate order by f.flightScheduledDateTime asc";
		List<Flight> flights= flightDao.find(sql, params);
		return flights;
	}
	@Override
	public List<Flight> getFlightByThreePrimaryKeys(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String sql = "from Flight f where f.flightNumber = :flightNumber and f.flightDirection = :direction and f.flightScheduledDate=:flightScheduledDate order by f.flightScheduledDateTime asc";
		List<Flight> flights= flightDao.find(sql, params);
		return flights;
	}
	@Override
	public List<Flight> getFlightByFuid(String fuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fuid", fuid);

		String sql = "from Flight f where f.fuid = :fuid order by f.flightScheduledDateTime asc";
		List<Flight> flights =flightDao.find(sql, params);
		
		return flights;
	}

	@Override
	public int updateFuid(Flight flight, String fuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", flight.getId());
		params.put("fuid", fuid);
		String sql = "update Flight f set f.fuid=:fuid where f.id = :id";
		int n = flightDao.executeHql(sql, params);
		return n;
	}
	@Override
	public Flight getFlightById(int id) {
		// TODO Auto-generated method stub
		Flight f = flightDao.get(Flight.class, id);
		
		return f;
	}
}
