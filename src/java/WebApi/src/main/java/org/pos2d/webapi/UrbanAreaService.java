/**
 * 
 */
package org.pos2d.webapi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.pos2d.webapi.dao.UrbanAreaDAO;
import org.pos2d.webapi.entities.UrbanArea;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author soumik
 *
 */
@Path("/suburb")
@Produces(MediaType.APPLICATION_JSON)
public class UrbanAreaService {
	@Autowired
    private UrbanAreaDAO urbanAreaDAO;
	
	@GET
    @Path("/{cityName}")
    public List<UrbanArea> getSubUrbsOfCity(@PathParam("cityName") String cityName) {
        List<UrbanArea> subUrb = urbanAreaDAO.getAllForCity(cityName);
        //CityBean
		return subUrb;
    }
	
	@GET
	@Path("/GetGeoJson")
    public org.pos2d.webapi.geojson.FeatureCollection getGeoJson(@QueryParam("city") String cityName, @QueryParam("area") String areaName) {
        UrbanArea subUrb = urbanAreaDAO.getByName(areaName, cityName);
        org.pos2d.webapi.geojson.FeatureCollection retVal = new org.pos2d.webapi.geojson.FeatureCollection();
		org.pos2d.webapi.geojson.Feature urbanAreaFeature = new org.pos2d.webapi.geojson.Feature();
		org.pos2d.webapi.geojson.Polygon subUrbPolygon = new org.pos2d.webapi.geojson.Polygon(subUrb.getGeom());
		urbanAreaFeature.setGeometry(subUrbPolygon);
		urbanAreaFeature.addProperty("type", "urban-area");
		retVal.addFeature(urbanAreaFeature);
        return retVal;
    }
}
