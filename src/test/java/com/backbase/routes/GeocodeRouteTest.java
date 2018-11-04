package com.backbase.routes;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Julián Picón
 * @since November 4, 2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GeocodeRouteTest {

	private static final String BOGOTA = "Bogota";
	
    @Autowired
    ProducerTemplate producerTemplate;
    
    @Value("${google.map.api.key}")
    private String apiKey;

    @Test
    public void testGeocodeRoute() {
        
    	final Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("address", BOGOTA);
        headers.put("key", apiKey);
        
        Exchange exchange = producerTemplate.send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeaders(headers);
            }
        });

        Assert.assertTrue(exchange.getOut().getBody(String.class).contains("\"status\":\"OK\""));
    }
}
