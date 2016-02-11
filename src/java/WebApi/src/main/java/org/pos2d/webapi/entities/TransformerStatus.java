/**
 * 
 */
package org.pos2d.webapi.entities;

/**
 * @author soumik
 *
 */
public class TransformerStatus {
	public static final String STATUS_OK = "OK";
	public static final String STATUS_BAD = "BAD";
	
	private long gid;
	private String status;
	private String statusData;
	private Transformer transformer;
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
	public String getStatusData() {
		return statusData;
	}
	/**
	 * @param statusData the statusData to set
	 */
	public void setStatusData(String statusData) {
		this.statusData = statusData;
	}
	/**
	 * @return the transformer
	 */
	public Transformer getTransformer() {
		return transformer;
	}
	/**
	 * @param transformer the transformer to set
	 */
	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}
	
	@Override
	public boolean equals(Object other){
		if ( !(other instanceof TransformerStatus) ) return false;
		
		final TransformerStatus status = (TransformerStatus) other;

        if ( !(status.getTransformer().getGid() == this.getTransformer().getGid())) return false;
        //if (!(status.getStatus().equalsIgnoreCase(this.getStatus())))  return false;

        return true;
		
	}
	
	@Override
	public int hashCode() {
        int result;
        //result = this.getStatus().hashCode();
        result = ((Long)this.getTransformer().getGid()).hashCode();
        return result;
    }
}
