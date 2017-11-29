package io.anuke.ucore.lsystem;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

public class LSystemData{
	public Color start;
	public Color end;
	public float space;
	public float len;
	public float swayscl;
	public float swayphase;
	public float swayspace;
	public float thickness;
	public int iterations;
	public String axiom;
	public HashMap<Character, String> rules;
	//public transient String result; //is usually null
	
	public LSystemData(String axiom, HashMap<Character, String> rules, int iterations, float swayspace, float swayphase, float swayscl, 
			float len, float space, float thickness, Color start, Color end){
		this.axiom = axiom;
		this.rules = rules;
		this.iterations = iterations;
		this.swayphase = swayphase;
		this.swayspace = swayspace;
		this.swayscl = swayscl;
		this.len = len;
		this.space = space;
		this.start = start;
		this.end = end;
		this.thickness = thickness;
	}
	
	private LSystemData(){}
	
	public void normalize(){
		HashMap<Character, String> map = new HashMap<>();
		
		for(Object o : rules.keySet()){
			map.put(((String)o).charAt(0), rules.get(o));
		}
		
		rules = map;
	}
}
