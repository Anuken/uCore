package io.anuke.ucore.cui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import io.anuke.ucore.core.*;
import io.anuke.ucore.cui.style.Style;
import io.anuke.ucore.util.Tmp;

public abstract class Section{
	private Array<String> styles = new Array<String>();

	public Style style = new Style();
	protected Style computedStyle = new Style();
	protected ObjectMap<Enum<?>, Style> stateStyles;
	protected Array<Section> children = new Array<>();
	protected Section parent;
	
	protected Enum<?> state;
	protected Enum<?> targetState;
	protected float stateTime;
	
	/**Do not modify unless laying out.*/
	public float x, y, width, height;
	protected float prefWidth = 50f, prefHeight = 50f;
	protected boolean setWidth, setHeight;

	public Section() {
		updateStyle();
	}
	
	protected void updateState(){
		
		if(targetState != null && targetState != state){
			stateTime += Timers.delta();
			if(stateTime > stateStyles.get(targetState).transition){
				state = targetState;
				stateTime = 0f;
				targetState = null;
			}
		}
		
		if(state == targetState){
			targetState = null;
		}
		
		Canvas.instance().getStylesheet().getStateStyle(this, computedStyle, style);
	}
	
	protected void updateStateStyle(){
		
	}
	
	public Vector2 getRelativeMousePosition(){
		Core.batch.getTransformMatrix().getTranslation(Tmp.v31);
		return Graphics.mouse().sub(Tmp.v31.x, Tmp.v31.y);
	}
	
	public boolean hasMouse(){
		return Tmp.r1.set(x, y, width, height).contains(getRelativeMousePosition());
	}

	public void setBounds(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setWidth(float width){
		this.width = width;
		setWidth = true;
	}
	
	public void setHeight(float height){
		this.height = height;
		setHeight = true;
	}
	
	public void setSize(float width, float height){
		setWidth(width);
		setHeight(height);
	}
	
	public void draw(){
		Draw.color(style.color);

		if(style.background != null){
			style.background.draw(x, y, width, height);
		}

		if(style.borderThickness > 0.0001f){
			Draw.color(style.borderColor);
			Draw.thick(style.borderThickness);
			Draw.linerect(x, y, width, height);
			Draw.reset();
		}

		if(children.size > 0){
			Core.batch.end();
			Core.batch.getTransformMatrix().translate(x, y, 0);
			Core.batch.begin();

			for(Section child : children){
				child.draw();
			}

			Core.batch.end();
			Core.batch.getTransformMatrix().translate(-x, -y, 0);
			Core.batch.begin();
			
			
		}
	}

	public void update(){
		for(Section child : children){
			child.update();
		}
	}

	public void add(Section section){
		if(section.parent != null){
			throw new RuntimeException("A section cannot have two parents.");
		}
		children.add(section);
		section.parent = this;
		section.updateStyle();
		section.updateSize();
	}

	public void remove(Section section){
		if(section.parent == null){
			throw new RuntimeException("A section must have a parent to remove it.");
		}
		children.removeValue(section, true);
		section.parent = null;
		section.updateStyle();
	}

	public void updateSize(){
		if(!setWidth)
			width = prefWidth + style.padLeft + style.padRight;
		if(!setHeight)
			height = prefHeight + style.padBottom + style.padTop;
	}

	public String getTypeName(){
		return ClassReflection.getSimpleName(getClass()).toLowerCase();
	}

	public void updateStyle(){
		Canvas.instance().getStylesheet().getStyle(this, computedStyle, style, styles);
	}

	public void addStyle(String newstyle){
		styles.add(newstyle);
		updateStyle();
	}
}
