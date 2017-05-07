package io.anuke.ucore.lights.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PixelShader{
	
	static final public ShaderProgram createPixelShader() {
		
		String frag = String.join("\n",
		"#ifdef GL_ES",
		"precision lowp float;",
		"#define MED mediump",
		"#else",
		"#define MED ",
		"#endif",
		"varying MED vec2 v_texCoords;",
		"uniform sampler2D u_texture;",
		"uniform vec4 ambient;",
		"uniform vec4 tint;",

		"const float r = 0.2;",

		"float round(float f){",
		"	return float(int(f/r))*r;",
		"}",

		"void main(){",
		"	vec4 c = texture2D(u_texture, v_texCoords);",
		"	c.r = round(c.r)*tint.r;",
		"	c.g = round(c.g)*tint.g;",
		"	c.b = round(c.b)*tint.b;",
		"	gl_FragColor.rgb = (ambient.rgb , c.rgb);",
		"	gl_FragColor.a = 1.0;",
		"}");
		
		String vert = String.join("\n",
		"attribute vec4 a_position;",
		"attribute vec2 a_texCoord;",
		"varying vec2 v_texCoords;",

		"void main(){",
		"	v_texCoords = a_texCoord;",
		"	gl_Position = a_position;",
		"}");
		
		ShaderProgram shade = new ShaderProgram(vert, frag);
		if(!shade.isCompiled())
			Gdx.app.error("Shader Compilation", shade.getLog());
		
		return shade;
	}
}
