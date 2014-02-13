package net.lomeli.lomlib.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.lomeli.lomlib.LomLibCore;

public class XMLUtil {

    public static Object praseXML(String URLLoc, String nodeName) {
        Object var1 = new Object();
        try {
            URL xmlURL = new URL(URLLoc);
            InputStream xml = xmlURL.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);

            doc.getDocumentElement().normalize();

            NodeList node = doc.getElementsByTagName(nodeName);
            try {
                var1 = node.item(0).getTextContent();
            }catch(NullPointerException b) {
            }
        }catch(SocketException e) {
        }catch(MalformedURLException e) {
        }catch(IOException e) {
        }catch(ParserConfigurationException e) {
        }catch(SAXException e) {
        }
        return var1;
    }

    public static String getString(String URLLoc, String nodeName) {
        return praseXML(URLLoc, nodeName).toString();
    }

    public static String getStringValue(String URLLoc, String nodeName) {
        return String.valueOf(praseXML(URLLoc, nodeName));
    }

    public static URL getURL(String URLLoc, String nodeName) {
        URL ur = null;
        try {
            ur = new URL(getString(URLLoc, nodeName));
        }catch(Exception e) {
        }
        return ur != null ? ur : null;
    }

    public static short getShort(String URLLoc, String nodeName) {
        return new Short(getString(URLLoc, nodeName));
    }

    public static boolean getBoolean(String URLLoc, String nodeName) {
        return new Boolean(getString(URLLoc, nodeName));
    }

    public static int getInteger(String URLLoc, String nodeName) {
        return new Integer(getString(URLLoc, nodeName));
    }

    public static double getDouble(String URLLoc, String nodeName) {
        return new Double(getString(URLLoc, nodeName));
    }

    public static float getFloat(String URLLoc, String nodeName) {
        return new Float(getString(URLLoc, nodeName));
    }

    public static long getLong(String URLLoc, String nodeName) {
        return new Long(getString(URLLoc, nodeName));
    }

    public static byte getByte(String URLLoc, String nodeName) {
        return new Byte(getString(URLLoc, nodeName));
    }

    private static boolean isValidXMLFile(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            File f = new File(filename);
            if(f.exists()) {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(f);
                return true;
            }
        }catch(Exception e) {
            LomLibCore.logger.logWarning("Invalid XML file!");
        }
        return true;
    }

    public static boolean isValidXMLFile(File config) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            if(config != null && config.exists()) {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(config);
                return true;
            }
        }catch(Exception e) {
            LomLibCore.logger.logWarning("Invalid configuration file!");
            if(config.exists()) {
                LomLibCore.logger.logWarning("Removing invalid file...");
                config.delete();
                LomLibCore.logger.logBasic("Done!");
            }
            return false;
        }
        return true;
    }

}
