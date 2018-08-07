

package com.tsystems.si.aviation.imf.vzHandle.handles.tools;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.Flight;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.ImfMessage;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.OrgMessage;
import com.tsystems.si.aviation.imf.vzHandle.db.service.FlightServiceI;
import com.tsystems.si.aviation.imf.vzHandle.db.service.OrgMessageServiceI;
import com.tsystems.si.aviation.imf.vzHandle.handles.vz.VzHandle;
 
/**   
 * @ClassName:  VzFlightMessageOperator   
 * @Description:TODO
 * @author: Bolo.Fang@t-systems.com  
 * @date:   2016年8月17日 下午2:48:00   
 *      
 */  
 
public class VzFlightMessageOperator2 {
	private static final Logger     logger               = LoggerFactory.getLogger(VzFlightMessageOperator2.class);
	@Autowired
	private FlightServiceI flightService;
	
	@Autowired
	private OrgMessageServiceI orgMessageService;
	
	@Autowired
	private VzHandle vzHandle;
	
	@Autowired
	private StcDictionary stcDictionary;
	
	@Autowired
	private AirportDictionary airportDictionary;
	
	public void process(OrgMessage orgMessage){
		//OrgMessage orgMessage = new OrgMessage();
		Set<ImfMessage>  imfMessageSet = new HashSet<ImfMessage>();
		ImfMessage xmlMessage =null;
		String jsonString = orgMessage.getContent();
		JSONObject jb = JSON.parseObject(jsonString);
		String recordeJson =JSON.toJSONString(jb, true);
		String flightNumbert = jb.getString("FlightNo");
		String flightNumber = vzHandle.flightNumberFormat(flightNumbert);
		String depStop = airportDictionary.getNewIATAByIATA(jb.getString("FlightDepcode"));
		String arrStop = airportDictionary.getNewIATAByIATA(jb.getString("FlightArrcode"));
		String registration = jb.getString("FlightReg");
		String orgRouting = jb.getString("FlightRoute");
		String diversionAirport = jb.getString("FlightAlternatecode");
		String routing = airportDictionary.getNewIATARouting(orgRouting);
		String stc = jb.getString("FlightCla");
		String serviceType = stcDictionary.getCAACStcByIATA(stc);
		String statusCN = jb.getString("FlightState");
		String status = getEnglishFlightState(statusCN);
		String delay1 = jb.getString("delay1");
		String delay1Name = jb.getString("delay1Name");
		String delayTime = jb.getString("dur1");
		Date originalDateTime = new Date();
		/*
		 * 
		 * 计划起飞    "FlightDeptimePlanDate":"2016-08-08 10:40:00",
		 * 预计起飞   "FlightDeptimeReadyDate":"2016-08-08 10:27:00",
		 * 实际起飞   "FlightDeptimeDate":"2016-08-08 10:44:00",
		 * 
		 * 实际降落   "FlightArrtimeDate":"2016-05-12 08:03:00",
		 * 预计降落   "FlightArrtimeReadyDate":"2016-08-08 13:20:00",
		 * 计划降落     "FlightArrtimePlanDate":"2016-08-08 13:30:00",
		 * 
		 * 
		 */
		
		String depStopSchedluedTakeOffDateTimeString = jb.getString("FlightDeptimePlanDate");		
		Date depStopSchedluedTakeOffDateTime = jb.getDate("FlightDeptimePlanDate");
		
		String depStopEstimatedTakeOffDateTimeString = jb.getString("FlightDeptimeReadyDate");
		Date depStopEstimatedTakeOffDateTime = jb.getDate("FlightDeptimeReadyDate");
		
		
		String depStopActualTakeOffDateTimeString = jb.getString("FlightDeptimeDate");
		Date depStopActualTakeOffDateTime = jb.getDate("FlightDeptimeDate");
		
		String arrStopSchedluedLandingDateTimeString = jb.getString("FlightArrtimePlanDate");
		Date arrStopSchedluedLandingDateTime = jb.getDate("FlightArrtimePlanDate");
		
		String arrStopEstimatedLandingDateTimeString = jb.getString("FlightArrtimeReadyDate");
		Date arrStopEstimatedLandingDateTime = jb.getDate("FlightArrtimeReadyDate");
		
		
		String arrStopActualLandingDateTimeString = jb.getString("FlightArrtimeDate");
		Date arrStopActualLandingDateTime = jb.getDate("FlightArrtimeDate");
		
		orgMessage.setGenerateDateTime(originalDateTime);
		orgMessage.setContent(recordeJson);
		String direction = getDirection(depStop,arrStop);
		logger.info("flightNumber:{}",flightNumber);
		logger.info("stcCN:{}",stc);
		logger.info("stcCAAC:{}",serviceType);
		logger.info("Status:{}",status);
		logger.info("direction:{}",direction);
		logger.info("routing:{}",routing);
		logger.info("FlightDeptimePlanDate:{}",depStopSchedluedTakeOffDateTimeString);
		logger.info("FlightDeptimeReadyDate:{}",depStopEstimatedTakeOffDateTimeString);
		logger.info("FlightDeptimeDate:{}",depStopActualTakeOffDateTimeString);
		logger.info("FlightArrtimePlanDate:{}",arrStopSchedluedLandingDateTimeString);
		logger.info("FlightArrtimeReadyDate:{}",arrStopEstimatedLandingDateTimeString);
		logger.info("FlightArrtimeDate:{}",arrStopActualLandingDateTimeString);
		
		StringBuffer comments= new StringBuffer();
		comments.append("flightNumber:").append(flightNumber).append(System.lineSeparator());
		comments.append("stcIATA:").append(stc).append("   stcCAAC:").append(serviceType).append(System.lineSeparator());
		comments.append("Status:").append(status).append("   direction:").append(direction).append(System.lineSeparator());
		comments.append("routing:").append(routing).append(System.lineSeparator());
		comments.append("FlightDeptimePlanDate:").append(depStopSchedluedTakeOffDateTimeString).append("   FlightDeptimeReadyDate:").append(depStopEstimatedTakeOffDateTimeString).append("   FlightDeptimeDate:").append(depStopActualTakeOffDateTimeString).append(System.lineSeparator());
		comments.append("FlightArrtimePlanDate:").append(arrStopSchedluedLandingDateTimeString).append("   FlightArrtimeReadyDate:").append(arrStopEstimatedLandingDateTimeString).append("   FlightArrtimeDate:").append(arrStopActualLandingDateTimeString).append(System.lineSeparator());
		

		//arrival flight direction A
		if(direction.equalsIgnoreCase("A")){
			List<Flight> flights=getFlightsByFlightNumberWithPeriodForHU(flightNumber,"A",arrStopSchedluedLandingDateTime,vzHandle.getBeforeMin(),vzHandle.getAfterMin());
			if(flights.isEmpty() ){
				logger.info("   Create New Arrival Flight!");
				comments.append("Create New Arrival Flight").append(System.lineSeparator());
				FlightXmlBean fxbean = new FlightXmlBean();
				fxbean.setMessageSequenceID(VzHandle.sequenceGenerator.generateNextNumber());
				fxbean.setOperationMode("NEW");
				fxbean.setOriginalDateTime(originalDateTime);
				fxbean.setSendDateTime(new Date());
				fxbean.setCreateDateTime(new Date());
				fxbean.setFlightIdentity(flightNumber);
				fxbean.setFlightDirection("A");
				fxbean.setFlightScheduledDate(arrStopSchedluedLandingDateTime);
				fxbean.setFlightScheduledDateTime(arrStopSchedluedLandingDateTime);
				fxbean.setFlightServiceType(serviceType);
				fxbean.setCreateReason("VZDynamic");
				fxbean.setXmlStatus(vzHandle.getStatusN());
				fxbean.setRegistration(registration);
                String[] routingArray = routing.split("-");
                if(routingArray.length==2){
                	fxbean.setAirportIATACodeLeg1(routingArray[0]);
                }
                if(routingArray.length==3){
                	if(routingArray[1].equalsIgnoreCase("HAK")){
                		fxbean.setAirportIATACodeLeg1(routingArray[0]);
                	}else{
                		fxbean.setAirportIATACodeLeg1(routingArray[0]);
                		fxbean.setAirportIATACodeLeg2(routingArray[1]);
                	}
                }
                fxbean.setScheduledPreviousAirportDepartureDateTime(depStopSchedluedTakeOffDateTime);
                if(status.equalsIgnoreCase("SCH")){
                	logger.info("Message ststus:{},For NEW arrival flight,add nothing.",new Object[]{status});
					comments.append("For NEW arrival flight,add nothing.").append(System.lineSeparator());
                	xmlMessage =vzHandle.createImfMessage(fxbean);
				}else if(status.equalsIgnoreCase("DEP")){
					logger.info("Message ststus:{},add ATOTout:{},ELDT{}",new Object[]{status,depStopActualTakeOffDateTimeString,arrStopEstimatedLandingDateTimeString});
					comments.append("add ATOTout:").append(depStopActualTakeOffDateTimeString).append(System.lineSeparator());
					comments.append("add ELDT:").append(arrStopEstimatedLandingDateTimeString).append(System.lineSeparator());
					fxbean.setActualPreviousAirportDepartureDateTime(depStopActualTakeOffDateTime);
					fxbean.setEstimatedLandingDateTime(arrStopEstimatedLandingDateTime);
					 xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("ARR")){
					logger.info("Message ststus:{},add ALDT:{}",new Object[]{status,arrStopActualLandingDateTimeString});
					comments.append("add ALDT:").append(arrStopActualLandingDateTimeString).append(System.lineSeparator());
					fxbean.setActualLandingDateTime(arrStopActualLandingDateTime);
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DEL")){
					logger.info("Message ststus:{},add OperationStatus:{},add ELDT:{}",new Object[]{status,"DELET",arrStopEstimatedLandingDateTimeString});
					comments.append("add OperationStatus:").append("DELET").append(System.lineSeparator());
					comments.append("add ELDT:").append(arrStopEstimatedLandingDateTimeString).append(System.lineSeparator());
					fxbean.setEstimatedLandingDateTime(arrStopEstimatedLandingDateTime);					
					fxbean.setOperationStatus("DELET");
					//fxbean.setXmlStatus(vzHandle.getStatusDly());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}else if(status.equalsIgnoreCase("RTR")){
					logger.info("Message ststus:{},add nothing!",new Object[]{status});
					comments.append("add nothing!").append(System.lineSeparator());
				}else if(status.equalsIgnoreCase("DIV")){
					logger.info("Message ststus:{},add diversionAirport:{},OperationStatus:{}",new Object[]{diversionAirport,"DIVAL"});
					comments.append("add diversionAirport:").append(diversionAirport).append("  OperationStatus:DIVAL").append(System.lineSeparator());
					fxbean.setDiversionAirportIATACode(diversionAirport);
					fxbean.setOperationStatus("DIVAL");
					//fxbean.setXmlStatus(vzHandle.getStatusDly());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}else if(status.equalsIgnoreCase("CNL")){
					logger.info("Message ststus:{},add OperationStatus:{}",new Object[]{"CNCL"});
					comments.append("add OperationStatus:").append("CNCL").append(System.lineSeparator());
					fxbean.setOperationStatus("CNCL");
					//fxbean.setXmlStatus(vzHandle.getStatusCnl());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}else{
					logger.info("Message ststus:{},Unknow status, add nothing!",new Object[]{status});
					comments.append(" Unknow status, add nothing!").append(System.lineSeparator());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}
               
                logger.info("ImfMessage:\n{}",xmlMessage);
				if(xmlMessage!=null){
					xmlMessage.setOrgMessage(orgMessage);
					xmlMessage.setOwner("VZ");
					imfMessageSet.add(xmlMessage);
					orgMessage.setStatus("S");
				}else{
					orgMessage.setStatus("I");
				}
				
				
			}else{
				Flight flight = flights.get(0);
				logger.info("   Find Flight, Id:{},Flight Number:{}",new Object[]{flight.getId(),flight.getFlightNumber()});
				FlightXmlBean fxbean = new FlightXmlBean();
				fxbean.setMessageSequenceID(VzHandle.sequenceGenerator.generateNextNumber());
				fxbean.setOperationMode("MOD");
				fxbean.setOriginalDateTime(originalDateTime);
				fxbean.setSendDateTime(new Date());
				fxbean.setCreateDateTime(new Date());
				fxbean.setFlightIdentity(flight.getFlightNumber());
				fxbean.setFlightDirection("A");
				fxbean.setFlightScheduledDate(flight.getFlightScheduledDate());
				fxbean.setFlightScheduledDateTime(flight.getFlightScheduledDateTime());
				fxbean.setXmlStatus(vzHandle.getStatusN());

				if(status.equalsIgnoreCase("SCH")){
					logger.info("Message ststus:{},For MOD arrival flight,No key fields need to update.");
					comments.append("For MOD arrival flight,No key fields need to update.").append(System.lineSeparator());
				}else if(status.equalsIgnoreCase("DEP")){
					logger.info("Message ststus:{},update ATOTout:{},ELDT{}",new Object[]{status,depStopActualTakeOffDateTimeString,arrStopEstimatedLandingDateTimeString});
					comments.append("Update ATOTout:").append(depStopActualTakeOffDateTimeString).append(System.lineSeparator());
					comments.append("Update ELDT:").append(arrStopEstimatedLandingDateTimeString).append(System.lineSeparator());
					fxbean.setActualPreviousAirportDepartureDateTime(depStopActualTakeOffDateTime);
					
					if(depStopActualTakeOffDateTime!=null){
						if(arrStopEstimatedLandingDateTime!=null && arrStopEstimatedLandingDateTime.after(new Date())){
							fxbean.setEstimatedLandingDateTime(arrStopEstimatedLandingDateTime);
						}
						fxbean.setXmlStatus(vzHandle.getStatusMod());
					}else{
						fxbean.setXmlStatus("N");
					}
					
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("ARR")){
					logger.info("Message ststus:{},update ALDT:{}",new Object[]{status,arrStopActualLandingDateTimeString});
					comments.append("Update ALDT:").append(arrStopActualLandingDateTimeString).append(System.lineSeparator());
					fxbean.setActualLandingDateTime(arrStopActualLandingDateTime);
					fxbean.setXmlStatus(vzHandle.getStatusMod());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DEL")){
					logger.info("Message ststus:{},update OperationStatus:{},update ELDT:{}",new Object[]{status,"DELET",arrStopEstimatedLandingDateTimeString});
					comments.append("Update OperationStatus:").append("DELET").append(System.lineSeparator());
					comments.append("Update ELDT:").append(arrStopEstimatedLandingDateTimeString).append(System.lineSeparator());
					fxbean.setEstimatedLandingDateTime(arrStopEstimatedLandingDateTime);					
					fxbean.setOperationStatus("DELET");
					fxbean.setXmlStatus(vzHandle.getStatusN());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}else if(status.equalsIgnoreCase("RTR")){
					logger.info("Message ststus:{},Ignore Return flight!",new Object[]{status});
					comments.append("Ignore Return flight!");
				}else if(status.equalsIgnoreCase("DIV")){
					logger.info("Message ststus:{},add diversionAirport:{},OperationStatus:{}",new Object[]{diversionAirport,"DIVAL"});
					comments.append("add diversionAirport:").append(diversionAirport).append("  OperationStatus:DIVAL").append(System.lineSeparator());
					fxbean.setDiversionAirportIATACode(diversionAirport);
					fxbean.setOperationStatus("DIVAL");
					fxbean.setXmlStatus(vzHandle.getStatusN());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}else if(status.equalsIgnoreCase("CNL")){
					logger.info("Message ststus:{},update OperationStatus:{}",new Object[]{"CNCL"});
					comments.append("Update OperationStatus:").append("CNCL").append(System.lineSeparator());
					fxbean.setOperationStatus("CNCL");
					fxbean.setXmlStatus(vzHandle.getStatusN());
					xmlMessage =vzHandle.createImfMessage(fxbean);
				}else{
					logger.info("Message ststus:{},Unknow status ignored!",new Object[]{status});
					comments.append(" Unknow status ignored!");
				}
				
				logger.info("ImfMessage:\n{}",xmlMessage);
				if(xmlMessage!=null){
					xmlMessage.setOwner("VZ");
					xmlMessage.setOrgMessage(orgMessage);
					imfMessageSet.add(xmlMessage);
					orgMessage.setStatus("S");
				}else{
					orgMessage.setStatus("I");
				}
				
			}
		}
		//arrival flight direction D
		if(direction.equalsIgnoreCase("D")){
			List<Flight> flights=getFlightsByFlightNumberWithPeriodForHU(flightNumber,"D",depStopSchedluedTakeOffDateTime,vzHandle.getBeforeMin(),vzHandle.getAfterMin());
			if(flights.isEmpty() ){
				logger.info("   Create New Departure Flight!");
				
				comments.append("Create New Departure Flight");
				FlightXmlBean fxbean = new FlightXmlBean();
				fxbean.setMessageSequenceID(VzHandle.sequenceGenerator.generateNextNumber());
				fxbean.setOperationMode("NEW");
				fxbean.setOriginalDateTime(originalDateTime);
				fxbean.setSendDateTime(new Date());
				fxbean.setCreateDateTime(new Date());
				fxbean.setFlightIdentity(flightNumber);
				fxbean.setFlightDirection("D");
				fxbean.setFlightScheduledDate(depStopSchedluedTakeOffDateTime);
				fxbean.setFlightScheduledDateTime(depStopSchedluedTakeOffDateTime);
				fxbean.setFlightServiceType(serviceType);
				fxbean.setCreateReason("VZDynamic");
				fxbean.setXmlStatus(vzHandle.getStatusN());
				fxbean.setRegistration(registration);
                String[] routingArray = routing.split("-");
                if(routingArray.length==2){
                	fxbean.setAirportIATACodeLeg1(routingArray[1]);
                }
                if(routingArray.length==3){
                	if(routingArray[0].equalsIgnoreCase("HAK")){
                		fxbean.setAirportIATACodeLeg1(routingArray[1]);
                		fxbean.setAirportIATACodeLeg2(routingArray[2]);
                	}else{
                		fxbean.setAirportIATACodeLeg1(routingArray[2]);

                	}
                }
                fxbean.setScheduledNextAirportArrivalDateTime(arrStopSchedluedLandingDateTime);
                if(status.equalsIgnoreCase("SCH")){
					logger.info("Message ststus:{},For NEW departure flight,add nothing.",new Object[]{status});
					comments.append("add nothing.").append(System.lineSeparator());
					 xmlMessage =vzHandle.createImfMessage(fxbean);
				}else if(status.equalsIgnoreCase("ARR")){
					logger.info("Message ststus:{},add nextALDT:{}.",new Object[]{status,arrStopActualLandingDateTimeString});
					comments.append("add nextALDT:").append(arrStopActualLandingDateTimeString).append(System.lineSeparator());
					fxbean.setActualNextAirportArrivalDateTime(arrStopActualLandingDateTime);
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DEP")){
					logger.info("Message ststus:{},add ATOT:{}",new Object[]{status,depStopActualTakeOffDateTimeString});				
					comments.append("add ATOT:").append(depStopActualTakeOffDateTimeString).append(System.lineSeparator());
					fxbean.setActualTakeOffDateTime(depStopActualTakeOffDateTime);
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DEL")){
					logger.info("Message ststus:{},add OperationStatus:{}, add ETOT:{}",new Object[]{status,"DELET",depStopEstimatedTakeOffDateTimeString});				
					comments.append("add OperationStatus:").append("DELET").append(System.lineSeparator());
					comments.append("add ETOT:").append(depStopEstimatedTakeOffDateTimeString).append(System.lineSeparator());
					fxbean.setEstimatedTakeOffDateTime(depStopEstimatedTakeOffDateTime);					
					fxbean.setOperationStatus("DELET");
					//fxbean.setXmlStatus(vzHandle.getStatusDly());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("RTR")){
					logger.info("Message ststus:{},add nothing!",new Object[]{status});
					comments.append("add nothing!").append(System.lineSeparator());
					 
				}else if(status.equalsIgnoreCase("DIV")){
					logger.info("Message ststus:{},add nothing!",new Object[]{status});
					comments.append("add nothing!").append(System.lineSeparator());
					 
				}else if(status.equalsIgnoreCase("CNL")){
					logger.info("Message ststus:{},update OperationStatus:{}",new Object[]{status,"CNCL"});				
					comments.append("Update OperationStatus:").append("CNCL").append(System.lineSeparator());
					fxbean.setOperationStatus("CNCL");
					//fxbean.setXmlStatus(vzHandle.getStatusCnl());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					 
				}else{
					logger.info("Message ststus:{},Unknow status,add nothing!",new Object[]{status});
					comments.append(" Unknow status,add nothing!").append(System.lineSeparator());
					 xmlMessage =vzHandle.createImfMessage(fxbean);
				}
				
	                logger.info("ImfMessage:\n{}",xmlMessage);
					if(xmlMessage!=null){
						xmlMessage.setOwner("VZ");
						xmlMessage.setOrgMessage(orgMessage);
						imfMessageSet.add(xmlMessage);
						orgMessage.setStatus("S");
					}else{
						orgMessage.setStatus("I");
					}
				
			}else{
				Flight flight = flights.get(0);
				logger.info("   Find Flight, Id:{},Flight Number:{}",new Object[]{flight.getId(),flight.getFlightNumber()});
				FlightXmlBean fxbean = new FlightXmlBean();
				fxbean.setMessageSequenceID(VzHandle.sequenceGenerator.generateNextNumber());
				fxbean.setOperationMode("MOD");
				fxbean.setOriginalDateTime(originalDateTime);
				fxbean.setSendDateTime(new Date());
				fxbean.setCreateDateTime(new Date());
				fxbean.setFlightIdentity(flight.getFlightNumber());
				fxbean.setFlightDirection("D");
				fxbean.setFlightScheduledDate(flight.getFlightScheduledDate());
				fxbean.setFlightScheduledDateTime(flight.getFlightScheduledDateTime());
				fxbean.setXmlStatus(vzHandle.getStatusN());
				if(status.equalsIgnoreCase("SCH")){
					logger.info("Message ststus:{},For MOD departure flight,No key fields need to update.",new Object[]{status});
					comments.append("For MOD departure flight,No key fields need to update.").append(System.lineSeparator());
					fxbean.setXmlStatus(vzHandle.getStatusN());
				}else if(status.equalsIgnoreCase("ARR")){
					logger.info("Message ststus:{},update nextALDT:{}.",new Object[]{status,arrStopActualLandingDateTimeString});
					comments.append("update nextALDT:").append(arrStopActualLandingDateTimeString).append(System.lineSeparator());
					fxbean.setActualNextAirportArrivalDateTime(arrStopActualLandingDateTime);
					fxbean.setXmlStatus(vzHandle.getStatusMod());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DEP")){
					logger.info("Message ststus:{},update ATOT:{}",new Object[]{status,depStopActualTakeOffDateTimeString});				
					comments.append("update ATOT:").append(depStopActualTakeOffDateTimeString).append(System.lineSeparator());
					fxbean.setActualTakeOffDateTime(depStopActualTakeOffDateTime);
					fxbean.setXmlStatus(vzHandle.getStatusMod());
					if(flight.getActualGateEndDateTime()==null) {
						fxbean.setXmlStatus("N");
						logger.info("ActualGateEndDateTime is null, wouldn't send ATOT.");
						comments.append("ActualGateEndDateTime is null, wouldn't send ATOT.").append(System.lineSeparator());
					}
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DEL")){
					logger.info("Message ststus:{},update OperationStatus:{}, update ETOT:{}",new Object[]{status,"DELET",depStopEstimatedTakeOffDateTimeString});				
					comments.append("Update OperationStatus:").append("DELET").append(System.lineSeparator());
					comments.append("update ETOT:").append(depStopEstimatedTakeOffDateTimeString).append(System.lineSeparator());
					fxbean.setEstimatedTakeOffDateTime(depStopEstimatedTakeOffDateTime);					
					fxbean.setOperationStatus("DELET");
					fxbean.setXmlStatus(vzHandle.getStatusN());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("RTR")){
					logger.info("Message ststus:{},update OperationStatus:{}",new Object[]{status,"RETRN"});				
					comments.append("Update OperationStatus:").append("RETRN").append(System.lineSeparator());
					fxbean.setOperationStatus("RETRN");
					fxbean.setXmlStatus(vzHandle.getStatusN());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else if(status.equalsIgnoreCase("DIV")){
					logger.info("Message ststus:{},Ignore Diversion flight!",new Object[]{status});
					comments.append("Ignore Diversion flight!");
					
				}else if(status.equalsIgnoreCase("CNL")){
					logger.info("Message ststus:{},update OperationStatus:{}",new Object[]{status,"CNCL"});				
					comments.append("Update OperationStatus:").append("Delay").append(System.lineSeparator());
					fxbean.setOperationStatus("CNCL");
					fxbean.setXmlStatus(vzHandle.getStatusN());
					xmlMessage =vzHandle.createImfMessage(fxbean);
					
				}else{
					logger.info("Message ststus:{},Unknow status ignored!",new Object[]{status});
					comments.append(" Unknow status ignored!").append(System.lineSeparator());
				}

                logger.info("ImfMessage:\n{}",xmlMessage);
				if(xmlMessage!=null){
					xmlMessage.setOwner("VZ");
					xmlMessage.setOrgMessage(orgMessage);
					imfMessageSet.add(xmlMessage);
					orgMessage.setStatus("S");
				}else{
					orgMessage.setStatus("I");
				}
			}
			
			
		}
		//本场到本场，试飞航班，生成2个航班
		if(depStop.equalsIgnoreCase(TelexConstant.default_baseAirportIATA) && arrStop.equalsIgnoreCase(TelexConstant.default_baseAirportIATA)){
			logger.error("本场到本场的试飞航班暂未处理！！！");
		}
		orgMessage.setUpdateDateTime(new Date());
		orgMessage.setImfMessages(imfMessageSet);
		orgMessage.setComments(comments.toString());
		if(vzHandle.getHandleMode().equalsIgnoreCase("PRD")){
			orgMessageService.saveOrUpdate(orgMessage);
			logger.info("Save orgmessage into database success.");
		}else{
			logger.info("Non PRD Mode,wouldn't save.");
		}
	}
	
	public String getDirection(String depatureStopIATA,String arrivalStopIATA){
		String direction=null;
		if(depatureStopIATA==null || arrivalStopIATA==null){
			logger.error("Has null Stop ignore message");
		}else{
			if(depatureStopIATA.equalsIgnoreCase(TelexConstant.default_baseAirportIATA)){
				direction ="D";
			}else if(arrivalStopIATA.equalsIgnoreCase(TelexConstant.default_baseAirportIATA)){
				direction ="A";
			}else{
				logger.error("No Base Stop。");
			}
		}
		
		return direction;
	}
	
	public String getEnglishFlightState(String flightState){
		String state=null;
		if(flightState!=null){
			if(flightState.equals("计划")){
				state="SCH";
			}else if(flightState.equals("起飞")){
				state="DEP";
			}else if(flightState.equals("到达")){
				state="ARR";
			}else if(flightState.equals("延误")){
				state="DEL";
			}else if(flightState.equals("取消")){
				state="CNL";
			}else if(flightState.equals("备降")){
				state="DIV";
			}else if(flightState.equals("返航")){
				state="RTR";
			}else{
				state="UNKNOW";
			}
		}
		
		return state;
	}
	
	public List<Flight> getFlightsByFlightNumberWithPeriodForHU(String flightNumber,String direction,Date scheduledDateTime,int beforeMin,int afterMin){
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("flightNumber", flightNumber);
		params.put("direction", direction);
		Map<String, Date> period = TimeHandle.getFlightDateTimePeriod(scheduledDateTime, beforeMin, afterMin);
		params.putAll(period);
		logger.info("   Query Parameters:");
		logger.info("     flightNumber:{}   Direction:{}",new Object[]{params.get("flightNumber"),params.get("direction")});
		logger.info("     Period:{} ~ {}",new Object[]{TimeHandle.getDateTimeString((Date) params.get("beforeDateTime")),TimeHandle.getDateTimeString((Date) params.get("afterDateTime"))});
		List<Flight> flights = flightService.getFlightByFlightNumberWithDateTimePeriod(params);
		//add suffix N as new flightnumber
		if(flights.isEmpty()){
			logger.info("     Flight not found,try suffix A");
        		String newFlightNumber = flightNumber +"A";
        		params.put("flightNumber", newFlightNumber);
        		logger.info("     New flightNumber:{}   Direction:{}",new Object[]{params.get("flightNumber"),params.get("direction")});
        	 flights = flightService.getFlightByFlightNumberWithDateTimePeriod(params);
		}
		
		return flights;
     }

}
