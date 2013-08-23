package net.lomeli.lomlib.util;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLUtil {
	public static Object praseXML(String URLLoc, String nodeName){
        Object var1 = new Object();
        try{
            URL xmlURL = new URL(URLLoc);
            InputStream xml = xmlURL.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);
            
            doc.getDocumentElement().normalize();
            
            NodeList node = doc.getElementsByTagName(nodeName);
            try{
            	var1 = node.item(0).getTextContent();
            }catch(NullPointerException b){}
        }
        catch(Exception e){}
        return var1;
	}
	
	public static String getString(String URLLoc, String nodeName){
		return praseXML(URLLoc, nodeName).toString();
	}
	
	public static String getStringValue(String URLLoc, String nodeName){
		return String.valueOf(praseXML(URLLoc, nodeName));
	}
	
	public static URL getURL(String URLLoc, String nodeName){
		URL ur = null;
		try{
			ur = new URL(getString(URLLoc, nodeName));
		}catch(Exception e){}
		return ur != null ? ur : null;
	}
	
	public static short getShort(String URLLoc, String nodeName){
		return new Short(getString(URLLoc, nodeName));
	}
	
	public static boolean getBoolean(String URLLoc, String nodeName){
		return new Boolean(getString(URLLoc, nodeName));
	}
	
	public static int getInteger(String URLLoc, String nodeName){
		return new Integer(getString(URLLoc, nodeName));
	}
	
	public static double getDouble(String URLLoc, String nodeName){
		return new Double(getString(URLLoc, nodeName));
	}
	
	public static float getFloat(String URLLoc, String nodeName){
		return new Float(getString(URLLoc, nodeName));
	}
	
	public static long getLong(String URLLoc, String nodeName){
		return new Long(getString(URLLoc, nodeName));
	}
	
	public static byte getByte(String URLLoc, String nodeName){
		return new Byte(getString(URLLoc, nodeName));
	}
}
