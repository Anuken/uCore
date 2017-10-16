package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Shader{
	public final ShaderProgram shader;
	
	public TextureRegion region;
	
	public Shader(String frag, String vert){
		this.shader = new ShaderProgram(Gdx.files.internal("shaders/"+vert+".vertex"),
				Gdx.files.internal("shaders/"+frag+".fragment"));
		
		if(!shader.isCompiled()){
			throw new RuntimeException("Error compiling shaders \"" + frag + "\" and \"" + vert + "\": " + shader.getLog());
		}
		
		if(shader.getLog().length() > 0)
			Gdx.app.error("Shaders", "Shader Log (" + frag + "/" + vert + "): " + shader.getLog());
	}
	
	public Shader(String frag){
		this(frag, "default");
	}
	
	public abstract void apply();
	
	public ShaderProgram program(){
		return shader;
	}
}
