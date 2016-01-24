/**
 * 
 */
package org.p2dgw;

import org.sardp4j.ArduinoNumericFieldType;
import org.sardp4j.ArduinoNumericType;

/**
 * @author soumik
 *
 */
public class GeoDemoPayloadType {
	
	@ArduinoNumericFieldType(
			Sequence=1,
			ArduinoNumberType = ArduinoNumericType.UInt32_t
	)
	private long xUs;
	@ArduinoNumericFieldType(
			Sequence=2,
			ArduinoNumberType = ArduinoNumericType.UInt32_t
	)
	private long yUs;
	/**
	 * @return the x
	 */
	public long getXUs() {
		return xUs;
	}
	/**
	 * @param x the x to set
	 */
	public void setXUs(long xUs) {
		this.xUs = xUs;
	}
	/**
	 * @return the y
	 */
	public long getYUs() {
		return yUs;
	}
	/**
	 * @param y the y to set
	 */
	public void setYUs(long yUs) {
		this.yUs = yUs;
	}
	
	
}
