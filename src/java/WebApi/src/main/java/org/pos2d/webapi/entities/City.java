/**
 * 
 */
package org.pos2d.webapi.entities;



import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.vividsolutions.jts.geom.Point;

/**
 * @author soumik
 *
 */
@JsonIgnoreProperties(value={"geom"})
public class City {
	
	private long gid;
	private String name;
	private Point geom;
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
	public Point getGeom() {
		return geom;
	}
	/**
	 * @param geom the geom to set
	 */
	public void setGeom(Point geom) {
		this.geom = geom;
	}
	
	public double getLongitude(){
		return this.geom.getX();
	}
	
	public double getLatitude(){
		return this.geom.getY();
	}

}
