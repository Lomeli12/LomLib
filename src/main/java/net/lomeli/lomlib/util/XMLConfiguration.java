package net.lomeli.lomlib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class XMLConfiguration {
    private File configurationFile;
    private List<String> todo1;
    private List<String> todo2;
    private List<String> todo3;
    private List<ConfigEnum> todo4;
    private boolean work;

    public XMLConfiguration(File configFile) {
        this(configFile, false);
    }

    public XMLConfiguration(File configFile, boolean useCfgExt) {
        if (useCfgExt)
            configurationFile = configFile;
        else {
            String filePath = configFile.getAbsolutePath().replace("cfg", "xml");
            configurationFile = new File(filePath);
        }
        todo1 = new ArrayList<String>();
        todo2 = new ArrayList<String>();
        todo3 = new ArrayList<String>();
        todo4 = new ArrayList<ConfigEnum>();
    }

    public void loadXml() {
        work = (!configurationFile.exists() || !XMLUtil.isValidXMLFile(configurationFile));
    }

    public void saveXML() {
        if (!todo1.isEmpty() && work) {
            configurationFile.delete();
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();

                Element rootElement = document.createElement("configuration");
                document.appendChild(rootElement);

                Element block = document.createElement(ConfigEnum.BLOCK_ID.getName());
                Element item = document.createElement(ConfigEnum.ITEM_ID.getName());
                Element general = document.createElement(ConfigEnum.GENERAL_CONFIG.getName());
                Element other = document.createElement(ConfigEnum.OTHER.getName());

                Element[] elements = { block, item, general, other };

                for (Element j : elements) {
                    rootElement.appendChild(j);
                }

                for (int i = 0; i < todo1.size(); i++) {
                    Element root = elements[todo4.get(i).loc];
                    if (root == null)
                        root = rootElement;
                    if (!todo3.get(i).isEmpty()) {
                        Comment newComment = document.createComment(todo3.get(i).toString());
                        root.appendChild(newComment);
                    }
                    Element newElement = document.createElement(todo1.get(i).toString());
                    newElement.appendChild(document.createTextNode(todo2.get(i).toString()));
                    root.appendChild(newElement);

                }

                rootElement.normalize();

                DOMSource source = new DOMSource(document);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                StreamResult result = new StreamResult(configurationFile.getAbsolutePath());
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int[] getIntArray(String nodeName, int[] defaultValue, String comment, ConfigEnum type) {
        todo1.add(nodeName);
        todo3.add(comment);
        todo4.add(type);
        if (praseLocalXMLFile(configurationFile, nodeName) == null) {
            String value = "";
            for (int i = 0; i < defaultValue.length; i++) {
                value.concat(defaultValue[i] + ";");
            }
            todo2.add(String.valueOf(value));
            work = true;
            return defaultValue;
        }
        todo2.add(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
        String value = String.valueOf(praseLocalXMLFile(configurationFile, nodeName));
        String[] sArray = value.split(";");
        int[] finalArray = new int[sArray.length];
        for (int i = 0; i < sArray.length; i++) {
            finalArray[i] = Integer.parseInt(sArray[i]);
        }
        return finalArray;
    }

    public int getInt(String nodeName, int defaultValue, String comment, ConfigEnum enumType) {
        todo1.add(nodeName);
        todo3.add(comment);
        todo4.add(enumType);
        if (praseLocalXMLFile(configurationFile, nodeName) == null) {
            todo2.add(String.valueOf(defaultValue));
            work = true;
            return defaultValue;
        }
        todo2.add(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
        return new Integer(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
    }

    public int getInt(String nodeName, int defaultValue, ConfigEnum enumType) {
        return getInt(nodeName, defaultValue, "", enumType);
    }

    public double getDouble(String nodeName, double defaultValue, String comment, ConfigEnum enumType) {
        todo1.add(nodeName);
        todo3.add(comment);
        todo4.add(enumType);
        if (praseLocalXMLFile(configurationFile, nodeName) == null) {
            todo2.add(String.valueOf(defaultValue));
            work = true;
            return defaultValue;
        }
        todo2.add(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
        return new Double(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
    }

    public double getDouble(String nodeName, double defaultValue, ConfigEnum enumType) {
        return getDouble(nodeName, defaultValue, "", enumType);
    }

    public boolean getBoolean(String nodeName, boolean defaultValue, String comment, ConfigEnum enumType) {
        todo1.add(nodeName);
        todo3.add(comment);
        todo4.add(enumType);
        if (praseLocalXMLFile(configurationFile, nodeName) == null) {
            todo2.add(String.valueOf(defaultValue));
            work = true;
            return defaultValue;
        }
        todo2.add(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
        return new Boolean(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
    }

    public boolean getBoolean(String nodeName, boolean defaultValue, ConfigEnum enumType) {
        return getBoolean(nodeName, defaultValue, "", enumType);
    }

    public String getString(String nodeName, String defaultValue, String comment, ConfigEnum enumType) {
        todo1.add(nodeName);
        todo3.add(comment);
        todo4.add(enumType);
        if (praseLocalXMLFile(configurationFile, nodeName) == null) {
            todo2.add(String.valueOf(defaultValue));
            work = true;
            return defaultValue;
        }
        todo2.add(String.valueOf(praseLocalXMLFile(configurationFile, nodeName)));
        return String.valueOf(praseLocalXMLFile(configurationFile, nodeName));
    }

    public String getString(String nodeName, String defaultValue, ConfigEnum enumType) {
        return getString(nodeName, defaultValue, "", enumType);
    }

    private Object praseLocalXMLFile(File xmlFile, String nodeName) {
        Object obj = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList node = doc.getElementsByTagName(nodeName);
            try {
                obj = node.item(0).getTextContent();
            } catch (NullPointerException b) {
            }
        } catch (Exception e) {
        }
        return obj;
    }

    public static enum ConfigEnum {
        BLOCK_ID("BlockIDs", 0), ITEM_ID("ItemIDs", 1), GENERAL_CONFIG("GeneralConfig", 2), OTHER("Other", 3);

        private String name;
        private int loc;

        private ConfigEnum(String name, int number) {
            this.name = name;
            this.loc = number;
        }

        String getName() {
            return this.name;
        }
    }

}
