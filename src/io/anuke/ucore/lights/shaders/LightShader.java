package io.anuke.ucore.lights.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import io.anuke.ucore.lights.RayHandler;

public final class LightShader {
	static final public ShaderProgram createLightShader() {
		String gamma = ""; 
		if (RayHandler.getGammaCorrection())
			gamma = "sqrt";
		
		final String vertexShader = 
				"attribute vec4 vertex_positions;\n" //
				+ "attribute vec4 quad_colors;\n" //
				+ "attribute float s;\n"
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //				
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = s * quad_colors;\n" //				
				+ "   gl_Position =  u_projTrans * vertex_positions;\n" //
				+ "}\n";
		final String fragmentShader = 
				String.join("\n",
				"#ifdef GL_ES",
				"precision lowp float;",
				"#define MED mediump",
				"#else",
				"#define MED ",
				"#endif",
				
				"#define rval 0.1",
				
				"float round(float f){",
				"	return float(int(f/rval))*rval;",
				"}",
				
				"varying vec4 v_color;\n",
				
				"void main(){",
				"	vec4 vc = v_color;",
				//"	vc.r = round(v_color.r);",
				//"	vc.g = round(v_color.g);",
				//"	vc.b = round(v_color.b);",
				//"	vc.a = round(v_color.a);",
				"	gl_FragColor = "+gamma+"(vc);",
				"}");
		
		ShaderProgram.pedantic = false;
		ShaderProgram lightShader = new ShaderProgram(vertexShader,
				fragmentShader);
		if (!lightShader.isCompiled()) {
			lightShader = new ShaderProgram("#version 330 core\n" +vertexShader,
					"#version 330 core\n" +fragmentShader);
			if(!lightShader.isCompiled()){
				Gdx.app.log("ERROR", lightShader.getLog());
			}
		}

		return lightShader;
	}
}
