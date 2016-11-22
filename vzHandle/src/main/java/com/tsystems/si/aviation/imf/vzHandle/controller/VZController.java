/** 
 * Project Name:vzHandle 
 * File Name:FlightController.java 
 * Package Name:com.tsystems.si.aviation.imf.vzHandle 
 * Date:2016年8月3日下午9:46:13
 * version:v1.0
 * Copyright (c) 2016, Bolo.Fang@t-systems.com All Rights Reserved. 
 * 
 */ 


package com.tsystems.si.aviation.imf.vzHandle.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.Flight;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.OrgMessage;
import com.tsystems.si.aviation.imf.vzHandle.db.service.FlightServiceI;
import com.tsystems.si.aviation.imf.vzHandle.db.service.OrgMessageServiceI;
import com.tsystems.si.aviation.imf.vzHandle.handles.tools.TelexConstant;
import com.tsystems.si.aviation.imf.vzHandle.handles.tools.VzFlightMessageOperator;



@Controller
public class VZController {
	private static final Logger     logger               = LoggerFactory.getLogger(VZController.class);
	
	@Autowired
	private FlightServiceI flightService;
	@Autowired
	private VzFlightMessageOperator vzFlightMessageOperator;
	@Autowired
	private OrgMessageServiceI orgMessageServiceI;
	//@RequestMapping(value="/putFlight",produces={MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/putFlight")
	public void putFlight(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("----------FlightController----------- ");
		String name = request.getParameter("name");
		logger.info("name>>>>:{}",name);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		try {
			response.getWriter().write("success");
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return "Hello world";  
	}
	
	@RequestMapping(value="/indexTest")
	public String index(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("----------FlightController----------- ");
		return "index";
	}
	
	@RequestMapping(value="/info/{id}")
	@ResponseBody
	public Flight getFlight(@PathVariable int id)
	{
		logger.info("----------FlightController----------- ");
		logger.info("id>>>>:{}",id);
		Flight f= flightService.getFlightById(id);
		return f;
	
	}
	
	@RequestMapping(value="/GetFlight2" , method = RequestMethod.POST)
	public void getFlight2(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("----------FlightController----------- ");
		logger.info("ContentLength:{}",request.getContentLength());
		logger.info("ContentType:{}",request.getContentType());
		//String requestJson = JSON.toJSONString(request,SerializerFeature.WriteDateUseDateFormat);
		//logger.info("request:{}",requestJson);
/*		Map<String, String[]> hm = request.getParameterMap();
		String hmJson = JSON.toJSONString(hm,SerializerFeature.WriteDateUseDateFormat);
		logger.info("hmJson:{}",hmJson);*/
		/*String flightInfo = request.getParameter("FlightInfo");
		logger.info("Received message>>>>>>>>>>>>>>>>>>，Msg:{}",flightInfo);*/

		try {		
			request.setCharacterEncoding("UTF-8"); 

			List<String> ls = new ArrayList<String>();
			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
			{
				ls.add(e.nextElement());
			}

			
/*			HashMap<String, String[]> hm = (HashMap<String, String[]>) request.getParameterMap();
			
			for (Entry<String, String[]> entry : hm.entrySet()) {
				   String paramaterName = entry.getKey();
				   String[] paramaterValue = entry.getValue();
				   logger.info("paramaterName:{} ",new Object[]{paramaterName});
					 for(int i=0;i<paramaterValue.length;i++){
						 logger.info("  paramaterValue:{} ",paramaterValue[i]);
					 }
				  }
			*/
/*			            InputStream is= null;   
			            String contentStr =null;
			             is = request.getInputStream();         
			             contentStr= IOUtils.toString(is, "utf-8");      */

			
/*            BufferedReader br = new BufferedReader(new InputStreamReader(  
                    (ServletInputStream) request.getInputStream(), "utf-8"));  
            StringBuffer sb = new StringBuffer("");  
            String temp;  
            while ((temp = br.readLine()) != null) {  
                sb.append(temp);  
            }  
            br.close();  */
/*	        ServletInputStream stream = request.getInputStream();
	        byte datas[] = new byte[request.getContentLength()];
	        int totalBytes = 0;
	        while (totalBytes < datas.length) {
	            int bytes = stream.read(datas, totalBytes, datas.length);
	            totalBytes += bytes;
	        }
	        
			String flightInfo = new String(datas, "UTF-8");
			flightInfo = URLDecoder.decode(flightInfo, "UTF-8");*/

            
          // String  flightInfo =JSON.toJSONString(contentStr,SerializerFeature.WriteDateUseDateFormat);

		    if(!ls.isEmpty()){
		    	String flightInfo = ls.get(0);
		    	logger.info("Received message>>>>>>>>>>>>>>>>>>Msg:{}",flightInfo);	
				JSONObject jb = JSON.parseObject(flightInfo);
				String depStop = jb.getString("FlightDepcode");
				String arrStop = jb.getString("FlightArrcode");
				String direction = getDrection(depStop,arrStop);
				if(direction==null){
					logger.info("Ignored,No BaseStop, {}-{}",new Object[]{depStop,arrStop});
				}else{
					vzFlightMessageOperator.process(flightInfo);
				}
		    }else{
		    	logger.info("Received message is null");	
		    }
			
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-type", "text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.write("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

	
	}
	@RequestMapping(value="/GetFlight" , method = RequestMethod.POST)
	public void getFlight3(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("----------FlightController----------- ");
		logger.info("ContentLength:{}",request.getContentLength());
		logger.info("ContentType:{}",request.getContentType());

		try {		
			request.setCharacterEncoding("UTF-8"); 

			List<String> ls = new ArrayList<String>();
			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
			{
				ls.add(e.nextElement());
			}
		    if(!ls.isEmpty()){
		    	String flightInfo = ls.get(0);
		    	logger.info("Received message>>>>>>>>>>>>>>>>>>Msg:{}",flightInfo);	
				JSONObject jb = JSON.parseObject(flightInfo);
				String depStop = jb.getString("FlightDepcode");
				String arrStop = jb.getString("FlightArrcode");
				String direction = getDrection(depStop,arrStop);
				if(direction==null){
					logger.info("Ignored,No BaseStop, {}-{}",new Object[]{depStop,arrStop});
				}else{
					OrgMessage orgMessage = new OrgMessage();
					orgMessage.setContent(flightInfo);
					orgMessage.setCreateDateTime(new Date());
					orgMessage.setOwner("VZ");
					orgMessage.setStatus("U");
					orgMessage.setType("VZDY");
					orgMessageServiceI.saveOrUpdate(orgMessage);
					logger.info("Save message to DataBase success.");	
				}
		    }else{
		    	logger.info("Received message is null");	
		    }
			
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-type", "text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.write("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

	
	}
	public String getDrection(String depatureStopIATA,String arrivalStopIATA){
		String direction=null;
		if(depatureStopIATA==null || arrivalStopIATA==null){
			logger.error("Has null Stop ignore message");
		}else{
			if(depatureStopIATA.equalsIgnoreCase(TelexConstant.default_baseAirportIATA)){
				direction ="D";
			}else if(arrivalStopIATA.equalsIgnoreCase(TelexConstant.default_baseAirportIATA)){
				direction ="A";
			}else{
				logger.error("No Base Stop.");
			}
		}
		
		return direction;
	}
}
