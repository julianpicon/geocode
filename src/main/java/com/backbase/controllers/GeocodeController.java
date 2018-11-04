package com.backbase.controllers;

import org.apache.camel.*;
import org.apache.camel.builder.BuilderSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.impl.XMLEntityManager.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Julián Picón
 * @since November 4, 2018
 */
@Controller
@RequestMapping("/api")
public class GeocodeController {

    private final Logger logger = LoggerFactory.getLogger(GeocodeController.class);

    @Autowired
    @EndpointInject(uri="direct:start")
    private ProducerTemplate template;

    @Value("${google.map.api.key}")
    private String apiKey;
    
    @RequestMapping(value = "/geocode/{address}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String code2(@PathVariable(name = "address") String address) {
        if (StringUtils.isEmpty(address.trim())) {
            return new String("{\"status\": \"ERR\"}");
        }
        final Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("address", address);
        headers.put("key", this.apiKey);

        Exchange exchange = this.template.send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeaders(headers);
            }
        });

        String out = exchange.getOut().getBody(String.class);
        return out;
    }

}
