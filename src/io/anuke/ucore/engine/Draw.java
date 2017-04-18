package io.anuke.ucore.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.graphics.PixmapUtils;

public class Draw{
	private static TextureRegion region = PixmapUtils.blankTextureRegion();
	private static float thickness = 1f;
	
	/**Sets the batch color to this color AND the previous alpha.*/
	public static void tint(Color color){
		color(color.r, color.g, color.b);
	}
	
	public static void color(Color color){
		batch().setColor(color);
	}
	
	public static void color(float r, float g, float b){
		batch().setColor(r, g, b, 1f);
	}
	
	public static void color(float r, float g, float b, float a){
		batch().setColor(r, g, b, a);
	}
	
	public static void alpha(float alpha){
		Color color = batch().getColor();
		batch().setColor(color.r, color.g, color.b, alpha);
	}
	
	public static void rect(String name, float x, float y){
		TextureRegion region = region(name);
		batch().draw(region, x - region.getRegionWidth()/2, y - region.getRegionHeight()/2);
	}
	
	public static void rect(String name, float x, float y, float rotation){
		TextureRegion region = region(name);
		batch().draw(region, x - region.getRegionWidth()/2, y - region.getRegionHeight()/2, 
				0, 0, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}
	
	public static void rect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch().draw(region, x - w, y - h/2, w, h);
	}
	
	public static void line(float x, float y, float x2, float y2){
		float length = Vector2.dst(x, y, x2, y2) + thickness / 2;
		float angle = ((float)Math.atan2(y2 - y, x2 - x) * MathUtils.radDeg);

		batch().draw(region, x - thickness / 2, y - thickness / 2, thickness / 2, thickness / 2, length, thickness, 1f, 1f, angle);
	}
	
	public static void line(TextureRegion texture, float x, float y, float x2, float y2){
		float length = Vector2.dst(x, y, x2, y2) + thickness / 2;
		float angle = ((float)Math.atan2(y2 - y, x2 - x) * MathUtils.radDeg);

		batch().draw(texture, x - thickness / 2, y - thickness / 2, thickness / 2, thickness / 2, length, thickness, 1f, 1f, angle);
	}
	
	public static void polygon(Batch batch, Vector2[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0;i < vertices.length;i ++){
			Vector2 current = vertices[i];
			Vector2 next = i == vertices.length - 1 ? vertices[0] : vertices[i + 1];
			line(current.x * scl + offsetx, current.y * scl + offsety, next.x * scl + offsetx, next.y * scl + offsety);
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
			
			line(x * scl + offsetx, y * scl + offsety, x2 * scl + offsetx, y2 * scl + offsety);
		}
	}
	
	public static void linerect(float x, float y, float width, float height, int xspace, int yspace){
		x -= xspace;
		y -= yspace;
		width += xspace * 2;
		height += yspace * 2;

		batch().draw(region, x, y, width, thickness);
		batch().draw(region, x, y + height, width, -thickness);

		batch().draw(region, x + width, y, -thickness, height);
		batch().draw(region, x, y, thickness, height);
	}
	
	public static void linerect(float x, float y, float width, float height){
		linerect(x, y, width, height, 0);
	}

	public static void linerect(float x, float y, float width, float height,int space){
		linerect(x, y, width, height, space, space);
	}
	
	public static void thickness(float thick){
		thickness = thick;
	}
	
	public static TextureRegion region(String name){
		return DrawContext.provider.getRegion(name);
	}
	
	private static SpriteBatch batch(){
		return DrawContext.batch;
	}
}
