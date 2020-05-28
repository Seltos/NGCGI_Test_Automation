package com.enquero.automationFramework.apiUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlHandler {

    public File updateXml(String location, String updateSequence, List<String> updates) throws Exception {

        String[] updateSequences = updateSequence.split(",");
        File file = new File(location);

        System.out.println(location);

        // When no updation
        if(updates.size()==0||updateSequences.length==0) {
            return file;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {
            Document doc = dbFactory.newDocumentBuilder().parse(file);
            NodeList nodes = doc.getElementsByTagName(updateSequence);
            for(int i=0; i<nodes.getLength(); i++){
                Node tempNode = nodes.item(i).getFirstChild();
                tempNode.setNodeValue(updates.get(i));
            }

            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File tempFile = File.createTempFile("updatedXml", ".xml", tempDir);
            StreamResult result = new StreamResult(tempFile);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            return tempFile;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }


    }

}
