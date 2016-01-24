/**
 * 
 */
package org.sardp4j;

/**
 * @author soumik
 *
 */
public class UInt32 extends Number implements Comparable<UInt32>{

	public static final long MIN_VALUE = 0;
	public static final long MAX_VALUE = 4_294_967_295L;
	public static final Class<UInt16> TYPE = UInt16.class;
	public static final int SIZE = 16;
	public static final int BYTES = 2;
	
	private byte[] bytes;
	private Long unsignedLong;
	
	public UInt32(byte[] msb2lsbBytes){
		if (msb2lsbBytes.length != 4)
			throw new IllegalArgumentException ("msb2lsbBytes byte array must have exactly 4 elements.");
		this.bytes = msb2lsbBytes;
		long data = Byte.toUnsignedLong(bytes[0]);
		data <<= 8;
		data |= Byte.toUnsignedLong(bytes[1]);
		data <<= 8;
		data |= Byte.toUnsignedLong(bytes[2]);
		data <<= 8;
		data |= Byte.toUnsignedLong(bytes[3]);
		this.unsignedLong = data;
	}
	
	public UInt32 (long unsignedValue){
		if (unsignedValue > UInt32.MAX_VALUE){
			String exMsg = String.format("The value %1$d of the parameter %2$s is greater than the maximum permissible value of %3$s", unsignedValue, "\"unsignedValue\"", UInt32.MAX_VALUE);
			throw new IllegalArgumentException (exMsg);
		}
		if (unsignedValue < UInt32.MIN_VALUE){
			String exMsg = String.format("The value %1$d of the parameter %2$s is less than the minimum permissible value of %3$s", unsignedValue, "\"unsignedValue\"", UInt32.MIN_VALUE);
			throw new IllegalArgumentException (exMsg);
		}
		bytes = new byte[4];
		this.unsignedLong = unsignedValue;
		bytes[3] = (byte) unsignedValue;
		long tempVal = unsignedValue;
		tempVal >>= 8;
		bytes[2] = (byte) tempVal;
		tempVal >>= 8;
		bytes[1] = (byte) tempVal;
		tempVal >>= 8;
		bytes[0] = (byte) tempVal;
	}
	
	@Override
	public int compareTo(UInt32 o) {
		return unsignedLong.compareTo(o.longValue());
	}

	@Override
	public int intValue() {
		return unsignedLong.intValue();
		/*String exMsg = String.format("This type does not allow a Java integer type conversion");
		throw new UnsupportedOperationException(exMsg);*/
	}

	@Override
	public long longValue() {
		return unsignedLong;
	}

	@Override
	public float floatValue() {
		return unsignedLong.floatValue();
	}

	@Override
	public double doubleValue() {
		return unsignedLong.doubleValue();
	}

}
