package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ShapeUtils{
	public static TextureRegion region;
	public static float thickness = 1f;

	public static void polygon(Batch batch, Vector2[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0;i < vertices.length;i ++){
			Vector2 current = vertices[i];
			Vector2 next = i == vertices.length - 1 ? vertices[0] : vertices[i + 1];
			//drawLine(batch, current.x*scl + offsetx, current.y*scl + offsety, current.x*scl + offsetx + 2, current.y*scl + offsety + 2);
			line(batch, current.x * scl + offsetx, current.y * scl + offsety, next.x * scl + offsetx, next.y * scl + offsety);

		}
	}

	public static void line(Batch batch, float x, float y, float x2, float y2){
		float length = Vector2.dst(x, y, x2, y2) + thickness / 2;
		int angle = (int)((float)Math.atan2(y2 - y, x2 - x) * MathUtils.radDeg);

		//batch.draw(texture, x - thickness/2, y - thickness/2, thickness/2, thickness/2, length, thickness, 1f, 1f, angle, 0, 0, 1, 1, false, false);
		batch.draw(region, x - thickness / 2, y - thickness / 2, thickness / 2, thickness / 2, length, thickness, 1f, 1f, angle);
	}
	
	public static void rect(Batch batch, float x, float y, float width, float height, int thickness){
		rect(batch, x, y, width, height, thickness, 0);
	}

	public static void rect(Batch batch, float x, float y, float width, float height, int thickness, int space){
		rect(batch, x, y, width, height, thickness, space, space);
	}

	public static void rect(Batch batch, float x, float y, float width, float height, int thickness, int xspace, int yspace){
		x -= xspace;
		y -= yspace;
		width += xspace * 2;
		height += yspace * 2;

		batch.draw(region, x, y, width, thickness);
		batch.draw(region, x, y + height, width, -thickness);

		batch.draw(region, x + width, y, -thickness, height);
		batch.draw(region, x, y, thickness, height);
	}
}
