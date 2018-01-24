package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.NumberUtils;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.scene.style.Drawable;
import io.anuke.ucore.util.Tmp;

import static io.anuke.ucore.core.Core.batch;

public class Draw{

	public static void sprite(Sprite sprite){
		sprite.draw(batch);
	}

	public static void patch(String name, float x, float y, float width, float height){
		getPatch(name).draw(batch, x, y, width, height);
	}

	public static Drawable getPatch(String name){
		return Core.skin.getDrawable(name);
	}

	/** Sets the batch color to this color AND the previous alpha. */
	public static void tint(Color color){
		color(color.r, color.g, color.b, batch.getColor().a);
	}

	public static void color(Color color){
		batch.setColor(color);
	}
	
	/**Sets the color to the provided color multiplied by the factor.*/
	public static void colorl(Color color, float multiply){
		batch.setColor(Tmp.c1.set(color).mul(multiply, multiply, multiply, 1f));
	}

	/** Automatically mixes colors. */
	public static void color(Color a, Color b, float s){
		batch.setColor(Hue.mix(a, b, s, Tmp.c1));
	}

	public static void color(int color){
		batch.setColor(NumberUtils.intBitsToFloat(color));
	}

	public static void color(){
		color(Color.WHITE);
	}

	public static void color(float r, float g, float b){
		batch.setColor(r, g, b, 1f);
	}

	public static void color(float r, float g, float b, float a){
		batch.setColor(r, g, b, a);
	}

	/** Lightness color. */
	public static void colorl(float l){
		color(l, l, l);
	}

	/** Lightness color, alpha. */
	public static void colorl(float l, float a){
		color(l, l, l, a);
	}

	public static void alpha(float alpha){
		Color color = batch.getColor();
		batch.setColor(color.r, color.g, color.b, alpha);
	}

	public static void rect(Texture texture, float x, float y){
		batch.draw(texture, x - texture.getWidth() / 2, y - texture.getHeight() / 2);
	}

	public static void rect(Texture texture, float x, float y, float w, float h){
		batch.draw(texture, x - w / 2, y - h / 2, w, h);
	}
	
	public static void rect(TextureRegion region, float x, float y){
		batch.draw(region, x - region.getRegionWidth() / 2, y - region.getRegionHeight() / 2);
	}
	
	public static void rect(TextureRegion region, float x, float y, float width, float height){
		batch.draw(region, x - width / 2, y - height / 2, width, height);
	}

	public static void rect(String name, float x, float y){
		TextureRegion region = region(name);
		batch.draw(region, x - region.getRegionWidth() / 2, y - region.getRegionHeight() / 2);
	}

	/** Rectangle centered and rotated around its bottom middle point. */
	public static void grect(String name, float x, float y, float rotation){
		TextureRegion region = region(name);
		batch.draw(region, x - region.getRegionWidth() / 2f, y, region.getRegionWidth() / 2f, 0, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}

	/** Grounded rect. */
	public static void grect(String name, float x, float y){
		TextureRegion region = region(name);
		batch.draw(region, x - region.getRegionWidth() / 2, y);
	}

	/** Grounded rect. */
	public static void grect(String name, float x, float y, boolean flipx){
		TextureRegion region = region(name);
		if(flipx){
			batch.draw(region, x + region.getRegionWidth() / 2, y, -region.getRegionWidth(), region.getRegionHeight());
		}else{
			batch.draw(region, x - region.getRegionWidth() / 2, y);
		}
	}

	/** Grounded rect. */
	public static void grect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch.draw(region, x - w / 2, y, w, h);
	}

	public static void rect(String name, float x, float y, float rotation){
		TextureRegion region = region(name);
		batch.draw(region, x - region.getRegionWidth() / 2, y - region.getRegionHeight() / 2, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}
	
	public static void rect(String name, float x, float y, float w, float h, float rotation){
		TextureRegion region = region(name);
		batch.draw(region, x - w / 2, y - h / 2, w / 2, h / 2, w, h, 1, 1, rotation);
	}

	public static void rect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch.draw(region, x - w / 2, y - h / 2, w, h);
	}
	
	public static void crect(Texture texture, float x, float y, float w, float h){
		batch.draw(texture, x, y, w, h);
	}
	
	public static void crect(TextureRegion texture, float x, float y, float w, float h){
		batch.draw(texture, x, y, w, h);
	}

	public static void crect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch.draw(region, x, y, w, h);
	}

	public static void crect(String name, float x, float y){
		TextureRegion region = region(name);
		batch.draw(region, x, y);
	}

	public static void tscl(float scl){
		Core.font.getData().setScale(scl);
	}

	public static void text(String text, float x, float y){
		text(text, x, y, Align.center);
	}

	public static void text(String text, float x, float y, int align){
		Core.font.draw(batch, text, x, y, 0, align, false);
	}

	public static void tcolor(Color color){
		Core.font.setColor(color);
	}

	public static void tcolor(float r, float g, float b, float a){
		Core.font.setColor(r, g, b, a);
	}

	public static void tcolor(float r, float g, float b){
		Core.font.setColor(r, g, b, 1f);
	}

	public static void tcolor(float alpha){
		Core.font.setColor(1f, 1f, 1f, alpha);
	}

	public static void tcolor(){
		Core.font.setColor(Color.WHITE);
	}

	/** Resets thickness, color and text color */
	public static void reset(){
		
		Lines.stroke(1f);
		color();
		if(Core.font != null)
			tcolor();
	}

	public static TextureRegion region(String name){
		return Core.atlas.getRegion(name);
	}

	public static boolean hasRegion(String name){
		return Core.atlas.hasRegion(name);
	}

}
