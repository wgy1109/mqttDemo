package com.sound.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
//import org.junit.Test;

public class Dom4JXml {
	
	
//	@Test
    public void test(){
        Long start = System.currentTimeMillis();
        String str="<?xml version=\"1.0\" encoding=\"utf-8\"?>" + 
    			"<project>" + 
    			"<version>15319</version>" + 
    			"<sensor>" + 
    			"<hearttime>6</hearttime>" + 
    			"<increment>0.21</increment>" + 
    			"<max>40</max>" + 
    			"<min>0</min>" + 
    			"<scenario/>" + 
    			"<sendtype>thres22hold</sendtype>" + 
    			"<sensoraddr>00281</sensoraddr>" + 
    			"<sensordatatype>number</sensordatatype>" + 
    			"<sensorid>1</sensorid>" + 
    			"<sensorname>tem</sensorname>" + 
    			"<sensorshowname/>" + 
    			"<sensortype>dis</sensortype>" + 
    			"<valuemax>100</valuemax>" + 
    			"<valuemin>22</valuemin>" + 
    			"</sensor>" + 
    			"</project>";
        String fileurl = "D:/test/rss3.xml";
        try {
			strChangeXML(str,fileurl);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("运行时间："+ (System.currentTimeMillis() - start));
    }
 
	// 把String保存为xml文件  str:字符串   fileurl：文件保存地址
	public static void strChangeXML(String str, String fileurl) throws Exception {
		SAXReader saxReader = new SAXReader();		
		Document document;		
			document = saxReader.read(new ByteArrayInputStream(str.getBytes("UTF-8")));			
			OutputFormat format = OutputFormat.createPrettyPrint();			
			/** 将document中的内容写入文件中 */			
			XMLWriter writer = new XMLWriter(new FileWriter(new File(fileurl)),format);
			writer.write(document);			
			writer.close();		
	}

}
