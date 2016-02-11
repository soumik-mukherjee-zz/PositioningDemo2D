/**
 * 
 */
package org.pos2d.webapi.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//import org.geojson.Polygon;
//import org.pos2d.webapi.geojson.Feature;
//import org.pos2d.webapi.geojson.FeatureCollection;

import com.vividsolutions.jts.geom.Polygon;


/**
 * @author soumik
 *
 */
@JsonIgnoreProperties(value={"geom"})
public class UrbanArea {
	private long gid;
	private String name;
	private Polygon geom;
	private String city;
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
	public Polygon getGeom() {
		return geom;
	}
	/**
	 * @param geom the geom to set
	 */
	public void setGeom(Polygon geom) {
		this.geom = geom;
	}
	
	/*public org.pos2d.webapi.geojson.FeatureCollection getGeoJson(){
		//return geom.getCoordinates();
		org.pos2d.webapi.geojson.FeatureCollection retVal = new org.pos2d.webapi.geojson.FeatureCollection();
		org.pos2d.webapi.geojson.Feature urbanAreaFeature = new org.pos2d.webapi.geojson.Feature();
		org.pos2d.webapi.geojson.Polygon subUrbPolygon = new org.pos2d.webapi.geojson.Polygon(this.getGeom());
		urbanAreaFeature.setGeometry(subUrbPolygon);
		retVal.addFeature(urbanAreaFeature);
		return retVal;
		Feature subUrbFeature = new Feature();
		
		retVal.add(subUrbFeature);
		Polygon subUrbPolygon = new Polygon();
		subUrbFeature.setGeometry(subUrbPolygon);
		
	    Coordinate coords[] = geom.getCoordinates();
	    //subUrbPolygon.
	    List<LngLatAlt> lngLatAlts = new ArrayList<LngLatAlt>();
	    for (Coordinate coord:coords){
	    	LngLatAlt lngLat = new LngLatAlt();
	    	lngLat.setLongitude(coord.x);
	    	lngLat.setLatitude(coord.y);
	    	lngLat.setAltitude(0.0);
	    	lngLatAlts.add(lngLat);
	    }
	    subUrbPolygon.add(lngLatAlts);
	    //subUrbPolygon.setCrs(geom.getSRID());
	    //Crs crs4326 = new Crs();
	    //Map<String,Object> propsCrs4326;
	    //crs4326.setProperties(properties);
		//return retVal;
	}*/
}
