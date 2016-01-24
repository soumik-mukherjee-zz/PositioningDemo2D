/**
 * 
 */
package org.sardp4j;

import java.lang.reflect.InvocationTargetException;

import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * @author soumik
 *
 */
public interface IPort <T>{
	T read (int count)  throws IllegalAccessException, InvocationTargetException;
	void write(T payload);
	void open();
	void close();
	void registerForSerialEvents();
}
