package io.anuke.ucore.util;

//TODO
public class Bits{

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
}
