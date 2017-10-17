package io.anuke.ucore.cui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.cui.style.Style;

public abstract class Section{
	private Array<String> styles = new Array<String>();

	public Style style = new Style();
	protected Array<Section> children = new Array<>();
	protected Section parent;
	
	/**Do not modify unless laying out.*/
	public float x, y, width, height;
	protected float prefWidth = 50f, prefHeight = 50f;
	protected boolean setWidth, setHeight;

	public Section() {
		updateStyle();
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

		if(style.border != null){
			Draw.color(style.border.color);
			Draw.thick(style.border.thickness);
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
		Canvas.instance().getStylesheet().getStyle(this, style, styles);
	}

	public void addStyle(String newstyle){
		styles.add(newstyle);
		updateStyle();
	}
}
