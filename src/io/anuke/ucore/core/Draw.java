package io.anuke.ucore.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.NumberUtils;

import io.anuke.ucore.graphics.PixmapUtils;

public class Draw{
	private static TextureRegion region = PixmapUtils.blankTextureRegion();
	private static float thickness = 1f;
	private static Vector2 vector = new Vector2();
	private static Vector2[] circle = new Vector2[30];
	
	static{
		float step = 360f/circle.length;
		vector.set(1f, 0);
		for(int i = 0; i < circle.length; i ++){
			vector.setAngle(i*step);
			circle[i] = vector.cpy();
		}
	}
	
	/**Sets the batch color to this color AND the previous alpha.*/
	public static void tint(Color color){
		color(color.r, color.g, color.b, batch().getColor().a);
	}
	
	public static void color(Color color){
		batch().setColor(color);
	}
	
	public static void color(int color){
		batch().setColor(NumberUtils.intBitsToFloat(color));
	}
	
	public static void color(){
		color(Color.WHITE);
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
				region.getRegionWidth()/2, region.getRegionHeight()/2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}
	
	public static void rect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch().draw(region, x - w, y - h/2, w, h);
	}
	
	public static void laser(String line, String edge, float x, float y, float x2, float y2){
		laser(line, edge, x, y, x2, y2, vector.set(x2 - x, y2 -y).angle());
	}
	
	public static void laser(String line, String edge, float x, float y, float x2, float y2, float rotation){
		
		//Draw.colorl(0.75f + MathUtils.random(0.2f) + Math.abs(MathUtils.sin(Timers.time()/3f)/4f));
		
		thickness = 12f;
		Draw.line(region(line),x, y, x2, y2);
		thickness = 1f;
		
		Draw.rect(edge, x, y, rotation + 180);
		
		Draw.rect(edge, x2, y2, rotation);
		
		//Draw.color();
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
	
	public static void circle(float x, float y, float scl){
		polygon(circle, x, y, scl);
	}
	
	public static void spike(float x, float y, float len1, float len2, int spikes, float rot){
		vector.set(0, 1);
		float step = 360f/spikes;
		
		for(int i = 0; i < spikes; i ++){
			vector.setAngle(i*step+rot);
			vector.setLength(len1);
			float x1 = vector.x, y1 = vector.y;
			vector.setLength(len2);
			
			line(x+x1, y+y1, x+vector.x, y+vector.y);
		}
	}
	
	public static void spike(float x, float y, float len1, float len2, int spikes){
		spike(x, y, len1, len2, spikes, 0);
	}
	
	public static void polygon(Vector2[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0;i < vertices.length;i ++){
			Vector2 current = vertices[i];
			Vector2 next = i == vertices.length - 1 ? vertices[0] : vertices[i + 1];
			line(current.x * scl + offsetx, current.y * scl + offsety, next.x * scl + offsetx, next.y * scl + offsety);
		}
	}
	
	public static void polygon(float[] vertices, float offsetx, float offsety, float scl){
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
	
	public static void text(String text, float x, float y){
		text(text, x, y, Align.center);
	}
	
	public static void text(String text, float x, float y, int align){
		DrawContext.font.draw(batch(), text, x, y, 0, align, false);
	}
	
	public static void tcolor(Color color){
		DrawContext.font.setColor(color);
	}
	
	public static void tcolor(float r, float g, float b, float a){
		DrawContext.font.setColor(r, g, b, a);
	}
	
	public static void tcolor(float r, float g, float b){
		DrawContext.font.setColor(r, g, b, 1f);
	}
	
	public static void tcolor(){
		DrawContext.font.setColor(Color.WHITE);
	}
	
	public static TextureRegion region(String name){
		return DrawContext.provider.getRegion(name);
	}
	
	private static SpriteBatch batch(){
		return DrawContext.batch;
	}
}
