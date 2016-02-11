/**
 * 
 */
package org.pos2d.webapi.entities;

import java.util.List;
import java.util.Set;

/**
 * @author soumik
 *
 */
public class Vehicle {
	private long gid;
	private String name;
	private String active;
	private Set<VehicleLocation> locations;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
	/**
	 * @return the locations
	 */
	public Set<VehicleLocation> getLocations() {
		return locations;
	}
	/**
	 * @param locations the locations to set
	 */
	public void setLocations(Set<VehicleLocation> locations) {
		this.locations = locations;
	}
}
