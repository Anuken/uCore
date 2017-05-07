package io.anuke.ucore.lights;

import com.badlogic.gdx.math.Vector2;

public class Casting{
	static Vector2 vector = new Vector2();

	public static Vector2 cast(float startx, float starty, float endx, float endy, float x, float y, float halfx, float halfy){
		float posx = startx, posy = starty, 
				deltax = endx-startx, deltay = endy-starty;
		
		Vector2 hit = vector;

		float paddingX = 0f;
		float paddingY = 0f;

		float scaleX = 1.0f / deltax;
		float scaleY = 1.0f / deltay;
		int signX = sign(scaleX);
		int signY = sign(scaleY);
		float nearTimeX = (x - signX * (halfx + paddingX) - posx) * scaleX;
		float nearTimeY = (y - signY * (halfy + paddingY) - posy) * scaleY;
		float farTimeX = (x + signX * (halfx + paddingX) - posx) * scaleX;
		float farTimeY = (y + signY * (halfy + paddingY) - posy) * scaleY;

		if(nearTimeX > farTimeY || nearTimeY > farTimeX)
			return null;

		float nearTime = nearTimeX > nearTimeY ? nearTimeX : nearTimeY;
		float farTime = farTimeX < farTimeY ? farTimeX : farTimeY;

		if(nearTime >= 1 || farTime <= 0)
			return null;

		float htime = clamp(nearTime);
		float hdeltax = htime * deltax;
		float hdeltay = htime * deltay;
		hit.x = posx + hdeltax;
		hit.y = posy + hdeltay;
		return hit;
	}
	
	static float clamp(float f){
		if(f < 0) return 0;
		if(f > 1) return 1;
		return f;
	}

	static int sign(float f){
		return(f < 0 ? -1 : 1);
	}
}
