package io.anuke.ucore.cui;

import java.util.EnumSet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.cui.layout.Mode;
import io.anuke.ucore.cui.style.Style;

public abstract class Section{
	private Array<String> styles = new Array<String>();
	private EnumSet<Mode> modes = EnumSet.noneOf(Mode.class);
	
	protected Style style = new Style();
	protected Array<Section> children = new Array<>();
	
	public float x, y, width = 100, height = 100;
	
	public Section(){
		Canvas.instance().getStylesheet().getStyle(style, getTypeName(), styles);
	}
	
	public void modes(Mode...modes){
		for(Mode mode : modes){
			this.modes.add(mode);
		}
	}
	
	public boolean isMode(Mode mode){
		return modes.contains(mode);
	}
	
	public void setMode(Mode mode, boolean set){
		if(set){
			modes.add(mode);
		}else{
			modes.remove(mode);
		}
	}
	
	public void setBounds(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(){
		Draw.color(style.color);
		
		if(style.background != null){
			style.background.draw(x, y, width, height);
		}
		
		if(style.border != null){
			Draw.color(style.border.color);
			Draw.thick(style.border.thickness);
			Draw.linerect(x, y, width, height);
			Draw.reset();
		}
		
		for(Section child : children){
			child.draw();
		}
	}
	
	public void update(){
		for(Section child : children){
			child.update();
		}
	}
	
	public void add(Section section){
		children.add(section);
	}
	
	public void remove(Section section){
		children.removeValue(section, true);
	}
	
	public String getTypeName(){
		return ClassReflection.getSimpleName(getClass()).toLowerCase();
	}
	
	public void addStyle(String newstyle){
		styles.add(newstyle);
		Canvas.instance().getStylesheet().getStyle(style, getTypeName(), styles);
	}
}
