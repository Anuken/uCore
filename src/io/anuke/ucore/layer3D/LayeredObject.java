package io.anuke.ucore.layer3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

public class LayeredObject implements Poolable{
	/**The textureregion layers for this object.
	 * Modifying these after the object is added will have no effect.*/
	public TextureRegion[] regions;
	/**The position and rotation of the object.*/
	public float x, y, z, rotation;
	/**The color of the object.*/
	public Color color = new Color(1,1,1,1);
	/**Rendering layer offset.*/
	public int offset = 0;
	
	/**Creatures a layered object from all the regions.*/
	public LayeredObject(TextureRegion... regions){
		this.regions = regions;
	}
	
	/**Creatures a layered object from all the textures*/
	public LayeredObject(Texture... textures){
		regions = new TextureRegion[textures.length];
		for(int i = 0; i < textures.length; i ++) regions[i] = new TextureRegion(textures[i]);
	}
	
	/**Sets the position for the object. 
	 * @return the object, for chaining.*/
	public LayeredObject setPosition(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**Sets the position for the object. 
	 * @return this object, for chaining.*/
	public LayeredObject setPosition(float x, float y){
		return setPosition(x,y,z);
	}
	
	public LayeredObject setColor(float r, float g, float b, float a){
		color.set(r, g, b, a);
		return this;
	}
	
	public LayeredObject setColor(float r, float g, float b){
		color.set(r, g, b, color.a);
		return this;
	}

	@Override
	public void reset(){
		x = y = z = rotation = 0;
		regions = null;
	}
}
