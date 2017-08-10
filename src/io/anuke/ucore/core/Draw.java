package io.anuke.ucore.core;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.NumberUtils;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.*;
import io.anuke.ucore.scene.style.Drawable;

//TODO maybe move some surface stuff to Graphics class, and keep actual drawing stuff here?*/
public class Draw{
	private static TextureRegion blank = PixmapUtils.blankTextureRegion();
	private static TextureRegion region = blank;
	private static float thickness = 1f;
	private static Vector2 vector = new Vector2();
	private static Vector2[] circle = new Vector2[30];
	private static Sprite sprite;
	private static Color tmpcolor = new Color();

	private static Stack<Batch> batches = new Stack<Batch>();

	private static ObjectMap<String, Drawer> draws = new ObjectMap<>();
	
	private static ObjectMap<String, Surface> surfaces = new ObjectMap<>();
	private static Stack<Surface> surfaceStack = new Stack<>();

	static{
		float step = 360f / circle.length;
		vector.set(1f, 0);
		for(int i = 0; i < circle.length; i++){
			vector.setAngle(i * step);
			circle[i] = vector.cpy();
		}
	}

	public static void useBatch(Batch batch){
		if(batches.isEmpty())
			batches.push(DrawContext.batch);
		batches.push(batch);
		DrawContext.batch = batch;
	}

	public static void popBatch(){
		batches.pop();
		DrawContext.batch = batches.peek();
	}
	
	public static Surface currentSurface(){
		return surfaceStack.isEmpty() ? null : surfaceStack.peek();
	}

	/** Adds a custom surface that handles events. */
	public static void addSurface(CustomSurface surface){
		surfaces.put(surface.name(), surface);
	}

	/** Creates a surface, sized to the screen */
	public static void addSurface(String name){
		addSurface(name, 1, 0);
	}

	public static void addSurface(String name, int scale){
		addSurface(name, scale, 0);
	}

	/**
	 * Creates a surface, scale times smaller than the screen. Useful for
	 * pixelated things.
	 */
	public static void addSurface(String name, int scale, int bind){
		surfaces.put(name, new Surface(name, scale, bind));
	}

	public static Surface getSurface(String name){
		if(!surfaces.containsKey(name))
			throw new IllegalArgumentException("The surface \"" + name + "\" does not exist!");

		return surfaces.get(name);
	}

	/** Begins drawing on a surface. */
	public static void surface(String name){
		if(!surfaceStack.isEmpty()){
			end();
			surfaceStack.peek().end(false);
		}

		Surface surface = getSurface(name);

		surfaceStack.push(surface);

		if(drawing())
			end();
		surface.begin();
		begin();
	}

	public static void surface(){
		surface(false);
	}

	/** Ends drawing on the current surface. */
	public static void surface(boolean end){
		checkSurface();

		Surface surface = surfaceStack.pop();

		end();
		surface.end(true);

		if(!end){
			Surface current = surfaceStack.empty() ? null : surfaceStack.peek();

			if(current != null)
				current.begin(false);
			begin();
		}
	}

