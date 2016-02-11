/**
 * 
 */
package org.pos2d.webapi.geojson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soumik
 *
 */
public class Crs {
	
	public static final Crs CRS_EPSG_4326 = new Crs("name","urn:ogc:def:crs:OGC:1.3:CRS84");
	private String type;
	private Map<String,Object> properties;
	
	public Crs(String type, String ogcCrsUrn){
		this.type = type;
		this.properties = new HashMap<String,Object>();
		this.properties.put("name", ogcCrsUrn);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	

}
