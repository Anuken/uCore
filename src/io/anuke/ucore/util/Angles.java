package io.anuke.ucore.util;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.PositionConsumer;

public class Angles{
	private static final Random random = new Random();
	static public Vector2 vector = new Vector2(1,1);
	
	public static float x(){
		return vector.x;
	}
	
	public static float y(){
		return vector.y;
	}
	
	static public float forwardDistance(float angle1, float angle2){
		return angle1 > angle2 ? angle1-angle2 : angle2-angle1;
	}

	static public float backwardDistance(float angle1, float angle2){
		return 360 - forwardDistance(angle1, angle2);
	}

	static public float angleDist(float a, float b){
		a = a % 360f;
		b = b % 360f;
		return Math.min(forwardDistance(a, b), backwardDistance(a, b));
	}

	static public float moveToward(float angle, float to, float speed){
		if(Math.abs(angleDist(angle, to)) < speed)return to;

		if((angle > to && backwardDistance(angle, to) > forwardDistance(angle, to)) || 
				(angle < to && backwardDistance(angle, to) < forwardDistance(angle, to)) ){
			angle -= speed;
		}else{
			angle += speed;
		}
		
		return angle;
	}
	
	static public float angle(float x, float y, float x2, float y2){
		return vector.set(x2 - x, y2 -y).angle();
	}
	
	static public float predictAngle(float x, float y, float x2, float y2, float velocityx, float velocityy, float speed){
		float time = Vector2.dst(x, y, x2, y2) / speed;
		return angle(x, y, x2 + velocityx*time, y2 + velocityy*time);
	}
	
	static public Vector2 rotate(float x, float y, float angle){
		if(MathUtils.isEqual(angle, 0, 0.001f)) return vector.set(x,y);
		return vector.set(x,y).rotate(angle);
	}
	
	static public Vector2 translation(float angle, float amount){
		if(amount < 0) angle += 180f;
		return vector.setAngle(angle).setLength(amount);
	}

	static public float mouseAngle(OrthographicCamera camera, float cx, float cy){
		Vector3 avector = camera.project(new Vector3(cx, cy, 0));
		vector.set(Gdx.input.getX() - avector.x, Gdx.graphics.getHeight() - Gdx.input.getY() - avector.y);
		return vector.angle();
	}
	
	static public float mouseAngle(float cx, float cy){
		Vector3 avector = Core.camera.project(new Vector3(cx, cy, 0));
		vector.set(Gdx.input.getX() - avector.x, Gdx.graphics.getHeight() - Gdx.input.getY() - avector.y);
		return vector.angle();
	}

	public static void circle(int points, Consumer<Float> cons){
		for(int i = 0; i < points; i ++){
			cons.accept(i*360f/points);
		}
	}

	public static void circleVectors(int points, float length, PositionConsumer pos){
		for(int i = 0; i < points; i ++){
			translation(i*360f/points, length);
			pos.accept(vector.x, vector.y);
		}
	}

	public static void shotgun(int points, float spacing, float offset, Consumer<Float> cons){
		for(int i = 0; i < points; i ++){
			cons.accept(i*spacing-(points-1)*spacing/2f+offset);
		}
	}

	public static void randVectors(long seed, int amount, float length, PositionConsumer cons){
		random.setSeed(seed);
		for(int i = 0; i < amount; i ++){
			float scl = length;
			float vang = random.nextFloat()*360f;
			Tmp.v3.set(scl, 0).setAngle(vang);
			cons.accept(Tmp.v3.x, Tmp.v3.y);
		}
	}

	public static void randLenVectors(long seed, int amount, float length, PositionConsumer cons){
		random.setSeed(seed);
		for(int i = 0; i < amount; i ++){
			float scl = length * random.nextFloat();
			float vang = random.nextFloat()*360f;
			Tmp.v3.set(scl, 0).setAngle(vang);
			cons.accept(Tmp.v3.x, Tmp.v3.y);
		}
	}
}

