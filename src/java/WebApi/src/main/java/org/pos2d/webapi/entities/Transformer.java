/**
 * 
 */
package org.pos2d.webapi.entities;

import com.vividsolutions.jts.geom.MultiPoint;


/**
 * @author soumik
 *
 */
public class Transformer {
	private long gid;
	private String name;
	private MultiPoint geom;
	private String city;
	private String suburb;
	private TransformerStatus transformerStatus;
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
	 * @return the geom
	 */
	public MultiPoint getGeom() {
		return geom;
	}
	/**
	 * @param geom the geom to set
	 */
	public void setGeom(MultiPoint geom) {
		this.geom = geom;
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
	 * @return the suburb
	 */
	public String getSuburb() {
		return suburb;
	}
	/**
	 * @param suburb the suburb to set
	 */
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	/**
	 * @return the transformerStatus
	 */
	public TransformerStatus getTransformerStatus() {
		return transformerStatus;
	}
	/**
	 * @param transformerStatus the transformerStatus to set
	 */
	public void setTransformerStatus(TransformerStatus transformerStatus) {
		this.transformerStatus = transformerStatus;
	}
}
