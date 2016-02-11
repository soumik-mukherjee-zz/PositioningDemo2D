/**
 * 
 */
package org.pos2d.webapi.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soumik
 *
 */
public class FireProximityDetectionBean {
	private String sourceEvent;
	private String city;
	private String subUrb;
	private Map<String, Object> properties = new HashMap<String, Object>();
	/**
	 * @return the sourceEvent
	 */
	public String getSourceEvent() {
		return sourceEvent;
	}
	/**
	 * @param sourceEvent the sourceEvent to set
	 */
	public void setSourceEvent(String sourceEvent) {
		this.sourceEvent = sourceEvent;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the subUrb
	 */
	public String getSubUrb() {
		return subUrb;
	}
	/**
	 * @param subUrb the subUrb to set
	 */
	public void setSubUrb(String subUrb) {
		this.subUrb = subUrb;
	}
	
}
