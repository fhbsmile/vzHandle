package com.tsystems.si.aviation.imf.vzHandle.db.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.si.aviation.imf.vzHandle.db.bean.ImfMessage;
import com.tsystems.si.aviation.imf.vzHandle.db.bean.OrgMessage;
import com.tsystems.si.aviation.imf.vzHandle.db.dao.OrgMessageDaoI;
import com.tsystems.si.aviation.imf.vzHandle.db.service.OrgMessageServiceI;


@Service
public class OrgMessageServiceImpl implements OrgMessageServiceI {
	private static final Logger     logger               = LoggerFactory.getLogger(OrgMessageServiceImpl.class);
	@Autowired
    private OrgMessageDaoI orgMessageDao;
	@Override
	public void saveOrUpdate(OrgMessage orgMessage) {
		// TODO Auto-generated method stub
		orgMessageDao.saveOrUpdate(orgMessage);
		}
	@Override
	public List<OrgMessage> findOrgMessageByStatusAndOwer(String status, String owner) {
		// TODO Auto-generated method stub
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("status", status);
		params.put("owner", owner);
		String sql = "from OrgMessage f where f.status = :status and f.owner = :owner order by f.id asc";
		List<OrgMessage> orgMessages= orgMessageDao.find(sql, params);
		return orgMessages;
	}
	}



