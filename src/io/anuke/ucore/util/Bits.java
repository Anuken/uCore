package io.anuke.ucore.util;

//TODO
public class Bits{
	static byte[] result = new byte[4];

	public static int packInt(short left, short right){
		return (left << 16) | (right & 0xFFF);
	}
	
	public static long packLong(int x, int y){
		return (((long)x) << 32) | (y & 0xffffffffL);
	}
	
	/**Packs two bytes with values 0-15 into one byte.*/
	public static byte packByte(byte left, byte right){
		return (byte) ((left << 4) | right);
	}
	
	public static byte getLeftByte(byte value){
		return (byte) ((value >> 4) & (byte) 0x0F);
	}
	
	public static byte getRightByte(byte value){
		return (byte) (value & 0x0F);
	}

	public static int getLeftShort(int field){
		return field >>> 16;
	}

	public static int getRightShort(int field){
		return field & 0xFFF;
	}
	
	/**The same array instance is returned each call.*/
	public static byte[] getBytes(int i){
	  result[0] = (byte) (i >> 24);
	  result[1] = (byte) (i >> 16);
	  result[2] = (byte) (i >> 8);
	  result[3] = (byte) (i /*>> 0*/);

	  return result;
	}
	
	/**Packs 4 bytes into an int.*/
	public static int packInt(byte[] array){
	    return ((0xFF & array[0]) << 24) | ((0xFF & array[1]) << 16) | ((0xFF & array[2]) << 8) | (0xFF & array[3]);
	}
}
