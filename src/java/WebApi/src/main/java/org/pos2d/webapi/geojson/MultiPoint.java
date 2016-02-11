/**
 * 
 */
package org.pos2d.webapi.geojson;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author soumik
 *
 */
public class MultiPoint implements Geometry {

	private double[][] coordinates;
	
	public MultiPoint(com.vividsolutions.jts.geom.MultiPoint jtsMultiPoint){
		//this.jtsPolygon = jtsPolygon;
		com.vividsolutions.jts.geom.Coordinate[] coords = jtsMultiPoint.getCoordinates();
		coordinates = new double[coords.length][2];
		int i = 0;
		for (Coordinate coord:coords){
			coordinates[i][0] = coord.x;
			coordinates[i][1] = coord.y;
			i ++;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.pos2d.webapi.geojson.GeoJsonObject#getType()
	 */
	@Override
	public String getType() {
		return GeoJsonObjectTypes.MULTIPOINT;
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
	 * @see org.pos2d.webapi.geojson.GeoJsonObject#setCrs(org.pos2d.webapi.geojson.Crs)
	 */
	@Override
	public void setCrs(Crs crs) {
		// do nothing

	}

	/**
	 * @return the coordinates
	 */
	public double[][] getCoordinates() {
		return coordinates;
	}

}
