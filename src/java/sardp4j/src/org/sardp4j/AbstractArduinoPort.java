/**
 * 
 */
package org.sardp4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * @author soumik
 * @param <T>
 *
 */
public abstract class AbstractArduinoPort<T> implements IPort<T>, SerialPortEventListener{

	private SerialPort serialPort;
	private T payload;
	@SuppressWarnings("unused")
	private Class<T> payloadType;
	private int payloadByteCount;
	private SortedMap<Integer, ArduinoNumericField> seqSortedFieldsMap;
	
	public AbstractArduinoPort (String portName, Class<T> payloadType) throws InstantiationException, IllegalAccessException{
		payloadByteCount = 0;
		seqSortedFieldsMap = new TreeMap <Integer, ArduinoNumericField>();
		serialPort = new SerialPort(portName);
		this.payloadType = payloadType;
		payload = payloadType.newInstance();
		Field[] fields = payloadType.getDeclaredFields();
		for (Field field:fields){
			if (field.isAnnotationPresent(ArduinoNumericFieldType.class)){
				field.setAccessible(true);// fields would be private
				ArduinoNumericFieldType arduinoNumber = field.getAnnotation(ArduinoNumericFieldType.class);
				ArduinoNumericField arduinoField = new ArduinoNumericField();
				arduinoField.setField(field);
				arduinoField.setArduinoNumber(arduinoNumber);
				seqSortedFieldsMap.put(arduinoNumber.Sequence(), arduinoField);
				payloadByteCount += getLengthInBytes(arduinoNumber.ArduinoNumberType());
			}
		}
		//payloadByteCount += 2;
	}
	
	@Override
	public T read(int count) throws IllegalAccessException, InvocationTargetException{
		byte[] buffer = null;
		T retObj = null;
		try {
			buffer = serialPort.readBytes(count);
			retObj = payloadType.newInstance();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Read 10 bytes from serial port
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Printing raw Rx buffer");
		StringBuffer sbf = new StringBuffer ();
		String hexFld = "%1$0#2x ";
		for (byte raw:buffer){
			sbf.append(String.format(hexFld,raw));
		}
		System.out.println(sbf.toString());
		//ByteBuffer bytes = ByteBuffer.allocate(payloadByteCount);
		ByteBuffer bytes = ByteBuffer.allocate(count);
		bytes.order(ByteOrder.LITTLE_ENDIAN);
		bytes.put(buffer);
		bytes.flip();
		Collection<ArduinoNumericField> sortedFields = seqSortedFieldsMap.values();
		System.out.println("Printing ByteBuffer data");
//		byte raw = bytes.get();
//		System.out.print(String.format(hexFld, raw));
		//if (raw == 'S'){
			for (ArduinoNumericField field:sortedFields){
				Field javaField = field.getField();
				ArduinoNumericFieldType arduinoType = field.getArduinoNumber();
				switch (arduinoType.ArduinoNumberType()){
					case UInt8_t:
						// read 1 byte
						byte value = bytes.get();
						System.out.print(String.format(hexFld, value));
						UInt8 uInt8 = new UInt8(value);
						//System.out.println(String.format("%1$s value is %2$d",javaField.getName(),uInt8.intValue()));
						BeanUtils.setProperty(retObj, javaField.getName(), uInt8.intValue());
						break;
					case UInt16_t:
						byte lsb = bytes.get();
						System.out.print(String.format(hexFld, lsb));
						byte msb = bytes.get();
						System.out.print(String.format(hexFld, msb));
						UInt16 uInt16 = new UInt16(msb, lsb);
						//System.out.println(String.format("%1$s value is %2$d",javaField.getName(),uInt16.intValue()));
						BeanUtils.setProperty(retObj, javaField.getName(), uInt16.intValue());
						break;
					case UInt32_t:
						byte[] data = new byte[4];
						data[3] = bytes.get();
						data[2] = bytes.get();
						data[1] = bytes.get();
						data[0] = bytes.get();
						System.out.print(String.format(hexFld, data[3]));
						System.out.print(String.format(hexFld, data[2]));
						System.out.print(String.format(hexFld, data[1]));
						System.out.print(String.format(hexFld, data[0]));
						UInt32 uInt32 = new UInt32(data);
						//System.out.println(String.format("%1$s value is %2$d",javaField.getName(),uInt32.longValue()));
						BeanUtils.setProperty(retObj, javaField.getName(), uInt32.longValue());
						break;
				}
			//}
			//raw = bytes.get();
//			System.out.print(String.format(hexFld, raw));
//			if (raw != 'E'){
//				//throw new IllegalAccessException();
//			}
//			else{
//				System.out.print("\n\r");
//			}
		}
		
		bytes.clear();
		return retObj;
	}

	@Override
	public void write(T payload){
        try {
			serialPort.writeBytes("This is a test string".getBytes());
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void open (){
		//Open serial port
        try {
        	serialPort.openPort();
        	serialPort.setParams(SerialPort.BAUDRATE_57600, 
			                     SerialPort.DATABITS_8,
			                     SerialPort.STOPBITS_1,
			                     SerialPort.PARITY_NONE);
        	serialPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXABORT | SerialPort.PURGE_TXCLEAR);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void close(){
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void registerForSerialEvents(){
		int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
		try {
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(this);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private int getLengthInBytes(ArduinoNumericType type){
		int retVal = 0;
		switch (type){
		case UInt8_t:
			retVal = 1;
			break;
		case UInt16_t:
			retVal = 2;
			break;
		case UInt32_t:
			retVal = 4;
			break;
		}
		return retVal;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		if(arg0.isRXCHAR()){
			T payload = null;
			try {
				System.out.println("Bytes to read : " + arg0.getEventValue());
				if (arg0.getEventValue() == payloadByteCount){
					payload = read(arg0.getEventValue());
					onDataRecieved (payload);
				}
					
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		}
		
    }
	
	public abstract void onDataRecieved (T payload);
}
