/**
 * 
 */
package org.pos2d.webapi.dao;

import org.pos2d.webapi.beans.VehicleLocationBean;

/**
 * @author soumik
 *
 */
public interface VehicleDAO {
	void createActiveVehicle(String vehicleName);
	void createVehicleLocation(VehicleLocationBean location);
}
