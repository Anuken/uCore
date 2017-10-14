package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.EarClippingTriangulator;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.util.Angles;

//TODO make this work properly
public class Drawv{
	private static final PolygonSpriteBatch batch = new PolygonSpriteBatch();
	//private static final PolygonRegion region = new PolygonRegion();
	private static final Texture blank = Pixmaps.blankTexture();
	private static final EarClippingTriangulator tri = new EarClippingTriangulator();
	
	private static float[] circleVert;
	private static float[] circleVertTmp;
	private static short[] circleInd;
	
	private static float[] triVert;
	private static short[] triInd;
	
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
		
		triVert = new float[3 * 5];
		triInd = new short[]{0, 1, 2};
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
	
	public static void tri(float x1, float y1, float x2, float y2, float x3, float y3){
		
		tri(x1, y1, x2, y2, x3, y3, batch.getPackedColor(), batch.getPackedColor(), batch.getPackedColor());
	}
			
	
	public static void tri(float x1, float y1, float x2, float y2, float x3, float y3
			, float c1, float c2, float c3){
		check();
		triVert[0 + 0*1] = x1; 
		triVert[1 + 0*1] = y1; 
		triVert[2 + 0*1] = c1; 
		
		triVert[0 + 5*1] = x2; 
		triVert[1 + 5*1] = y2; 
		triVert[2 + 5*1] = c2; 

		triVert[0 + 5*2] = x3; 
		triVert[1 + 5*2] = y3; 
		triVert[2 + 5*2] = c3; 
		
		batch.draw(blank, triVert, 0, triVert.length, triInd, 0, triInd.length);
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
