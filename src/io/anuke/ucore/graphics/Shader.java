package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import io.anuke.ucore.core.Draw;

public abstract class Shader{
	public final ShaderProgram shader;
	
	public TextureRegion region;
	
	public Shader(String frag, String vert){
		this.shader = new ShaderProgram(Gdx.files.internal("shaders/"+vert+".vertex"),
				Gdx.files.internal("shaders/"+frag+".fragment"));
		
		if(!shader.isCompiled()){
			throw new RuntimeException(shader.getLog());
		}
		
		Draw.addShader(this);
	}
	
	public Shader(String frag){
		this(frag, "default");
	}
	
	public abstract void apply();
	
	public ShaderProgram program(){
		return shader;
	}
}
