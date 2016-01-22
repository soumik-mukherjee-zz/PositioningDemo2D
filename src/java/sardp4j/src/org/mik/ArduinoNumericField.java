/**
 * 
 */
package org.mik;

import java.lang.reflect.Field;

/**
 * @author soumik
 *
 */
public class ArduinoNumericField {
	
	private Field field;
	private ArduinoNumericFieldType arduinoNumber;
	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}
	/**
	 * @return the arduinoNumber
	 */
	public ArduinoNumericFieldType getArduinoNumber() {
		return arduinoNumber;
	}
	/**
	 * @param arduinoNumber the arduinoNumber to set
	 */
	public void setArduinoNumber(ArduinoNumericFieldType arduinoNumber) {
		this.arduinoNumber = arduinoNumber;
	}

}
