package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ShapeUtils{
	public static TextureRegion region = PixmapUtils.blankTextureRegion();
	public static float thickness = 1f;

	public static void polygon(Batch batch, Vector2[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0;i < vertices.length;i ++){
			Vector2 current = vertices[i];
			Vector2 next = i == vertices.length - 1 ? vertices[0] : vertices[i + 1];
			line(batch, current.x * scl + offsetx, current.y * scl + offsety, next.x * scl + offsetx, next.y * scl + offsety);
		}
	}
	
	public static void polygon(Batch batch, float[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0;i < vertices.length/2; i ++){
			float x = vertices[i*2];
			float y = vertices[i*2+1];
			
			float x2 = 0, y2 = 0;
			if(i == vertices.length/2 - 1){
				x2 = vertices[0];
				y2 = vertices[1];
			}else{
				x2 = vertices[i*2+2];
				y2 = vertices[i*2+3];
			}
			
			line(batch, x * scl + offsetx, y * scl + offsety, x2 * scl + offsetx, y2 * scl + offsety);
		}
	}

	public static void line(Batch batch, float x, float y, float x2, float y2){
		float length = Vector2.dst(x, y, x2, y2) + thickness / 2;
		float angle = ((float)Math.atan2(y2 - y, x2 - x) * MathUtils.radDeg);

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
