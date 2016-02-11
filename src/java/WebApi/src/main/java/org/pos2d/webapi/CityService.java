/**
 * 
 */
package org.pos2d.webapi;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pos2d.webapi.dao.CityDAO;
import org.pos2d.webapi.entities.City;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author soumik
 *
 */
@Path("/city")
@Produces(MediaType.APPLICATION_JSON)
public class CityService {
	
	@Autowired
    private CityDAO cityDAO;
	
	@GET
    @Path("/{name}")
    public String getCity(@PathParam("name") String name) {
        City city = cityDAO.getByName(name);
        //CityBean
		return name;
    }
	
	@SuppressWarnings("rawtypes")
	@GET
    @Path("/getall")
    public List getAllCities() {
		List<City> cities = cityDAO.getAll();
		/*List<CityBean> cityNames = new ArrayList<CityBean>();
		for(City city: cities){
			//cityNames.add(city.getName());
			CityBean bean = new CityBean();
			bean.setName(city.getName());
			cityNames.add(bean);
		}
		return cityNames;*/
		return cities;
	}

}
