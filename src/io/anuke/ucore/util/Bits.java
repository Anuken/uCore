package io.anuke.ucore.util;

//TODO
public class Bits{
	
	public int packInt(short left, short right){
		return (left << 16) | (right & 0xFFF);
	}
	
	 public int getLeftShort(int field) {
		 return field >>> 16;
	 }

	 public int getRightShort(int field) {
		 return field & 0xFFF;
	 }
}
