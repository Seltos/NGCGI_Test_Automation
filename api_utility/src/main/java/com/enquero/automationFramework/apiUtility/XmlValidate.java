package com.enquero.automationFramework.apiUtility;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlValidate {

    public boolean validateXmlWithSchema(String xmlLocation, String schemaLocation){


        try {
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(
                    schemaLocation));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlLocation)));
            return true;
        } catch (SAXException e) {
            e.printStackTrace();
            return  false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



}
