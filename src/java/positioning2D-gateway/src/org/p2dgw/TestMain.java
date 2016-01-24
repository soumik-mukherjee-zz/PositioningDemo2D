/**
 * 
 */
package org.p2dgw;

import org.sardp4j.AbstractArduinoPort;


/**
 * @author soumik
 *
 */
public class TestMain extends AbstractArduinoPort<TestPayloadType>{

	static TestMain obj;
	public TestMain() throws InstantiationException, IllegalAccessException {
		super("/dev/cu.usbmodemFD121", TestPayloadType.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			obj = new TestMain();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.open();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			   @Override
			   public void run() {
			    obj.close();
			   }
			  });
		obj.registerForSerialEvents();
		//System.out.println(String.format("%1$0#2X %2$0#2X %3$0#2X %4$0#2X", bytes[0], bytes[1], bytes[2], bytes[3]));
		
	}
	@Override
	public void onDataRecieved(TestPayloadType payload) {
		System.out.println("New data recieved");
		System.out.println(String.format("Hex data: field1 = %1$0#2x, field2 = %2$0#2x, field3 = %3$0#2x", payload.getField1(), payload.getField2(), payload.getField3()));
		System.out.println(String.format("Decimal data: field1 = %1$d, field2 = %2$d, field3 = %3$d", payload.getField1(), payload.getField2(), payload.getField3()));
	}

}
