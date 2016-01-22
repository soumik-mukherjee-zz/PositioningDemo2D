/**
 * 
 */
package com.tcs.demo.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * @author soumik
 *
 */
public class VehicleLocation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1010642834150946180L;
	private String vehicleId;
	private Double longitude;
	private Double latitude;
	private Date locationChangedOn;
	//private boolean subscriptionAcknowledged;
	/**
	 * @return the vehicleId
	 */
	public String getVehicleId() {
		return vehicleId;
	}
	/**
	 * @param vehicleId the vehicleId to set
	 */
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the locationChangedOn
	 */
	public Date getLocationChangedOn() {
		return locationChangedOn;
	}
	/**
	 * @param locationChangedOn the locationChangedOn to set
	 */
	public void setLocationChangedOn(Date locationChangedOn) {
		this.locationChangedOn = locationChangedOn;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result;
		//result = getVehicleId().hashCode() + getLatitude().hashCode() + getLongitude().hashCode() + getLocationChangedOn().hashCode();
		result = getVehicleId().hashCode() + getLocationChangedOn().hashCode();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
        if ( !(other instanceof VehicleLocation) ) return false;
        final VehicleLocation otherVl = (VehicleLocation) other;

        if ( !otherVl.getVehicleId().equals( getVehicleId() ) ) return false;
        //if ( !otherVl.getLatitude().equals( getLatitude() ) ) return false;
        //if ( !otherVl.getLongitude().equals( getLongitude() ) ) return false;
        if ( !otherVl.getLocationChangedOn().equals( getLocationChangedOn() ) ) return false;

        return true;
	}
	
	

}
