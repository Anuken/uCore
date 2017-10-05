package io.anuke.ucore.lsystem;

import java.util.Stack;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Timers;

public class LSystem{
	private LSystemData data;
	private String string;
	
	private Stack<Vector3> stack = new Stack<>();
	private int maxstack = 0;
	
	private boolean moving = false;
	
	private float angle = 90;
	
	private float lastx, lasty;
	
	public float x, y, timeOffset;
	
	public LSystem(LSystemData data){
		this.data = data;
		string = LGen.gen(data.axiom, data.rules, data.iterations);
	}
	
	public LSystem(FileHandle file){
		this(new Json().fromJson(LSystemData.class, file));
	}
	
	public void draw(){
		Draw.thick(data.thickness);
		angle = 90;
		lastx = lasty = 0;
		for(int i = 0; i < string.length(); i ++){
			drawc(string.charAt(i));
		}
		Draw.color();
	}
	
	private void drawForward(){
		float sway = data.swayscl*MathUtils.sin(timeOffset+Timers.time()/data.swayphase+stack.size()*data.swayspace);
		
		float radians = MathUtils.degRad*(-angle+180 + sway);
		
		float nx = data.len;
		float ny = 0;
		float cos = MathUtils.cos(radians);
		float sin = MathUtils.sin(radians);
		float newX = nx * cos - ny * sin;
		float newY = nx * sin + ny * cos;
		
		nx = newX;
		ny = newY;
		
		float scl = (float)stack.size()/(maxstack-2);
		
		Draw.color(data.start, data.end, scl);
		Draw.line(x + lastx, y + lasty, x + lastx+nx, y + lasty+ny);
		
		lastx += nx;
		lasty += ny;
	}
	
	private void push(){
		stack.push(new Vector3(lastx, lasty, angle));
		maxstack = Math.max(stack.size(), maxstack);
	}
	
	private void pop(){
		if(stack.isEmpty()) return;
		Vector3 vec = stack.pop();
		lastx = vec.x;
		lasty = vec.y;
		angle = vec.z;
	}
	
	private void drawc(char c){
		if(c == 'F'){
			drawForward();
		}else if(c == '-'){
			angle -= data.space;
		}else if(c == '+'){
			angle += data.space;
		}else if(c == '['){
			push();
		}else if(c == ']'){
			pop();
		}
	}
}
