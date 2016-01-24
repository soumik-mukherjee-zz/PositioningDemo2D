/**
 * 
 */
package org.sardp4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author soumik
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArduinoNumericFieldType {
	int Sequence();
	ArduinoNumericType ArduinoNumberType();
}
