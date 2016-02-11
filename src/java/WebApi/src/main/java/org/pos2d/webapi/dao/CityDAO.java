/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.List;

import org.pos2d.webapi.entities.City;

/**
 * @author soumik
 *
 */
public interface CityDAO {
	public List<City> getAll();
	public City getByName(String name);
}
