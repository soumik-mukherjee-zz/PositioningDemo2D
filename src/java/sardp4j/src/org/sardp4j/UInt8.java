/**
 * 
 */
package org.sardp4j;

/**
 * @author soumik
 *
 */
public class UInt8 extends Number implements Comparable<UInt8>{

	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 255;
	public static final Class<UInt8> TYPE = UInt8.class;
	public static final int SIZE = 8;
	public static final int BYTES = 1;
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -5651729473840459948L;
	private byte value;
	private Integer unsignedInt;
	
	public UInt8(byte value){
		this.value = value;
		this.unsignedInt = Byte.toUnsignedInt(value);
	}
	
	public UInt8(int unsignedInt){
		if (unsignedInt > UInt8.MAX_VALUE){
			String exMsg = String.format("The value %1$d of the parameter %2$s is greater than the maximum permissible value of %3$s", unsignedInt, "\"unsignedInt\"", UInt8.MAX_VALUE);
			throw new IllegalArgumentException (exMsg);
		}
		if (unsignedInt < UInt8.MIN_VALUE){
			String exMsg = String.format("The value %1$d of the parameter %2$s is less than the minimum permissible value of %3$s", unsignedInt, "\"unsignedInt\"", UInt8.MIN_VALUE);
			throw new IllegalArgumentException (exMsg);
		}
		this.unsignedInt = unsignedInt;
		this.value = (byte)unsignedInt;
	}
	
	@Override
	public int compareTo(UInt8 o) {
		return this.unsignedInt.compareTo(o.intValue());
		/*if (this.unsignedInt > o.intValue()){
			retVal = 1;
		}
		else if (this.unsignedInt < o.in)*/
	}

	@Override
	public int intValue() {
		return unsignedInt;
	}

	@Override
	public long longValue() {
		return Byte.toUnsignedLong(value);
	}

	@Override
	public float floatValue() {
		return unsignedInt.floatValue();
	}

	@Override
	public double doubleValue() {
		return unsignedInt.doubleValue();
	}

}