	/** Ends the current surface and draws its contents onto the screen. */
	public static void flushSurface(){
		checkSurface();

		Surface surface = surfaceStack.pop();

		end();
		surface.end(true);

		Surface current = surfaceStack.empty() ? null : surfaceStack.peek();

		if(current != null)
			current.begin(false);

		setScreen();
		batch().draw(surface.texture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		end();

		beginCam();
	}

	/** Sets the batch projection matrix to the screen, without the camera. */
	public static void setScreen(){
		boolean drawing = batch().isDrawing();

		if(drawing)
			end();
		batch().getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		begin();
	}

	/** Begins the batch and sets the camera projection matrix. */
	public static void beginCam(){
		batch().setProjectionMatrix(DrawContext.camera.combined);
		batch().begin();
	}

	/** Begins the batch. */
	public static void begin(){
		batch().begin();
	}

	/** Ends the batch */
	public static void end(){
		batch().end();
	}

	public static boolean drawing(){
		return batch().isDrawing();
	}

	private static void checkSurface(){
		if(surfaceStack.isEmpty())
			throw new RuntimeException("Surface stack is empty! Set a surface first.");
	}

	/** Set the shader by name, with specified params. */
	public static void shader(String name, Object... params){
		boolean rendering = batch().isDrawing();

		if(rendering)
			batch().end();

		Shader shader = Shaders.get(name);
		shader.setParams(params);

		batch().setShader(shader.program());

		if(rendering)
			batch().begin();
	}

	/** Revert to the default shader. */
	public static void shader(){
		boolean rendering = batch().isDrawing();

		if(rendering)
			batch().end();

		batch().setShader(null);

		if(rendering)
			batch().begin();
	}

	public static void sprite(Sprite sprite){
		sprite.draw(batch());
	}

	public static void drawable(String name, Drawer run){
		draws.put(name, run);
	}

	public static void draw(String name, float x, float y){
		draws.get(name).draw(x, y);
	}

	public static void patch(String name, float x, float y, float width, float height){
		getPatch(name).draw(batch(), x, y, width, height);
	}

	public static Drawable getPatch(String name){
		return DrawContext.skin.getDrawable(name);
	}

	/** Sets the batch color to this color AND the previous alpha. */
	public static void tint(Color color){
		color(color.r, color.g, color.b, batch().getColor().a);
	}

	public static void color(Color color){
		batch().setColor(color);
	}

	/** Automatically mixes colors. */
	public static void color(Color a, Color b, float s){
		batch().setColor(Hue.mix(a, b, s, tmpcolor));
	}

	public static void color(String name){
		batch().setColor(Colors.get(name));
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

	/** Lightness color. */
	public static void colorl(float l){
		color(l, l, l);
	}

	/** Lightness color, alpha. */
	public static void colorl(float l, float a){
		color(l, l, l, a);
	}

	public static void alpha(float alpha){
		Color color = batch().getColor();
		batch().setColor(color.r, color.g, color.b, alpha);
	}

	public static void gradient(Color left, Color right, float alpha, float x, float y, float w, float h){
		if(sprite == null)
			sprite = new Sprite(blank);

		sprite.setBounds(x, y, w, h);

		tmpcolor.set(left);
		tmpcolor.a *= alpha;
		float cl = tmpcolor.toFloatBits();

		tmpcolor.set(right);
		tmpcolor.a *= alpha;
		float cr = tmpcolor.toFloatBits();

		float[] v = sprite.getVertices();
		v[SpriteBatch.C1] = cl;
		v[SpriteBatch.C2] = cl;

		v[SpriteBatch.C3] = cr;
		v[SpriteBatch.C4] = cr;

		sprite.draw(batch());
	}

	public static void rect(String name, float x, float y){
		TextureRegion region = region(name);
		batch().draw(region, x - region.getRegionWidth() / 2, y - region.getRegionHeight() / 2);
	}

	/** Floating side rect. */
	public static void sirect(String name, float x, float y, float rotation){
		TextureRegion region = region(name);
		batch().draw(region, x, y - region.getRegionHeight() / 2f, 0, region.getRegionHeight() / 2f, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);

	}

	/** Floating side rect. */
	public static void borect(String name, float x, float y, float rotation){
		TextureRegion region = region(name);
		batch().draw(region, x - region.getRegionWidth() / 2f, y, region.getRegionWidth() / 2f, 0, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);

	}

	/** Grounded rect. */
	public static void grect(String name, float x, float y){
		TextureRegion region = region(name);
		batch().draw(region, x - region.getRegionWidth() / 2, y);
	}

	/** Grounded rect. */
	public static void grect(String name, float x, float y, boolean flipx){
		TextureRegion region = region(name);
		if(flipx){
			batch().draw(region, x + region.getRegionWidth() / 2, y, -region.getRegionWidth(), region.getRegionHeight());
		}else{
			batch().draw(region, x - region.getRegionWidth() / 2, y);
		}
	}

	/** Grounded rect. */
	public static void grect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch().draw(region, x - w / 2, y, w, h);
	}

	public static void rect(String name, float x, float y, float rotation){
		TextureRegion region = region(name);
		batch().draw(region, x - region.getRegionWidth() / 2, y - region.getRegionHeight() / 2, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, rotation);
	}

	public static void rect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch().draw(region, x - w / 2, y - h / 2, w, h);
	}

	public static void crect(String name, float x, float y, float w, float h){
		TextureRegion region = region(name);
		batch().draw(region, x, y, w, h);
	}

	public static void crect(String name, float x, float y){
		TextureRegion region = region(name);
		batch().draw(region, x, y);
	}

	public static void laser(String line, String edge, float x, float y, float x2, float y2){
		laser(line, edge, x, y, x2, y2, vector.set(x2 - x, y2 - y).angle());
	}

	public static void laser(String line, String edge, float x, float y, float x2, float y2, float rotation){

		//Draw.colorl(0.75f + MathUtils.random(0.2f) + Math.abs(MathUtils.sin(Timers.time()/3f)/4f));

		thickness = 12f;
		Draw.line(region(line), x, y, x2, y2);
		thickness = 1f;

		Draw.rect(edge, x, y, rotation + 180);

		Draw.rect(edge, x2, y2, rotation);

		//Draw.color();
	}

