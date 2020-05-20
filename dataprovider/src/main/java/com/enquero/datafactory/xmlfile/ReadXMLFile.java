package com.enquero.datafactory.xmlfile;

import com.enquero.datafactory.DataFactory.TestDataFactory;
import org.json.simple.JSONObject;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ReadXMLFile {

    private static DocumentBuilderFactory dbfactory=null;
    private static DocumentBuilder dBuilder = null;
    private static Document document=null;
    private static Element rootElement;
    public static HashMap<Object,Object> input_Parameter= new HashMap<Object,Object>();
    public static HashMap<Object,Object> validation_Parameter= new HashMap<Object,Object>();
    public static JSONObject input_Parameter1;
    public static JSONObject validation_Parameter1;

    /**
     * @param xmlFilename the path for the XML file
     * @param nodeValue the testcase name
     * @return Iterator<Object[]>
     */
    public Iterator<Object[]> getTestData(String xmlFilename, String nodeValue){
        Collection<Object[]> provider = new ArrayList<Object[]>();
    try{
        TestDataFactory xmldataFactory= new TestDataFactory();
        dbfactory=DocumentBuilderFactory.newInstance();
        dBuilder=dbfactory.newDocumentBuilder();
        document=  dBuilder.parse(new File(xmlFilename));
        document.getDocumentElement().normalize();
        rootElement= document.getDocumentElement();
        NodeList nodelist= document.getElementsByTagName("test");

        for (int i=0;i<nodelist.getLength();i++){
            Node nNode = nodelist.item(i);
            String n= nNode.getAttributes().getNamedItem("testcaseName").getTextContent();
            if (nNode.hasChildNodes() && n.equals(nodeValue)) {
                xmldataFactory.setTestCaseId(nNode.getAttributes().getNamedItem("testcaseId").getTextContent());
                xmldataFactory.setTestCaseName(nNode.getAttributes().getNamedItem("testcaseName").getTextContent());
                printNodeList(nNode.getChildNodes());
                xmldataFactory.setInputParameters(input_Parameter1);
                xmldataFactory.setValidationParameters(validation_Parameter1);
                provider.add(new Object[]{xmldataFactory});
            }
        }
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    } catch (SAXException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return provider.iterator();
    }

    /**
     * @param nodeList the list of the nodes inside the Parent node
     */
    private static void printNodeList(NodeList nodeList)
    {
        for (int count = 0; count < nodeList.getLength(); count++)
        {
            Node elemNode = nodeList.item(count);
            if ((elemNode.getNodeType() == Node.ELEMENT_NODE))
            {
                for (int j=0;j<elemNode.getChildNodes().getLength();j++) {
                    if (elemNode.getNodeName().equals("InputParameters")) {
                        if (!elemNode.getChildNodes().item(j).getNodeName().equals("#text")) {
                            input_Parameter.put(elemNode.getChildNodes().item(j).getNodeName(), elemNode.getChildNodes().item(j).getTextContent());
                            input_Parameter1= new JSONObject(input_Parameter);
                        }
                    } else if (elemNode.getNodeName().equals("ValidationParameters")){
                        if (!elemNode.getChildNodes().item(j).getNodeName().equals("#text")) {
                            validation_Parameter.put(elemNode.getChildNodes().item(j).getNodeName(), elemNode.getChildNodes().item(j).getTextContent());
                            validation_Parameter1= new JSONObject(validation_Parameter);
                        }
                        }
                }
                if (elemNode.hasChildNodes())
                {
                    printNodeList(elemNode.getChildNodes());
                }
            }
        }
    }
}
