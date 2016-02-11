/**
 * 
 */
package org.pos2d.webapi.mq;

import java.io.IOException;

import org.pos2d.webapi.beans.TransformerStatusBean;
import org.pos2d.webapi.beans.VehicleLocationBean;
import org.pos2d.webapi.dao.TransformerDAO;
import org.pos2d.webapi.dao.VehicleDAO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author soumik
 *
 */
public class DbLoggerListener {
	
	@Autowired
    private TransformerDAO xfrDAO;
	
	@Autowired
    private VehicleDAO vehicleDAO;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Transactional
	public void saveLocation(String data) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("In DbLoggerListener#saveLocation:");
		ObjectMapper mapper = new ObjectMapper();
		VehicleLocationBean bean = mapper.readValue(data, VehicleLocationBean.class);
		vehicleDAO.createVehicleLocation(bean);
		rabbitTemplate.convertAndSend("location.changed", data);
	}
	
	@Transactional
	public void saveXfrStatus(String data) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("In DbLoggerListener#saveXfrStatus:");
		//System.out.println(foo);
		ObjectMapper mapper = new ObjectMapper();
		TransformerStatusBean bean = mapper.readValue(data, TransformerStatusBean.class);
		String statusData = mapper.writeValueAsString(bean.getStatusData());
		xfrDAO.createXfrStatus(bean, statusData);
		rabbitTemplate.convertAndSend("xfr.status.changed", data);
    }
	
	@Transactional
	public void saveProximityEvent(String data) {
		System.out.println("In DbLoggerListener#saveProximityEvent:");
		System.out.println(data);
    }

}
