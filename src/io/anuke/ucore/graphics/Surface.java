package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Graphics;

/**A framebuffer wrapper.*/
public class Surface implements Disposable{
	private FrameBuffer buffer;
	private int scale = 1;
	private boolean linear = false;
	private int bind = 0;
	
	public Surface(int scale, int bind){
		this.scale = scale;
		this.bind = bind;
		resize();
	}
	
	public void resize(){
		if(buffer != null){
			buffer.dispose();
			buffer = null;
		}
		
		int scale = this.scale == -1 ? Core.cameraScale : this.scale;
		
		buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth()/scale, Gdx.graphics.getHeight()/scale, false);
		if(!linear)
			buffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public Texture texture(){
		return buffer.getColorBufferTexture();
	}
	
	public void setLinear(boolean linear){
		this.linear = linear;
	}
	
	public void setScale(int scale){
		this.scale = scale;
		resize();
	}
	
	public void begin(){
		begin(true);
	}
	
	public void begin(boolean clear){
		buffer.begin();
		if(bind != 0) buffer.getColorBufferTexture().bind(bind);
		//for(Texture texture : Core.atlas.getTextures()){
		//	texture.bind(0);
		//}
		
		if(clear)
			Graphics.clear(Color.CLEAR);
	}
	
	//TODO bind all textures to 0 as well, maybe?
	public void end(boolean render){
		buffer.end();
		if(bind != 0) buffer.getColorBufferTexture().bind(0);
		//for(Texture texture : Core.atlas.getTextures()){
		//	texture.bind(0);
		//}
	}
	
	@Override
	public void dispose(){
		buffer.dispose();
	}
}
