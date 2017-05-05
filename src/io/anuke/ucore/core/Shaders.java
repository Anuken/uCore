
package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.Shader;
import io.anuke.ucore.graphics.Shader.ShaderController;

public class Shaders{
	private static ObjectMap<String, Shader> shaders = new ObjectMap<>();
	
	public static void load(String name, String frag, String vert, ShaderController control){
		Shader shade = new Shader(
				new ShaderProgram(Gdx.files.internal("shaders/"+vert+".vertex"),
						Gdx.files.internal("shaders/"+frag+".fragment")), control);
		
		shaders.put(name, shade);
	}
	
	public static void load(String name, String frag, ShaderController control){
		load(name, frag, "default", control);
	}
	
	public static Shader get(String name){
		return shaders.get(name);
	}
}
