/**
 * 
 */
package org.pos2d.webapi.entities;

import java.util.Calendar;

import com.vividsolutions.jts.geom.Point;

/**
 * @author soumik
 *
 */
public class VehicleLocation {
	private long gid;
	private Vehicle vehicle;
	private Point location;
	private Calendar updatedOn;
	/**
	 * @return the gid
	 */
	public long getGid() {
		return gid;
	}
	/**
	 * @param gid the gid to set
	 */
	public void setGid(long gid) {
		this.gid = gid;
	}
	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	/**
	 * @return the updatedOn
	 */
	public Calendar getUpdatedOn() {
		return updatedOn;
	}
	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}
}