	public static void lineAngle(float x, float y, float angle, float length){
		vector.setLength(length).setAngle(angle);

		line(x, y, x + vector.x, y + vector.y);
	}

	public static void lineAngle(float x, float y, float offset, float angle, float length){
		vector.setLength(length + offset).setAngle(angle);

		line(x, y, x + vector.x, y + vector.y);
	}

	public static void lineAngleCenter(float x, float y, float angle, float length){
		vector.setLength(length).setAngle(angle);

		line(x - vector.x / 2, y - vector.y / 2, x + vector.x / 2, y + vector.y / 2);
	}

	public static void line(float x, float y, float x2, float y2){
		float length = Vector2.dst(x, y, x2, y2) + thickness / 2;
		float angle = ((float) Math.atan2(y2 - y, x2 - x) * MathUtils.radDeg);

		batch().draw(region, x - thickness / 2, y - thickness / 2, thickness / 2, thickness / 2, length, thickness, 1f, 1f, angle);
	}

	public static void line(TextureRegion texture, float x, float y, float x2, float y2){
		float length = Vector2.dst(x, y, x2, y2) + thickness / 2;
		float angle = ((float) Math.atan2(y2 - y, x2 - x) * MathUtils.radDeg);

		batch().draw(texture, x - thickness / 2, y - thickness / 2, thickness / 2, thickness / 2, length, thickness, 1f, 1f, angle);
	}

	public static void circle(float x, float y, float rad){
		polygon(circle, x, y, rad);
	}

	public static void dashcircle(float x, float y, float scl){
		dashpolygon(circle, x, y, scl);
	}

	public static void spikes(float x, float y, float radius, float length, int spikes, float rot){
		vector.set(0, 1);
		float step = 360f / spikes;

		for(int i = 0; i < spikes; i++){
			vector.setAngle(i * step + rot);
			vector.setLength(radius);
			float x1 = vector.x, y1 = vector.y;
			vector.setLength(radius + length);

			line(x + x1, y + y1, x + vector.x, y + vector.y);
		}
	}

	public static void spikes(float x, float y, float rad, float length, int spikes){
		spikes(x, y, rad, length, spikes, 0);
	}

	public static void polygon(int sides, float x, float y, float radius, float angle){
		vector.set(0, 0);

		for(int i = 0; i < sides; i++){
			vector.set(radius, 0).setAngle(360f / sides * i + angle + 90);
			float x1 = vector.x;
			float y1 = vector.y;

			vector.set(radius, 0).setAngle(360f / sides * (i + 1) + angle + 90);

			Draw.line(x1 + x, y1 + y, vector.x + x, vector.y + y);
		}
	}

	public static void polysegment(int sides, int from, int to, float x, float y, float radius, float angle){
		vector.set(0, 0);

		for(int i = from; i < to; i++){
			vector.set(radius, 0).setAngle(360f / sides * i + angle + 90);
			float x1 = vector.x;
			float y1 = vector.y;

			vector.set(radius, 0).setAngle(360f / sides * (i + 1) + angle + 90);

			Draw.line(x1 + x, y1 + y, vector.x + x, vector.y + y);
		}
	}

