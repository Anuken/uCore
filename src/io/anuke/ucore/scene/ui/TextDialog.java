package io.anuke.ucore.scene.ui;

import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.ui.layout.Value;

public class TextDialog extends Dialog{
	private float textwidth = -1;
	
	private Value value = new Value(){
		public float get(Element e){
			return textwidth <= 0 ? e.getPrefWidth() : textwidth;
		}
	};
	
	public TextDialog(String title, float width, String... text) {
		super(title);
		this.textwidth = width;
		addCloseButton();
		
		set(title, text);
	}
	
	public TextDialog(String title, String... text) {
		this(title, -1, text);
	}
	
	public void set(String title, String... text){
		content().clearChildren();
		getTitleLabel().setText(title);
		
		for(String s : text){
			Label label = new Label(s);
			label.setWrap(true);
			content().add(label).maxWidth(value);
			content().row();
		}
	}
	
	public void setTextWidth(float w){
		this.textwidth = w;
	}
	
	public TextDialog padText(float amount){
		content().pad(amount);
		return this;
	}
}
