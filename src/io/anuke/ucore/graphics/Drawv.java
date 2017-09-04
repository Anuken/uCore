package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.EarClippingTriangulator;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.util.Angles;

//TODO
public class Drawv{
	private static final PolygonSpriteBatch batch = new PolygonSpriteBatch();
	//private static final PolygonRegion region = new PolygonRegion();
	private static final Texture blank = PixmapUtils.blankTexture();
	private static final EarClippingTriangulator tri = new EarClippingTriangulator();
	
	private static float[] circleVert;
	private static float[] circleVertTmp;
	private static short[] circleInd;
	
	static{
		int circleEdges = 40;
		
		circleVert = new float[circleEdges*2];
		circleVertTmp = new float[circleEdges*5];
		
		for(int i = 0; i < circleEdges; i ++){
			Angles.translation(i*360f/circleEdges, 1f);
			circleVert[i*2] = Angles.vector.x;
			circleVert[i*2+1] = Angles.vector.y;
		}
		
		circleInd = tri.computeTriangles(circleVert).toArray();
	}
	
	public static void begin(){
		batch.begin();
	}
	
	public static void end(){
		batch.end();
	}
	
	public static void color(Color color){
		batch.setColor(color);
	}
	
	public static void color(){
		batch.setColor(Color.WHITE);
	}
	
	public static void circle(float x, float y, float radius){
		check();
		
		for(int i = 0; i < circleVert.length/2; i ++){
			circleVertTmp[i*5] = circleVert[i*2]*radius + x;
			circleVertTmp[i*5+1] = circleVert[i*2+1]*radius + y;
			circleVertTmp[i*5+2] = batch.getPackedColor();
		}
		
		batch.draw(blank, circleVertTmp, 0, circleVertTmp.length, circleInd, 0, circleInd.length);
	}
	
	private static void check(){
		if(batch.getProjectionMatrix() != Core.batch.getProjectionMatrix()){
			boolean drawing = batch.isDrawing();
			if(drawing)
				batch.end();
			batch.setProjectionMatrix(Core.batch.getProjectionMatrix());
			if(drawing)
				batch.begin();
		}
	}
	
}
