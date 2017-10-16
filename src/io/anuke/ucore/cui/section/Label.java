package io.anuke.ucore.cui.section;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.cui.Section;

public class Label extends Section{
	public String text = "";
	public boolean wrap = true;
	
	public Label(String text){
		this.text = text;
	}
	
	public Label(){
		this("");
	}

	@Override
	public void draw(){
		super.draw();
		
		GlyphLayout layout = Pools.obtain(GlyphLayout.class);
		
		layout.setText(Core.font, text, style.color, width, Align.center, true);
		//TODO
		Core.font.setColor(style.color);
		Core.font.draw(Core.batch, text, x, y + height/2 + layout.height/2f, width, Align.center, true);
		
		Pools.free(layout);
	}
}
