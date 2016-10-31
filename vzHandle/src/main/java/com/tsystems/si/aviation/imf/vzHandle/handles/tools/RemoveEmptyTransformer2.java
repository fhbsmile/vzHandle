package com.tsystems.si.aviation.imf.vzHandle.handles.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveEmptyTransformer2 {
	private static final Logger     logger               = LoggerFactory.getLogger(RemoveEmptyTransformer2.class);
	
	  private  String xslName = "remove_empty_nodes.xsl";
	  private  Transformer transformer;
	  private  URL urlFilterXSL = ConfigurationUtils.locate(xslName);
	  private  String xsl = null;
	  private  Source xslSource;

	  public  String xmlFilter(String xml)
	  {
		logger.info("Orignal Xml: \n{}", xml);
	    Source xmlInput = new StreamSource(new StringReader(xml));
	    StringWriter stringWriter = new StringWriter();
	    StreamResult xmlOutput = new StreamResult(stringWriter);
	    try {
	      transformer.transform(xmlInput, xmlOutput);
	    } catch (Exception e) {
	    	logger.error(e.getMessage(), e);
	      return xml;
	    }
          String result = xmlOutput.getWriter().toString();
	    logger.info("After filter: \n{}", result);
	    return result;
	  }
	  public  String xmlFilter2(String xml)
	  {
	        String formattedOutput = "";  
	        try {  
	        	StreamSource xmlSource = new StreamSource(new ByteArrayInputStream( xml.getBytes() ) );
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            transformer.transform( xmlSource, new StreamResult( baos ) );

	            formattedOutput = baos.toString();

	        } catch (Exception e) {  
	            logger.error(e.getMessage(), e);
	        }  
	        return formattedOutput;  
	  }
	  
	  public void initTransformer(){

		    try
		    {
		      xsl = IOUtils.toString(urlFilterXSL, "utf-8");
		      logger.info("Loading  Filter XSL Success!");
		      logger.info("\n{}", xsl);
		    }
		    catch (IOException e) {
		      e.printStackTrace();
		      logger.error("Loading  Filter XSL Failed!!!,System exit!!");
		      logger.error(e.getMessage(), e);
		      System.exit(0);
		    }

		    xslSource = new StreamSource(new StringReader(xsl));
		    try {
		      transformer = TransformerFactory.newInstance().newTransformer(xslSource);
		      transformer.setOutputProperty("indent", "yes");
		    }
		    catch (TransformerConfigurationException e) {
		      e.printStackTrace();
		      System.exit(0);
		    }
		    catch (TransformerFactoryConfigurationError e) {
		      e.printStackTrace();
		      System.exit(0);
		    }
	  }

}
