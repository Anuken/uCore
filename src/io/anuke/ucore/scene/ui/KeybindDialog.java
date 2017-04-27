package io.anuke.ucore.scene.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.scene.style.Styles;
import io.anuke.ucore.scene.ui.layout.Table;

public class KeybindDialog extends Dialog{
	private KeybindDialogStyle style;
	
	public KeybindDialog() {
		super("Rebind Keys");
		style = Styles.styles.get(KeybindDialogStyle.class);
		setup();
		addCloseButton();
	}
	
	public void setStyle(KeybindDialogStyle style){
		this.style = style;
		setup();
	}
	
	private void setup(){
		Table table = content();
		table.clear();
		
		for(String s : KeyBinds.getBinds()){
			Label keylabel = new Label(Keys.toString(KeyBinds.get(s)));
			
			keylabel.setColor(style.keyColor);
			
			table.add(capitalize(s), style.keyNameColor).left().padRight(40).padLeft(8);
			table.add(keylabel).left().minWidth(90).padRight(20);
			table.addButton("Rebind", ()->{
				openDialog(s);
			});
			table.addButton("Reset", ()->{
				KeyBinds.resetKey(s);
				setup();
				KeyBinds.saveBindings();
			});
			table.row();
		}
		
		pack();
	}
	
	private void openDialog(String name){
		Dialog d = new Dialog("Press a key...", "dialog");
		
		d.getTitleTable().getCells().first().pad(4);
		d.button("Cancel", -1).pad(4);
		
		d.keyDown(i->{
			d.hide();
			KeyBinds.rebindKey(name, i);
			KeyBinds.saveBindings();
			setup();
		});
		
		d.show(getScene());
	}
	
	private String capitalize(String s){
		return s.substring(0, 1).toUpperCase()+s.substring(1);
	}
	
	static public class KeybindDialogStyle {
		public Color keyColor = Color.WHITE;
		public Color keyNameColor = Color.WHITE;
	}
}
