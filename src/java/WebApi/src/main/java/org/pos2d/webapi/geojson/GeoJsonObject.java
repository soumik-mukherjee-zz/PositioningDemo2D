/**
 * 
 */
package org.pos2d.webapi.geojson;

/**
 * @author soumik
 *
 */
public interface GeoJsonObject {
	public String getType();
	public Crs getCrs();
	public void setCrs(Crs crs);
}
