package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class FilledPolygon{
	private PolygonRegion region;
	private PolygonSprite sprite;
	private EarClippingTriangulator tri;
	private TextureRegion texture;

	public FilledPolygon(TextureRegion texture, Array<Vector2> vertices){
		this.texture = texture;
		tri = new EarClippingTriangulator();
		setVertices(vertices);
	}

	public void setVertices(Array<Vector2> vertices){
		float[] floats = new float[vertices.size*2];
		for(int i = 0; i < vertices.size; i ++){
			floats[i*2] = vertices.get(i).x;
			floats[i*2+1] = vertices.get(i).y;
		}
		region = new PolygonRegion(texture, floats, tri.computeTriangles(floats).toArray());
		if(sprite == null) sprite = new PolygonSprite(region);
		sprite.setRegion(region);
	}

	public void draw(PolygonSpriteBatch batch){
		sprite.draw(batch);
	}

	public PolygonSprite sprite(){
		return sprite;
	}

	public void setPosition(float x, float y){
		sprite.setPosition(x, y);
	}
}
