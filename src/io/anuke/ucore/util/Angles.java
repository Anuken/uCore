package io.anuke.ucore.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.anuke.ucore.core.Core;

public class Angles{
	static public Vector2 vector = new Vector2(1,1);
	
	static public float forwardDistance(float angle1, float angle2){
		return angle1 > angle2 ? angle1-angle2 : angle2-angle1;
	}

	static public float backwardDistance(float angle1, float angle2){
		return 360 - forwardDistance(angle1, angle2);
	}

	static public float angleDist(float a, float b){
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
		float dst = Vector2.dst(x, y, x2, y2);
		dst /= speed;
		x2 += velocityx*dst;
		y2 += velocityy*dst;
		return angle(x, y, x2, y2);
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
}

