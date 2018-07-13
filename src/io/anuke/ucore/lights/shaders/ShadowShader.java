package io.anuke.ucore.lights.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ShadowShader{
    static final public ShaderProgram createShadowShader(){
        final String vertexShader = "attribute vec4 a_position;\n" //
                + "attribute vec2 a_texCoord;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_texCoords = a_texCoord;\n" //
                + "   gl_Position = a_position;\n" //
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

                        "varying MED vec2 v_texCoords;",

                        "uniform sampler2D u_texture;",
                        "uniform vec4 ambient;",

                        "float round(float f){",
                        "	return float(int(f/rval))*rval;",
                        "}",

                        "void main(){",
                        "	vec4 c = texture2D(u_texture, v_texCoords);",
                        "	gl_FragColor.rgb = c.rgb * c.a + ambient.rgb;",
                        "	gl_FragColor.a = ambient.a - c.a;",
                        "}");


        ShaderProgram.pedantic = false;
        ShaderProgram shadowShader = new ShaderProgram(vertexShader,
                fragmentShader);
        if(!shadowShader.isCompiled()){
            shadowShader = new ShaderProgram("#version 330 core\n" + vertexShader,
                    "#version 330 core\n" + fragmentShader);
            if(!shadowShader.isCompiled()){
                Gdx.app.log("ERROR", shadowShader.getLog());
            }
        }

        return shadowShader;
    }
}
