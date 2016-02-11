/**
 * 
 */
package org.pos2d.webapi.beans;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author soumik
 *
 */
@JsonIgnoreProperties(value={"statusData"})
public class TransformerStatusBean {
	private String name;
	private String status;
	//private String statusData;
	private Map<String,String> statusData;
	
	public TransformerStatusBean(){
		statusData = new HashMap<String, String>();
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the statusData
	 */
	public Map<String,String> getStatusData() {
		return statusData;
	}
	/**
	 * @param statusData the statusData to set
	 */
	public void setStatusData(Map<String,String> statusData) {
		this.statusData = statusData;
	}
	
	public void addStatusData(String name, String value){
		this.statusData.put(name, value);
	}
	
}
