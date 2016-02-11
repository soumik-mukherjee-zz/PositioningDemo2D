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
public class Feature implements GeoJsonObject{
	
	private Geometry geometry;
	private Map<String,Object> properties;
	
	public Feature (){
		properties = new HashMap<String,Object>();
	}
	
	@Override
	public String getType() {
		return GeoJsonObjectTypes.FEATURE;
	}

	@Override
	public Crs getCrs() {
		// support only epsg 4326 projection as of now
		return Crs.CRS_EPSG_4326;
	}

	@Override
	public void setCrs(Crs crs) {
		// do nothing
		
	}

	/**
	 * @return the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public void addProperty (String key, Object value){
		properties.put(key, value);
	}

}
