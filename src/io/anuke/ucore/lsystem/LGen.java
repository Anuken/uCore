package io.anuke.ucore.lsystem;

import java.util.HashMap;

public class LGen{
	
	public static String gen(String axiom, int iterations, Object... objects){
		 HashMap<Character, String> map = new HashMap<>();
		 
		 for(int i = 0; i < objects.length; i += 2){
			 map.put((char)objects[i], (String)objects[i+1]);
		 }
		 
		 StringBuilder out = new StringBuilder(axiom);
		 StringBuilder temp = new StringBuilder();
		 
		 for(int i = 0; i < iterations; i ++){
			 temp = new StringBuilder(out);
			 out.setLength(0);
			 for(int z = 0; z < temp.length(); z ++){
				 char c = temp.charAt(z);
				 
				 if(map.containsKey(c)){
					 out.append(map.get(c));
				 }else{
					 out.append(c);
				 }
			 }
			 
		 }
		 
		 return out.toString();
	}
	
}
