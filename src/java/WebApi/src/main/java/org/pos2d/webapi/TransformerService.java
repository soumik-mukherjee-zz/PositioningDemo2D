/**
 * 
 */
package org.pos2d.webapi;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.pos2d.webapi.beans.TransformerStatusBean;
import org.pos2d.webapi.dao.TransformerDAO;
import org.pos2d.webapi.entities.Transformer;
import org.pos2d.webapi.entities.TransformerStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author soumik
 *
 */
@Path("/xfr")
@Produces(MediaType.APPLICATION_JSON)
public class TransformerService {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	//@Autowired
	//private ApplicationContext appContext;
	
	@Autowired
    private TransformerDAO xfrDAO;
	
	@GET
	@Path("/GetByStatusAsGeoJson")
    public org.pos2d.webapi.geojson.FeatureCollection getTransformers(@QueryParam("city") String cityName, @QueryParam("area") String suburb, @QueryParam("status") String status) {
        List<Transformer> xfrs = xfrDAO.getBySuburbAndStatus(cityName, suburb, status);
        org.pos2d.webapi.geojson.FeatureCollection fc = new org.pos2d.webapi.geojson.FeatureCollection();
        for (Transformer xfr:xfrs){
        	org.pos2d.webapi.geojson.Feature xfrFeature = new org.pos2d.webapi.geojson.Feature();
        	xfrFeature.addProperty("name", xfr.getName());
        	org.pos2d.webapi.geojson.MultiPoint xfrMpt = new org.pos2d.webapi.geojson.MultiPoint(xfr.getGeom());
        	xfrFeature.setGeometry(xfrMpt);
        	fc.addFeature(xfrFeature);
        }
        return fc;
    }
	
	@GET
	@Path("/GetAllAsGeoJson")
    public org.pos2d.webapi.geojson.FeatureCollection getTransformers(@QueryParam("city") String cityName, @QueryParam("area") String suburb) {
        List<Transformer> xfrs = xfrDAO.getBySuburb(cityName, suburb);
        org.pos2d.webapi.geojson.FeatureCollection fc = new org.pos2d.webapi.geojson.FeatureCollection();
        for (Transformer xfr:xfrs){
        	org.pos2d.webapi.geojson.Feature xfrFeature = new org.pos2d.webapi.geojson.Feature();
        	xfrFeature.addProperty("type", "transformer");
        	xfrFeature.addProperty("name", xfr.getName());
        	String status = xfr.getTransformerStatus().getStatus();
        	xfrFeature.addProperty("status", status);
        	if (status.equalsIgnoreCase(TransformerStatus.STATUS_BAD)){
        		xfrFeature.addProperty("statusData", xfr.getTransformerStatus().getStatusData());
        	}
        	org.pos2d.webapi.geojson.MultiPoint xfrMpt = new org.pos2d.webapi.geojson.MultiPoint(xfr.getGeom());
        	xfrFeature.setGeometry(xfrMpt);
        	fc.addFeature(xfrFeature);
        }
        return fc;
    }
	
	@POST
	@Path("/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateTransformerStatus(String data){
		/*
		 * Message schema is 
		 * {
			    "name":"DTR/SM/01",
			    "status":"BAD"|"OK",
			    "statusData":{
			        "oil-level-pct":"100",
			        "oxidizer-health-pct":"15"
		    	}
		    }
		 *
		 */
		//appContext.getBean("transformerStatus",RabbitTemplate.class).convertAndSend(data);
		rabbitTemplate.convertAndSend("xfr.status.onchange", data);
	}
}
