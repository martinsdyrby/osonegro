package com.molamil.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.molamil.osonegro.utils.Logger;

public class LocalizedStringUtil
{
	private static Map<String, String> stringData;
	
	public LocalizedStringUtil() {}
/*
	public static String getLocalizedString(String key)
	{
		//external file data
		if(stringData != null)
		{
			if(stringData.containsKey(key))
			{
				//parsing adds escape chars...
				String result = stringData.get(key).replace("\\n", "\n").replace("\\t", "\t");
				return result;
			}
		}

		//internal resource data fallback
		String packageName = Session.getInstance().getContext().getPackageName();
	    int resId = Session.getInstance().getContext().getResources().getIdentifier(key, "string", packageName);
	    if(resId <= 0)
	    {
	    	return key;
	    }
	    try {
		    return Session.getInstance().getContext().getString(resId);
		} catch (Exception e) {
			return key;
		}
	}
*/
	/* read specific file in external storage (strings.xml) and sends to xml parsing*/
	/*
	public static void parseFile()
	{

		String filename = MainActivity.EXTERNAL_STORAGE_DIR+MainActivity.TEXT_DATA_FILENAME;
		File file = new File(filename);
	    if(!file.exists())
	    {
	    	Logger.info("no external texts available");
	    }
	    
	    //Logger.info("parse file");
	    FileInputStream fis;
		try {
			fis = new FileInputStream(file); 
			InputStreamReader isr = new InputStreamReader(fis);
		    char[] inputBuffer = new char[fis.available()];
		    isr.read(inputBuffer);
		    String data = new String(inputBuffer);
		    parseXML(data);
		    isr.close();
		    fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	*/
	
	private static void parseXML(String xml)
	{
		stringData = new HashMap<String, String>();
		
		XMLParser parser = new XMLParser();
		Document doc = parser.getDomElement(xml);
		NodeList nl = doc.getElementsByTagName("string");
		     
		for (int i = 0; i < nl.getLength(); i++)
		{
		    Node node = nl.item(i);
		    Element e = (Element) node;
		    String key = e.getAttribute("name");
		    String value = "";
		    if(e.getChildNodes().getLength() > 0)
		    {
		    	value = e.getChildNodes().item(0).getNodeValue();
		    }
		    
		    stringData.put(key, value);
		}
	}
}
