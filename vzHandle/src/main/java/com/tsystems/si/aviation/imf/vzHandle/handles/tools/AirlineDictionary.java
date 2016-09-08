package com.tsystems.si.aviation.imf.vzHandle.handles.tools;

import java.util.HashMap;

public class AirlineDictionary
{
  private HashMap<String, String> airlineMap = new HashMap();
  private String huAirlines = "HU,GS,JD,GX,8L,9H,PN,FU,HX,UQ";

  public HashMap<String, String> getAirlineMap() {
    return this.airlineMap;
  }

  public void setAirlineMap(HashMap<String, String> airlineMap) {
    this.airlineMap = airlineMap;
  }

  public String getHuAirlines() {
	return huAirlines;
}

public void setHuAirlines(String huAirlines) {
	this.huAirlines = huAirlines;
}

public String getIATAByICAO(String icao) {
	  String res = this.airlineMap.get(icao);

    return res;
  }
}