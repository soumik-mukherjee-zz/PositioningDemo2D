/**
 * 
 */
package org.pos2d.webapi.geojson;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author soumik
 *
 */
public class Polygon implements Geometry {

	private double[][][] coordinates;
	
	public Polygon(com.vividsolutions.jts.geom.Polygon jtsPolygon){
		//this.jtsPolygon = jtsPolygon;
		com.vividsolutions.jts.geom.Coordinate[] coords = jtsPolygon.getCoordinates();
		coordinates = new double[1][coords.length][2];
		int i = 0;
		for (Coordinate coord:coords){
			coordinates[0][i][0] = coord.x;
			coordinates[0][i][1] = coord.y;
			i ++;
		}
	}
	/* (non-Javadoc)
	 * @see org.pos2d.webapi.geojson.GeoJsonObject#getType()
	 */
	@Override
	public String getType() {
		return GeoJsonObjectTypes.POLYGON;
	}

	/* (non-Javadoc)
	 * @see org.pos2d.webapi.geojson.GeoJsonObject#getCrs()
	 */
	@Override
	public Crs getCrs() {
		// support only epsg 4326 projection as of now
		return  Crs.CRS_EPSG_4326;
	}

	/* (non-Javadoc)
	 * @see org.pos2d.webapi.geojson.GeoJsonObject#setCrs(java.lang.String)
	 */
	@Override
	public void setCrs(Crs crs) {
		// do nothing

	}
	
	public double[][][] getCoordinates(){
		return coordinates;
	}

}
