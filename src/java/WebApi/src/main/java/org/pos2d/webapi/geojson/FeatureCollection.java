/**
 * 
 */
package org.pos2d.webapi.geojson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soumik
 *
 */
public class FeatureCollection implements GeoJsonObject{

	private List<Feature> features;
	
	public FeatureCollection(){
		features = new ArrayList<Feature>();
	}
	@Override
	public String getType() {
		return GeoJsonObjectTypes.FEATURECOLLECTION;
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
	
	public void addFeature(Feature feature){
		features.add(feature);
	}
	
	public List<Feature> getFeatures(){
		return features;
	}
	

}
