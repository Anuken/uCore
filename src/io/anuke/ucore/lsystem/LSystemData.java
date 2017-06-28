package io.anuke.ucore.lsystem;

import com.badlogic.gdx.graphics.Color;

public class LSystemData{
	public Color start;
	public Color end;
	public float space;
	public float len;
	public float swayscl;
	public float swayphase;
	public float swayspace;
	public int iterations;
	public String string;
	
	public LSystemData(String string, int iterations, float swayspace, float swayphase, float swayscl, 
			float len, float space, Color start, Color end){
		this.string = string;
		this.iterations = iterations;
		this.swayphase = swayphase;
		this.swayspace = swayspace;
		this.swayscl = swayscl;
		this.len = len;
		this.space = space;
		this.start = start;
		this.end = end;
	}
	
	private LSystemData(){}
}
