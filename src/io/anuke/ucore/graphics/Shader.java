package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader{
	ShaderProgram program;
	ShaderController control;
	
	public Shader(ShaderProgram program, ShaderController control){
		this.program = program;
		this.control = control;
		
		if(!program.isCompiled()){
			throw new RuntimeException(program.getLog());
		}
	}
	
	public void setParams(Object...objects){
		program.begin();
		control.setParams(program, objects);
		program.end();
	}
	
	public ShaderProgram program(){
		return program;
	}
	
	public interface ShaderController{
		void setParams(ShaderProgram shader, Object... params);
	}
}
