package com.enquero.driverfactory.web_selenium.actions;

import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.internal.runtime.Undefined;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.enquero.driverfactory.web_selenium.base.TestAction;
import com.enquero.driverfactory.web_selenium.contracts.ITestActor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An action that parses XML data and extracts fragments of it based on XPath
 * expressions.
 */
public class ReadXml extends TestAction {

    // Keeping a final reference to the current action instance, so that we
    // can access it from anonymous classes
    final TestAction action = this;

    /**
     * Wraps a Node instance into a JS object with a friendly API that allows
     * the user to parse the XML data (access children and attributes).
     */
    private AbstractJSObject createNodeWrapper(Node node) {
        return new XmlNodeWrapper(node);
    }

    private List<Node> executeXPath(Node rootNode, String xpathExpression) {
        try {
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile(xpathExpression);
            Object nodeObj = expr.evaluate(rootNode, XPathConstants.NODESET);
            List<Node> results = new ArrayList<>();
            if (nodeObj instanceof Node) {
                results.add((Node) nodeObj);
            } else if (nodeObj instanceof NodeList) {
                NodeList nodeList = (NodeList) nodeObj;
                int nodeCount = nodeList.getLength();
                for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
                    Node resultItem = nodeList.item(nodeIndex);
                    results.add(resultItem);
                }
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "There was an error executing XPath expression %s on node %s",
                    xpathExpression,
                    rootNode.getLocalName()), ex);
        }
    }

    @Override
    public void run() {
        super.run();

        String filePath = this.readStringArgument("file", null);
        String xmlString = this.readStringArgument("xml", null);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = null;

            if (filePath != null) {
                File xmlFile = new File(filePath);
                doc = builder.parse(xmlFile);
            } else if (xmlString != null) {
                doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
            } else {
                throw new RuntimeException("Neither the \"file\" argument, nor the \"xml\" argument were provided.");
            }

            this.writeOutput("rootNode", createNodeWrapper(doc.getDocumentElement()));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse XML", ex);
        }
    }

    private void processAttributes(Node xmlNode, Map<String, Object> nodeObject) {
        NamedNodeMap attributesMap = xmlNode.getAttributes();
        int attrCount = attributesMap.getLength();
        for (int attrIndex = 0; attrIndex < attrCount; attrIndex++) {
            Node attributeNode = attributesMap.item(attrIndex);
            if (attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String prefix = attributeNode.getPrefix() != null ? attributeNode.getPrefix() : "";
                nodeObject.put("$" + prefix + attributeNode.getNodeName(), attributeNode.getNodeValue());
            }
        }
    }

    private Object processNode(Node xmlNode) {
        if (xmlNode.getNodeType() == Node.ELEMENT_NODE) {
            Map<String, Object> nodeObject = new HashMap<>();

            processAttributes(xmlNode, nodeObject);

            StringBuilder nodeText = null;
            NodeList nodeList = xmlNode.getChildNodes();
            int childCount = nodeList.getLength();
            for (int childIndex = 0; childIndex < childCount; childIndex++) {
                Node childNode = nodeList.item(childIndex);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    String prefix = childNode.getPrefix() != null ? childNode.getPrefix() : "";
                    nodeObject.put(prefix + childNode.getNodeName(), processNode(childNode));
                } else if (childNode.getNodeType() == Node.TEXT_NODE) {
                    if (nodeText == null) {
                        nodeText = new StringBuilder();
                    }

                    nodeText.append(childNode.getTextContent());
                }
            }

            if (nodeText != null) {
                nodeObject.put("$$text", nodeText.toString());
            }

            return nodeObject;
        } else if (xmlNode.getNodeType() == Node.ATTRIBUTE_NODE) {
            return xmlNode.getNodeValue();
        } else if (xmlNode.getNodeType() == Node.TEXT_NODE) {
            return xmlNode.getNodeValue();
        } else {
            return null;
        }
    }

    class XmlNodeWrapper extends AbstractJSObject {

        private transient Node wrappedNode;

        public XmlNodeWrapper(Node node) {
            wrappedNode = node;
        }

        @Override
        public Object getMember(String name) {
            switch (name) {
                case "attribute":
                    return new Function<String, String>() {
                        @Override
                        public String apply(String attributeName) {
                            return wrappedNode.getAttributes()
                                    .getNamedItem(attributeName)
                                    .getNodeValue();
                        }
                    };
                case "attributes":
                    Map<String, String> attributesMap = new HashMap();
                    NamedNodeMap attributesNodeMap = wrappedNode.getAttributes();
                    int attrCount = attributesNodeMap.getLength();
                    for (int attrIndex = 0; attrIndex < attrCount; attrIndex++) {
                        Node attributeNode = attributesNodeMap.item(attrIndex);
                        attributesMap.put(attributeNode.getNodeName(), attributeNode.getNodeValue());
                    }
                    ITestActor actor = action.getActor();
                    if (actor != null) {
                        return actor.toJsType(attributesMap);
                    } else {
                        return attributesMap;
                    }
                case "name":
                    return wrappedNode.getNodeName();
                case "node":
                    return new Function<String, AbstractJSObject>() {
                        @Override
                        public AbstractJSObject apply(String xpathExpression) {
                            List<Node> children = executeXPath(wrappedNode, xpathExpression);
                            if (children.size() > 0) {
                                return createNodeWrapper(children.get(0));
                            } else {
                                return null;
                            }
                        }
                    };
                case "nodes":
                    return new AbstractJSObject() {
                        @Override
                        public boolean isFunction() {
                            return true;
                        }

                        @Override
                        public Object call(Object thiz, Object... args) {
                            String xpathExpression;

                            if (args.length > 0) {
                                xpathExpression = args[0].toString();
                            } else {
                                xpathExpression = "*";
                            }

                            List<Node> children = executeXPath(wrappedNode, xpathExpression);
                            List<AbstractJSObject> jsObjectList = children.stream()
                                    .map(n -> createNodeWrapper(n))
                                    .collect(Collectors.toList());
                            return action.getActor().toJsType(jsObjectList);
                        }
                    };
                case "nodeType":
                    switch (wrappedNode.getNodeType()) {
                        case Node.ATTRIBUTE_NODE:
                            return "attribute";
                        case Node.CDATA_SECTION_NODE:
                            return "cdata";
                        case Node.COMMENT_NODE:
                            return "comment";
                        case Node.DOCUMENT_TYPE_NODE:
                            return "doctype";
                        case Node.ELEMENT_NODE:
                            return "element";
                        case Node.ENTITY_NODE:
                            return "entity";
                        case Node.ENTITY_REFERENCE_NODE:
                            return "entityref";
                        case Node.NOTATION_NODE:
                            return "notation";
                        case Node.PROCESSING_INSTRUCTION_NODE:
                            return "instruction";
                        case Node.TEXT_NODE:
                            return "text";
                        default:
                            throw new RuntimeException(String.format(
                                    "Unknown XML node type: %s",
                                    wrappedNode.getNodeType()));
                    }
                case "prefix":
                    return wrappedNode.getPrefix();
                case "text":
                    return wrappedNode.getTextContent();
                default:
                    return Undefined.getUndefined();
            }
        }
    }
}