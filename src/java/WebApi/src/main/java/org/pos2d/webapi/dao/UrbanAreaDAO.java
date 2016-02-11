/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.List;

import org.pos2d.webapi.entities.UrbanArea;

/**
 * @author soumik
 *
 */
public interface UrbanAreaDAO {
	
	public UrbanArea getByName(String name, String city);
	public List<UrbanArea> getAllForCity(String city);

}
