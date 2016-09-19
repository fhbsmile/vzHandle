package com.tsystems.si.aviation.imf.vzHandle.handles.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRemoveEmptyTransformer {
	private static final Logger     logger               = LoggerFactory.getLogger(TestRemoveEmptyTransformer.class);


	@Test
	public void testXmlFilter(){
		File telx = new File("file/f.xml");
		//File telx = new File("file/AFTN_20150114223713_006707.txt");
		//File telx = new File("file/org.xml");
		String badstring=null;
		try {
			badstring = FileUtils.readFileToString(telx, "utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nn = RemoveEmptyTransformer.xmlFilter(badstring);
		logger.info("result:{}",nn);
	}
}
