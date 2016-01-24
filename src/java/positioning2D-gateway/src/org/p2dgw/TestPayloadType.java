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
public class TestPayloadType {
	
	@ArduinoNumericFieldType(Sequence=3,ArduinoNumberType = ArduinoNumericType.UInt16_t)
	private int field1;
	@ArduinoNumericFieldType(Sequence=2,ArduinoNumberType = ArduinoNumericType.UInt8_t)
	private int field2;
	@ArduinoNumericFieldType(Sequence=1,ArduinoNumberType = ArduinoNumericType.UInt32_t)
	private long field3;
	/**
	 * @return the field1
	 */
	public int getField1() {
		return field1;
	}
	/**
	 * @param field1 the field1 to set
	 */
	public void setField1(int field1) {
		this.field1 = field1;
	}
	/**
	 * @return the field2
	 */
	public int getField2() {
		return field2;
	}
	/**
	 * @param field2 the field2 to set
	 */
	public void setField2(int field2) {
		this.field2 = field2;
	}
	/**
	 * @return the field3
	 */
	public long getField3() {
		return field3;
	}
	/**
	 * @param field3 the field3 to set
	 */
	public void setField3(long field3) {
		this.field3 = field3;
	}
}
