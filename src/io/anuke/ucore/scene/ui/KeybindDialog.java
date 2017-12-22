package io.anuke.ucore.scene.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Inputs.DeviceType;
import io.anuke.ucore.core.Inputs.InputDevice;
import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.scene.ui.layout.Stack;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.Strings;

public class KeybindDialog extends Dialog{
	private KeybindDialogStyle style;
	private String section = "default";
	private String rebindKey = null;
	private Dialog rebindDialog;
	private ObjectMap<String, Integer> sectionControls = new ObjectMap<String, Integer>();

	public KeybindDialog() {
		super("Rebind Keys");
		style = Core.skin.get(KeybindDialogStyle.class);
		setup();
		addCloseButton();

		Array<String> sections = KeyBinds.getSections();
		section = sections.first();

		if(Inputs.controllersLoaded()){
			Inputs.invokeControl("io.anuke.ucontrol.ControllerBridge", "keybindListen", this);
		}

	}

	public void setStyle(KeybindDialogStyle style){
		this.style = style;
		setup();
	}

	private void setup(){
		content().clear();

		Array<String> sections = KeyBinds.getSections();

		Stack stack = new Stack();
		ButtonGroup<TextButton> group = new ButtonGroup<>();

		for(String section : sections){
			if(!sectionControls.containsKey(section))
				sectionControls.put(section, 0);
			if(sections.size != 1){
				TextButton button = new TextButton(Strings.capitalize(section), "toggle");
				if(section.equals(this.section))
					button.toggle();

				button.clicked(() -> {
					this.section = section;
				});

				group.add(button);
				content().add(button).fill();
			}

			Table table = new Table();

			Label device = new Label("Keyboard");
			device.setColor(style.controllerColor);
			device.setAlignment(Align.center);

			Array<InputDevice> devices = Inputs.getDevices();
			
			Table stable = new Table();

			stable.addButton("<", () -> {
				int i = sectionControls.get(section);
				if(i - 1 >= 0){
					sectionControls.put(section, i - 1);
					KeyBinds.setDevice(section, devices.get(i - 1));
					setup();
				}
			}).disabled(sectionControls.get(section) - 1 < 0).size(40);

			stable.add(device).minWidth(device.getMinWidth() + 60);

			device.setText(Inputs.getDevices().get(sectionControls.get(section)).name);

			stable.addButton(">", () -> {
				int i = sectionControls.get(section);

				if(i + 1 < devices.size){
					sectionControls.put(section, i + 1);
					KeyBinds.setDevice(section, devices.get(i + 1));
					setup();
				}
			}).disabled(sectionControls.get(section) + 1 >= devices.size).size(40);
			
			if(Inputs.controllersLoaded())
				table.add(stable).colspan(3);

			table.row();
			table.add().height(10);
			table.row();

			for(String s : KeyBinds.getBinds(section)){
				Label keylabel = new Label(KeyBinds.toString(section, KeyBinds.get(section, s)));

				keylabel.setColor(style.keyColor);

				table.add(Strings.capitalize(s), style.keyNameColor).left().padRight(40).padLeft(8);
				table.add(keylabel).left().minWidth(90).padRight(20);
				table.addButton("Rebind", () -> {
					openDialog(section, s);
				});
				table.row();
			}

			table.setVisible(() -> this.section.equals(section));

			table.addButton("Reset to Defaults", () -> {
				for(String s : KeyBinds.getBinds(section))
					KeyBinds.resetKey(section, s);
				setup();
				KeyBinds.saveBindings();
			}).colspan(4).padTop(4).fill();

			stack.add(table);
		}

		content().row();

		content().add(stack).colspan(sections.size);

		pack();
	}

	/** Internal use only! */
	public boolean canRebindController(){
		return rebindKey != null && KeyBinds.getType(section) == DeviceType.controller;
	}

	/** Internal use only! */
	public void rebind(int data){
		rebindDialog.hide();
		KeyBinds.rebindKey(section, rebindKey, data);
		KeyBinds.saveBindings();
		rebindKey = null;
		setup();
	}

	/** Internal use only! **/
	public void internalRefresh(){
		setup();
	}

	private void openDialog(String section, String name){
		rebindDialog = new Dialog("Press a key...", "dialog");

		rebindKey = name;

		rebindDialog.getTitleTable().getCells().first().pad(4);
		rebindDialog.addButton("Cancel", ()-> hide()).pad(4);

		if(KeyBinds.getType(section) == DeviceType.keyboard)
			rebindDialog.keyDown(i -> {
				rebindDialog.hide();
				KeyBinds.rebindKey(section, name, i);
				KeyBinds.saveBindings();
				rebindKey = null;
				setup();
			});

		rebindDialog.show(getScene());
	}

	static public class KeybindDialogStyle{
		public Color keyColor = Color.WHITE;
		public Color keyNameColor = Color.WHITE;
		public Color controllerColor = Color.WHITE;
	}
}
