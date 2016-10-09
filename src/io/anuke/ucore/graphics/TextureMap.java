package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Utility class for loading and storing textures.
 */
public class TextureMap implements Disposable{
	private ObjectMap<String, Texture> map = new ObjectMap<String, Texture>();
	public String path = "", errortexture = "error";

	/**
	 * @param path
	 *            Directory to load textures from. It will load textures at
	 *            runtime as requested.
	 */
	public TextureMap(String path) {
		this.path = path;
	}

	/**
	 * Sets all the textures in the array to TextureWrap.Repeat.
	 */
	public void repeatWrap(String... textures){
		for(String string : textures){
			get(string).setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		}
	}

	/**
	 * @param Name
	 *            of texture you want to find.
	 * @return The texture, or the error texture if it isn't found and
	 *         autoloading is disabled.
	 */
	public Texture get(String name){
		if(!map.containsKey(name)){
			try{
				map.put(name, new Texture(path + name + ".png"));
			}catch(Exception e){
				map.put(name, null);
			}
		}
		return map.get(name);
	}

	public void put(String name, Texture texture){
		map.put(name, texture);
	}

	public boolean has(String name){
		return map.containsKey(name);
	}

	public void dispose(){
		for(Texture texture : map.values())
			texture.dispose();
	}
}
