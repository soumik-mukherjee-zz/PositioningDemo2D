/**
 * 
 */
package org.pos2d.webapi;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pos2d.webapi.dao.VehicleDAO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * @author soumik
 *
 */
@Path("/vehicle")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleService {
	
	@Autowired
	private VehicleDAO vehicleDAO;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@POST
	@Path("/location")
	public void updateLocation(String data){
		/*
		 * Message schema is 
		 * {
			    "vehicleName":"TRUCK/91-01",
			    "longitude":"88.67",
			    "latitude":"27.35"
			}
		 *
		 */
		rabbitTemplate.convertAndSend("location.onchange", data);
	}
	
	@POST
	@Path("/register")
	public void register(String jsonData){
		/*
		 * Expected message schema:
		 * {
		    "name":"TRUCK/91-01"
		   }
		 * 
		 */
		String vehicleName = null;
		JsonFactory factory = new JsonFactory();
		JsonParser parser;
		try {
			parser = factory.createParser(jsonData);
			while(!parser.isClosed()){
				JsonToken jsonToken = parser.nextToken();
				if(JsonToken.FIELD_NAME.equals(jsonToken)){
					String fieldName = parser.getCurrentName();
			        System.out.println(fieldName);
			        jsonToken = parser.nextToken();
			        if("name".equals(fieldName)){
			        	vehicleName = parser.getValueAsString();
			        }
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		vehicleDAO.createActiveVehicle(vehicleName);
	}
}
