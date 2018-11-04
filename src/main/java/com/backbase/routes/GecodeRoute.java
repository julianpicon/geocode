package com.backbase.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.backbase.bean.GeocodeBean;

/**
 * @author Julián Picón
 * @since November 4, 2018
 */
@Component
public class GecodeRoute extends RouteBuilder {

    @Value("${google.map.api.endpoint}")
    private String endPoint;

    @Override
    public void configure() throws Exception {
        XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setEncoding("UTF-8");
        xmlJsonFormat.setTrimSpaces(true);

        from("direct:start")
        	.setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
        	.setHeader(Exchange.HTTP_QUERY, simple("address=${in.headers.address}&key=${headers.key}"))
	        	.to(endPoint)
	        	.to("log:?level=INFO&showAll=true")
	        	.bean(new GeocodeBean())
	        	.marshal(xmlJsonFormat)
        	;

    }
}
