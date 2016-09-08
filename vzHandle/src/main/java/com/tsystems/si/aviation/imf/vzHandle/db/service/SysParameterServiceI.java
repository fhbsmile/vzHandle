package com.tsystems.si.aviation.imf.vzHandle.db.service;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.SysParameter;



public interface  SysParameterServiceI {
   
	public void saveOrUpdate(SysParameter sysParameter);
	
	public void deleteSysParameterByName(String spName);
	
	public SysParameter querySysParameterByName(String spName);
}
