package io.anuke.ucore.scene.ui;

import io.anuke.ucore.scene.ui.layout.Cell;

public class TextDialog extends Dialog{
	private Cell<Label> cell;
	
	public TextDialog(String title, String... text) {
		super(title);
		
		addCloseButton();
		
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
