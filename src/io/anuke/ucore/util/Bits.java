package io.anuke.ucore.util;

//TODO
public class Bits{

	public static int packInt(short left, short right){
		return (left << 16) | (right & 0xFFF);
	}
	
	public static long packLong(int x, int y){
		return (((long)x) << 32) | (y & 0xffffffffL);
	}

	public static int getLeftShort(int field){
		return field >>> 16;
	}

	public static int getRightShort(int field){
		return field & 0xFFF;
	}
}