	public static void curve(float x1, float y1, float cx1, float cy1, float cx2, float cy2, float x2, float y2, int segments){

		// Algorithm shamelessly stolen from shapenrenderer class
		float subdiv_step = 1f / segments;
		float subdiv_step2 = subdiv_step * subdiv_step;
		float subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;

		float pre1 = 3 * subdiv_step;
		float pre2 = 3 * subdiv_step2;
		float pre4 = 6 * subdiv_step2;
		float pre5 = 6 * subdiv_step3;

		float tmp1x = x1 - cx1 * 2 + cx2;
		float tmp1y = y1 - cy1 * 2 + cy2;

		float tmp2x = (cx1 - cx2) * 3 - x1 + x2;
		float tmp2y = (cy1 - cy2) * 3 - y1 + y2;

		float fx = x1;
		float fy = y1;

		float dfx = (cx1 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
		float dfy = (cy1 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;

		float ddfx = tmp1x * pre4 + tmp2x * pre5;
		float ddfy = tmp1y * pre4 + tmp2y * pre5;

		//needs more d
		float dddfx = tmp2x * pre5;
		float dddfy = tmp2y * pre5;

		while(segments-- > 0){
			float fxold = fx, fyold = fy;
			fx += dfx;
			fy += dfy;
			dfx += ddfx;
			dfy += ddfy;
			ddfx += dddfx;
			ddfy += dddfy;
			line(fxold, fyold, fx, fy);
		}

		line(fx, fy, x2, y2);
	}

	public static void swirl(float x, float y, float radius, float fraction){
		int sides = 50;
		int max = (int) (sides * (fraction + 0.001f));
		vector.set(0, 0);

		for(int i = 0; i < max; i++){
			vector.set(radius, 0).setAngle(360f / sides * i);
			float x1 = vector.x;
			float y1 = vector.y;

			vector.set(radius, 0).setAngle(360f / sides * (i + 1));

			Draw.line(x1 + x, y1 + y, vector.x + x, vector.y + y);
		}
	}

	public static void polygon(int sides, float x, float y, float radius){
		polygon(sides, x, y, radius, 0);
	}

	public static void polygon(Vector2[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0; i < vertices.length; i++){
			Vector2 current = vertices[i];
			Vector2 next = i == vertices.length - 1 ? vertices[0] : vertices[i + 1];
			line(current.x * scl + offsetx, current.y * scl + offsety, next.x * scl + offsetx, next.y * scl + offsety);
		}
	}

	public static void dashpolygon(Vector2[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0; i < vertices.length; i++){
			if(i % 2 != 0)
				continue;
			Vector2 current = vertices[i];
			Vector2 next = i == vertices.length - 1 ? vertices[0] : vertices[i + 1];
			line(current.x * scl + offsetx, current.y * scl + offsety, next.x * scl + offsetx, next.y * scl + offsety);
		}
	}

	public static void polygon(float[] vertices, float offsetx, float offsety, float scl){
		for(int i = 0; i < vertices.length / 2; i++){
			float x = vertices[i * 2];
			float y = vertices[i * 2 + 1];

			float x2 = 0, y2 = 0;
			if(i == vertices.length / 2 - 1){
				x2 = vertices[0];
				y2 = vertices[1];
			}else{
				x2 = vertices[i * 2 + 2];
				y2 = vertices[i * 2 + 3];
			}

			line(x * scl + offsetx, y * scl + offsety, x2 * scl + offsetx, y2 * scl + offsety);
		}
	}

	public static void square(float x, float y, float rad){
		linerect(x - rad, y - rad, rad * 2, rad * 2);
	}

	public static void fillrect(float x, float y, float width, float height){
		batch().draw(region, x - width / 2f, y - height / 2f, width, height);
	}

	public static void fillcrect(float x, float y, float width, float height){
		batch().draw(region, x, y, width, height);
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

	public static void linerect(Rectangle rect){
		linerect(rect.x, rect.y, rect.width, rect.height, 0);
	}

	public static void linerect(float x, float y, float width, float height, int space){
		linerect(x, y, width, height, space, space);
	}

	public static void thickness(float thick){
		thickness = thick;
	}

	public static void thick(float thick){
		thickness = thick;
	}
	
	public static float getThickness(){
		return thickness;
	}
	
	public static void tscl(float scl){
		DrawContext.font.getData().setScale(scl);
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

	public static void tcolor(float alpha){
		DrawContext.font.setColor(1f, 1f, 1f, alpha);
	}

	public static void tcolor(){
		DrawContext.font.setColor(Color.WHITE);
	}

	/** Resets thickness, color and text color */
	public static void reset(){
		thickness(1f);
		color();
		if(DrawContext.font != null)
			tcolor();
	}

	public static TextureRegion region(String name){
		return DrawContext.atlas.getRegion(name);
	}

	public static boolean hasRegion(String name){
		return DrawContext.atlas.hasRegion(name);
	}

	public static Batch batch(){
		return DrawContext.batch;
	}

	public static void resize(){
		for(Surface surface : surfaces.values()){
			surface.resize();
		}
	}

	/**
	 * Disposes drawContext resources, as well as internal resources and skin.
	 */
	public static void dispose(){
		blank.getTexture().dispose();
		batch().dispose();
		
		for(Surface surface : surfaces.values()){
			surface.dispose();
		}
		
		surfaces.clear();
		
		Caches.dispose();
		
		if(Textures.isLoaded()){
			Textures.dispose();
		}
		
		if(DrawContext.scene != null){
			DrawContext.scene.dispose();
		}

		if(DrawContext.atlas != null){
			DrawContext.atlas.dispose();
		}

		if(DrawContext.skin != null){
			DrawContext.skin.dispose();
		}
	}

	public interface Drawer{
		public void draw(float x, float y);
	}

}
