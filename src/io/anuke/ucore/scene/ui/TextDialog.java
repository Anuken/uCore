package io.anuke.ucore.scene.ui;

import io.anuke.ucore.scene.ui.layout.Cell;

public class TextDialog extends Dialog{
	private Cell<Label> cell;
	
	public TextDialog(String title, String... text) {
		super(title);
		
		addCloseButton();
		
		set(title, text);
	}
	
	public void set(String title, String... text){
		content().clearChildren();
		getTitleLabel().setText(title);
		
		StringBuilder out = new StringBuilder();
		
		for(String s : text){
			out.append(s + "\n");
		}
		
		cell = content().add(out.toString());
	}
	
	public TextDialog padText(float amount){
		cell.pad(amount);
		return this;
	}
}
