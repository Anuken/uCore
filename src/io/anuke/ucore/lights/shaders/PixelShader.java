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
		"const float scl = 0.04;",
		"const int pr = 3;",

		"float round(float f){",
		"	return float(int(f/r+0.7))*r;",
		"}",

		"void main(){",
		"	vec4 c = texture2D(u_texture, v_texCoords);",
		"	float dst = c.r;",
		"	dst = pow(dst, pr);",
		"	c.r = round(1.0-clamp(1.0/dst*scl, 0.0, 1.0))*tint.r;",
		"	c.g = round(1.0-clamp(1.0/pow(c.g, pr)*scl, 0.0, 1.0))*tint.g;",
		"	c.b = round(1.0-clamp(1.0/pow(c.b, pr)*scl, 0.0, 1.0))*tint.b;",
		"	gl_FragColor.rgb = (ambient.rgb , c.rgb);",
		"	gl_FragColor.a = 1.0;",
		"}");
		//TODO fix posturization
		
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
