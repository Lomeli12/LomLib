package net.lomeli.lomlib.util;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class UpdateHelper 
{
	private static boolean isUpdated;
	private static String downloadURL;
	
	public UpdateHelper()
	{
		isUpdated = true;
	}
	
	public boolean isUpdate()
	{
		return isUpdated;
	}
	
	public String getDownloadURL()
	{
		return downloadURL;
	}
	
	public void check(String modname, String updateUrl, int major, int minor, int revision)
	{
		checkForUpdates(modname, updateUrl, major, minor, revision);
	}
	
	private void checkForUpdates(String modname, String updateURL, int major, int minor, int revision)
	{
		Object[] xml = { praseXML(updateURL, "majorBuildNumber"), praseXML(updateURL, "minorBuildNumber"),
		           praseXML(updateURL, "revisionBuildNumber") };
		int[] latestVersion = { new Integer(xml[0].toString()), 
	            new Integer(xml[1].toString()), new Integer(xml[2].toString()) };
		int[] currentVersion = { major, minor, revision };
		
		for(int i = 0; i < 3; i++)
		{
			if(latestVersion[i] > currentVersion[i])
			{
				isUpdated = false;
				downloadURL = String.valueOf(praseXML(updateURL, "modURL"));
				System.out.println("A new version of " + modname + " can be downloaded at " + downloadURL);
			}
		}
	}
	
	private Object praseXML(String URLLoc, String nodeName)
	{
        Object var1 = new Object();
        try
        {
            URL xmlURL = new URL(URLLoc);
            InputStream xml = xmlURL.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);
            
            doc.getDocumentElement().normalize();
            
            NodeList node = doc.getElementsByTagName(nodeName);
            
            var1 = node.item(0).getTextContent();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return var1;
	}
}
