package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;

/**A batch that does absolutely nothing. Might be useful in some edge cases.*/
public class DummyBatch implements Batch{

	@Override
	public void dispose(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColor(Color tint){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColor(float r, float g, float b, float a){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColor(float color){
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getColor(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getPackedColor(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Texture texture, float x, float y){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Texture texture, float[] spriteVertices, int offset, int count){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(TextureRegion region, float x, float y){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float width, float height){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(TextureRegion region, float width, float height, Affine2 transform){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableBlending(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableBlending(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlendFunction(int srcFunc, int dstFunc){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha) {

	}

	@Override
	public int getBlendSrcFunc(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlendDstFunc(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlendSrcFuncAlpha() {
		return 0;
	}

	@Override
	public int getBlendDstFuncAlpha() {
		return 0;
	}

	@Override
	public Matrix4 getProjectionMatrix(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix4 getTransformMatrix(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProjectionMatrix(Matrix4 projection){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransformMatrix(Matrix4 transform){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShader(ShaderProgram shader){
		// TODO Auto-generated method stub
		
	}

	@Override
	public ShaderProgram getShader(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBlendingEnabled(){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDrawing(){
		// TODO Auto-generated method stub
		return false;
	}

}
