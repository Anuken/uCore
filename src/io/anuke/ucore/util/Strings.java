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
		String out = s.substring(0, 1).toUpperCase()+s.substring(1);
		int idx = out.indexOf('_');
		if(idx != -1){
			out = out.substring(0, idx) + " " + Character.toUpperCase(out.charAt(idx+1)) + out.substring(idx+2);
		}
		return out;
	}
	
	public static boolean canParseInt(String s){
		return parseInt(s) != Integer.MIN_VALUE;
	}
	
	public static boolean canParsePostiveInt(String s){
		int p = parseInt(s);
		return p > 0;
	}
	
	/**Returns Integer.MIN_VALUE if parsing failed.*/
	public static int parseInt(String s){
		try{
			return Integer.parseInt(s);
		}catch (Exception e){
			return Integer.MIN_VALUE;
		}
	}
	
	/**Returns Float.NEGATIVE_INFINITY if parsing failed.*/
	public static float parseFloat(String s){
		try{
			return Float.parseFloat(s);
		}catch (Exception e){
			return Float.NEGATIVE_INFINITY;
		}
	}
	
	public static String toFixed(double d, int decimalPlaces) {
	    if (decimalPlaces < 0 || decimalPlaces > 8) {
	        throw new IllegalArgumentException("Unsupported number of "
	                + "decimal places: " + decimalPlaces);
	    }
	    String s = "" + Math.round(d * Math.pow(10, decimalPlaces));
	    int len = s.length();
	    int decimalPosition = len - decimalPlaces;
	    StringBuilder result = new StringBuilder();
	    if (decimalPlaces == 0) {
	        return s;
	    } else if (decimalPosition > 0) {
	        // Insert a dot in the right place
	        result.append(s.substring(0, decimalPosition));
	        result.append(".");
	        result.append(s.substring(decimalPosition));
	    } else {
	        result.append("0.");
	        // Insert leading zeroes into the decimal part
	        while (decimalPosition++ < 0) {
	            result.append("0");
	        }
	        result.append(s);
	    }
	    return result.toString();
	}
}
