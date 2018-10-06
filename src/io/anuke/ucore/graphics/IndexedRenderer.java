package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

public class IndexedRenderer implements Disposable{
    private final static int vsize = 5;

    private ShaderProgram program = SpriteBatch.createDefaultShader();
    private Mesh mesh;
    private float[] tmpVerts = new float[vsize * 6];
    private float[] vertices;

    private Matrix4 projMatrix = new Matrix4();
    private Matrix4 transMatrix = new Matrix4();
    private Matrix4 combined = new Matrix4();
    private float color = Color.WHITE.toFloatBits();

    public IndexedRenderer(int sprites){
        resize(sprites);
    }

    static public ShaderProgram createDefaultShader(){
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = texture2D(u_texture, v_texCoords);\n" //
                + "}";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if(!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    public void render(Texture texture){
        Gdx.gl.glEnable(GL20.GL_BLEND);

        updateMatrix();

        program.begin();

        texture.bind();

        program.setUniformMatrix("u_projTrans", combined);
        program.setUniformi("u_texture", 0);

        mesh.render(program, GL20.GL_TRIANGLES, 0, vertices.length / 5);

        program.end();
    }

    public void setColor(Color color){
        this.color = color.toFloatBits();
    }

    public void draw(int index, TextureRegion region, float x, float y, float w, float h){
        final float fx2 = x + w;
        final float fy2 = y + h;
        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        float[] vertices = tmpVerts;

        int idx = 0;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        //tri2
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        mesh.updateVertices(index * vsize * 6, vertices);
    }

    public void draw(int index, TextureRegion region, float x, float y, float w, float h, float rotation){
        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        final float originX = w / 2, originY = h / 2;

        final float cos = MathUtils.cosDeg(rotation);
        final float sin = MathUtils.sinDeg(rotation);

        float fx = -originX;
        float fy = -originY;
        float fx2 = w - originX;
        float fy2 = h - originY;

        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;

        float x1 = cos * fx - sin * fy;
        float y1 = sin * fx + cos * fy;

        float x2 = cos * fx - sin * fy2;
        float y2 = sin * fx + cos * fy2;

        float x3 = cos * fx2 - sin * fy2;
        float y3 = sin * fx2 + cos * fy2;

        float x4 = x1 + (x3 - x2);
        float y4 = y3 - (y2 - y1);

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        float[] vertices = tmpVerts;

        int idx = 0;
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        //tri2
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        mesh.updateVertices(index * vsize * 6, vertices);
    }

    public Matrix4 getTransformMatrix(){
        return transMatrix;
    }

    public void setTransformMatrix(Matrix4 matrix){
        transMatrix = matrix;
    }

    public Matrix4 getProjectionMatrix(){
        return projMatrix;
    }

    public void setProjectionMatrix(Matrix4 matrix){
        projMatrix = matrix;
    }

    public void resize(int sprites){
        if(mesh != null) mesh.dispose();

        mesh = new Mesh(true, 6 * sprites, 0,
                new VertexAttribute(Usage.Position, 2, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
        vertices = new float[6 * sprites * vsize];
        mesh.setVertices(vertices);
    }

    private void updateMatrix(){
        combined.set(projMatrix).mul(transMatrix);
    }

    @Override
    public void dispose(){
        mesh.dispose();
        program.dispose();
    }
}
