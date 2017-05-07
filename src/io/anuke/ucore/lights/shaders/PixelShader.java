package io.anuke.ucore.lights.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PixelShader{
	
	static final public ShaderProgram createPixelShader() {
		return new ShaderProgram(Gdx.files.classpath("io/anuke/ucore/lights/shaders/pixlight.vertex"),
				Gdx.files.classpath("io/anuke/ucore/lights/shaders/pixlight.fragment"));
	}
}
