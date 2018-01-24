package io.anuke.ucore.lsystem;

import java.util.Stack;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.graphics.Lines;

public class LSystem{
	protected LSystemData data;
	protected String string;
	
	protected final Array<Line> lines = new Array<Line>();
	protected Stack<Vector3> stack = new Stack<>();
	protected int maxstack = 0;
	
	protected boolean moving = false;
	
	protected float angle = 90;
	
	protected float lastx, lasty;
	
	public float x, y, timeOffset;
	public boolean sortMode = true;
	public boolean sort = true;
	public boolean colorBoost = false;
	
	public LSystem(LSystemData data){
		this.data = data;
		string = LGen.gen(data.axiom, data.rules, data.iterations);
	}
	
	public LSystem(FileHandle file){
		this(new Json().fromJson(LSystemData.class, file));
	}
	
	public void draw(){
		Lines.stroke(data.thickness);
		
		if(sort){
			lines.clear();
		}
		
		angle = 90;
		lastx = lasty = 0;
		
		for(int i = 0; i < string.length(); i ++){
			drawc(string.charAt(i));
		}
		
		if(sort){
			lines.sort();
			drawLines();
		}
			
		Draw.color();
	}
	
	protected void drawLines(){
		for(Line line : lines){
			Draw.color(data.start, data.end, (float)line.stack/(maxstack - (colorBoost ? 2 : 0)));
			Lines.line(line.x1 + x, line.y1 + y, line.x2 + x, line.y2 + y);
		}
	}
	
	protected void drawForward(){
		float sway = data.swayscl*MathUtils.sin((timeOffset+getTime())/data.swayphase+stack.size()*data.swayspace);
		
		float radians = MathUtils.degRad*(-angle+180 + sway);
		
		float nx = data.len;
		float ny = 0;
		float cos = MathUtils.cos(radians);
		float sin = MathUtils.sin(radians);
		float newX = nx * cos - ny * sin;
		float newY = nx * sin + ny * cos;
		
		nx = newX;
		ny = newY;
		
		float scl = (float)stack.size()/(maxstack);
		
		if(sort){
			lines.add(new Line(stack.size(), lastx, lasty, lastx+nx, lasty+ny, scl));
		}else{
			Draw.color(data.start, data.end, scl);
			Lines.line(x + lastx, y + lasty, x + lastx+nx, y + lasty+ny);
		}
		
		lastx += nx;
		lasty += ny;
	}
	
	protected float getTime(){
		return Timers.time();
	}
	
	protected void push(){
		stack.push(new Vector3(lastx, lasty, angle));
		maxstack = Math.max(stack.size(), maxstack);
	}
	
	protected void pop(){
		if(stack.isEmpty()) return;
		Vector3 vec = stack.pop();
		lastx = vec.x;
		lasty = vec.y;
		angle = vec.z;
	}
	
	protected void drawc(char c){
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
	
	protected class Line implements Comparable<Line>{
		public int stack;
		public float x1, y1, x2, y2;
		public float color;
		
		Line(int stack, float x1, float y1, float x2, float y2, float color){
			this.stack = stack;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
		}

		@Override
		public int compareTo(Line l){
			return (l.stack == stack) ? 0 : sortMode ? (stack > l.stack ? -1 : 1) : (stack > l.stack ? 1 : -1);
		}
		
	}
}
