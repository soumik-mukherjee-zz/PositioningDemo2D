/**
 * 
 */
package org.p2dgw;

import org.sardp4j.AbstractArduinoPort;

/**
 * @author soumik
 *
 */
public class Main extends AbstractArduinoPort<GeoDemoPayloadType>{
	
	private static final String OSX_COM_PORT_NAME = "/dev/cu.usbmodemFD121";
	private static final int US_ROUNDTRIP_CM = 57; // time in micro-seconds it take for sound to cover a round trip distance of 1cm (actually 2cm)
	private static Main obj;
	public Main()
			throws InstantiationException, IllegalAccessException {
		super(OSX_COM_PORT_NAME, GeoDemoPayloadType.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			obj = new Main();
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
	}

	@Override
	public void onDataRecieved(GeoDemoPayloadType payload) {
		System.out.println("New data recieved");
		//System.out.println(String.format("Hex data: xUs = %1$0#2x, yUs = %2$0#2x", payload.getXUs(), payload.getYUs()));
		//System.out.println(String.format("Decimal data: xUs = %1$d, yUs = %2$d", payload.getXUs(), payload.getYUs()));
		double x = payload.getXUs()/US_ROUNDTRIP_CM;
		double y = payload.getYUs()/US_ROUNDTRIP_CM;
		System.out.println(String.format("Decimal data: x = %1$0#f cm, y = %2$0#f cm", x, y));
	}
}
