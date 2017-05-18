package io.anuke.ucore.util;

public class Strings{
	public static String parseException(Exception e){
		StringBuilder build = new StringBuilder();
		
		build.append(e.getClass().getName() + ": " +e.getMessage());
		
		for(StackTraceElement s : e.getStackTrace()){
			build.append("\n"+s.toString());
		}
		return build.toString();
	}
	
	public static String capitalize(String s){
		return s.substring(0, 1).toUpperCase()+s.substring(1);
	}
	
	/**Returns Integer.MIN_VALUE if parsing failed.*/
	public static int parseInt(String s){
		try{
			return Integer.parseInt(s);
		}catch (Exception e){
			return Integer.MIN_VALUE;
		}
	}
	
	/**Returns Float.NaN if parsing failed.*/
	public static float parseFloat(String s){
		try{
			return Float.parseFloat(s);
		}catch (Exception e){
			return Float.NaN;
		}
	}
}
