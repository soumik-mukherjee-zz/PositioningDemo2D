/**
 * 
 */
package org.mik;

/**
 * @author soumik
 *
 */
public class UInt16 extends Number implements Comparable<UInt16>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2571545928061500160L;
	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 65535;
	public static final Class<UInt16> TYPE = UInt16.class;
	public static final int SIZE = 16;
	public static final int BYTES = 2;
	
	private byte lsb;
	private byte msb;
	private Integer unsignedInt;
	
	public UInt16(byte lsb, byte msb){
		this.lsb = lsb;
		this.msb = msb;
		int data = Byte.toUnsignedInt(lsb);
		data <<= 8;
		data |= Byte.toUnsignedInt(msb);
		this.unsignedInt = data;
	}
	
	public UInt16 (Integer unsignedIntValue){
		if (unsignedIntValue > UInt16.MAX_VALUE){
			String exMsg = String.format("The value %1$d of the parameter %2$s is greater than the maximum permissible value of %3$s", unsignedIntValue, "\"unsignedIntValue\"", UInt16.MAX_VALUE);
			throw new IllegalArgumentException (exMsg);
		}
		if (unsignedIntValue < UInt16.MIN_VALUE){
			String exMsg = String.format("The value %1$d of the parameter %2$s is less than the minimum permissible value of %3$s", unsignedIntValue, "\"unsignedIntValue\"", UInt16.MIN_VALUE);
			throw new IllegalArgumentException (exMsg);
		}
		this.unsignedInt = unsignedIntValue;
		Integer tempVal = unsignedIntValue >> 8;
		this.msb = tempVal.byteValue();	
		this.lsb = unsignedIntValue.byteValue();
	}
	
	public int compareTo(UInt16 o) {
		return this.unsignedInt.compareTo(o.intValue());
	}

	@Override
	public int intValue() {
		return unsignedInt;
	}

	@Override
	public long longValue() {
		return Integer.toUnsignedLong(unsignedInt);
	}

	@Override
	public float floatValue() {
		return unsignedInt.floatValue();
	}

	@Override
	public double doubleValue() {
		return unsignedInt.doubleValue();
	}

	/**
	 * @return the lsb
	 */
	public byte getLsb() {
		return lsb;
	}

	/**
	 * @return the msb
	 */
	public byte getMsb() {
		return msb;
	}

}
