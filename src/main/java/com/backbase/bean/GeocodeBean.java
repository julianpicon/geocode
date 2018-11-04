package com.backbase.bean;


import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Julián Picón
 * @since November 4, 2018
 */
public class GeocodeBean {
	
	public String map(String input) {
		Document initialDocument = convertStringToDocument(input);
		Document modifiedDocument = createDocument(initialDocument);
        String str = convertDocumentToString(modifiedDocument);
		return str;
	}
	
	private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
    private Document createDocument(Document doc) {
    	try {
    		
        	final NodeList results = doc.getElementsByTagName("result");
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			// root element
			Element root = document.createElement("geocoder");
			document.appendChild(root);
			
			// status element
			final String status = doc.getElementsByTagName("status").item(0).getTextContent();
			Element statusElement = document.createElement("status");
			statusElement.appendChild(document.createTextNode(status));
			root.appendChild(statusElement);

			for (int i = 0; i < results.getLength(); i++) {
				
	        	final String formattedAddress = doc.getElementsByTagName("formatted_address").item(i).getTextContent();
	        	final String latitude =  doc.getElementsByTagName("lat").item(i).getTextContent();
	        	final String longitude =  doc.getElementsByTagName("lng").item(i).getTextContent();
	        	
	        	// address element
				Element addressElement = document.createElement("results");
				root.appendChild(addressElement);
				
	        	// formatted address
				Element formattedAddressElement = document.createElement("formatted_address");
				formattedAddressElement.appendChild(document.createTextNode(formattedAddress));
				addressElement.appendChild(formattedAddressElement);

				// latitude
				Element latitudeElement = document.createElement("latitude");
				latitudeElement.appendChild(document.createTextNode(latitude));
				addressElement.appendChild(latitudeElement);

				// longitude
				Element longitudeElement = document.createElement("longitude");
				longitudeElement.appendChild(document.createTextNode(longitude));
				addressElement.appendChild(longitudeElement);
			}
			
			
			return document;
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
    	
		return doc;
	}

}
