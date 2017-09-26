package io.anuke.ucore.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Physics{
	static Vector2 vector = new Vector2();
	
	public static Vector2 raycastRect(float startx, float starty, float endx, float endy, Rectangle rectangle){
		return raycastRect(startx, starty, endx, endy, rectangle.x + rectangle.width/2, rectangle.y + rectangle.height/2, 
				rectangle.width/2f, rectangle.height/2f);
	}

	public static Vector2 raycastRect(float startx, float starty, float endx, float endy, float x, float y, float halfx, float halfy){
		float posx = startx, posy = starty, 
				deltax = endx-startx, deltay = endy-starty;
		
		Vector2 hit = vector;

		float paddingX = 0f;
		float paddingY = 0f;

		float scaleX = 1.0f / deltax;
		float scaleY = 1.0f / deltay;
		int signX = Mathf.sign(scaleX);
		int signY = Mathf.sign(scaleY);
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

		float htime = Mathf.clamp(nearTime);
		float hdeltax = htime * deltax;
		float hdeltay = htime * deltay;
		hit.x = posx + hdeltax;
		hit.y = posy + hdeltay;
		return hit;
	}
	
	/**Checks for collisions between two rectangles, and returns the correct delta vector of A.
	 * Note: The same vector instance is returned each time!*/
	public static Vector2 overlap(Rectangle a, Rectangle b){
		Vector2 normal = vector;
		float penetration = 0f;
		
		float ax = a.x+a.width/2, bx = b.x+b.width/2;
		float ay = a.y+a.height/2, by = b.y+b.height/2;
		
        // Vector from A to B
        float nx = ax - bx,
            ny = ay - by;

        // Calculate half extends along x axis
        float aex = a.width / 2,
            bex = b.width / 2;

        // Overlap on x axis
        float xoverlap = aex + bex - Math.abs(nx);
        if (xoverlap > 0) {

            // Calculate half extends along y axis
            float aey = a.height / 2,
                bey = b.height / 2;

            // Overlap on x axis
            float yoverlap = aey + bey - Math.abs(ny);
            if (!MathUtils.isEqual(yoverlap, 0)) {

                // Find out which axis is the axis of least penetration
                if (xoverlap < yoverlap) {

                    // Point towards B knowing that n points from A to B
                    normal.x = nx < 0 ? 1 : -1;
                    normal.y = 0;
                    penetration = xoverlap;

                } else {

                    // Point towards B knowing that n points from A to B
                    normal.x = 0;
                    normal.y = ny < 0 ? 1 : -1;
                    penetration = yoverlap;

                }

            }
        }
		
        float percent = 1f,
            slop = 0.05f,
            m = Math.max(penetration - slop, 0.0f) / 2f;

        // Apply correctional impulse
        float cx = m * normal.x * percent,
            cy = m * normal.y * percent;
        
        vector.x = -cx;
        vector.y = -cy;
        
        return vector;
    }
	
}
