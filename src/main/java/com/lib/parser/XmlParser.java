package com.lib.parser;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Convert string to XML doc
 */
public class XmlParser {
    public static Document parse(String xml) throws ParserConfigurationException, SAXException, IOException{

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;

        builder = factory.newDocumentBuilder();
        is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        return doc;
    }
}
