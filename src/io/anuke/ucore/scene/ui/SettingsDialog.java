package io.anuke.ucore.scene.ui;

import io.anuke.ucore.core.Musics;
import io.anuke.ucore.core.Settings;
import io.anuke.ucore.scene.ui.layout.Table;

public class SettingsDialog extends Dialog{

	public SettingsDialog() {
		super("Settings");
		addCloseButton();
	}
	
	public void sliderPref(String name, String title, int def, int min, int max,StringProcessor s){
		sliderPref(name, title, def, min, max, 1, s);
	}
	
	public void screenshakePref(){
		sliderPref("screenshake", "Screen Shake", 4, 0, 8, i -> {
			return (i / 4f) + "x";
		});
	}
	
	public void volumePrefs(){
		
		sliderPref("sfxvol", "SFX Volume", 10, 0, 10, 1, i->{
			return (int)(i*10) + "%";
		});
		
		sliderPref("musicvol", "Music Volume", 10, 0, 10, 1, i->{
			Musics.updateVolume();
			return (int)(i*10) + "%";
		});
	}
	
	public void sliderPref(String name, String title, int def, int min, int max, int step, StringProcessor s){
		Table table = getContentTable();
		Slider slider = new Slider(min, max, step, false);
		Settings.defaults(name, def);
		
		slider.setValue(Settings.getInt(name));
		
		Label label = new Label(title);
		slider.changed(()->{
			Settings.putInt(name, (int)slider.getValue());
			Settings.save();
			label.setText(title + ": " + s.get((int)slider.getValue()));
		});
		
		slider.change();
		table.add(label).minWidth(label.getPrefWidth()+50).left();
		table.add(slider);
		table.addButton("Reset", ()->{
			slider.setValue(def);
			slider.change();
		});
		table.row();
	}
	
	public void checkPref(String name, String title, boolean def){
		Table table = getContentTable();
		CheckBox box = new CheckBox(title);
		Settings.defaults(name, def);
		
		box.setChecked(Settings.getBool(name));
		
		box.changed(()->{
			Settings.putBool(name, box.isChecked);
			Settings.save();
		});
		
		box.left();
		table.add(box).minWidth(box.getPrefWidth()+50).left();
		table.add().grow();
		table.addButton("Reset", ()->{
			box.setChecked(def);
			box.change();
		});
		table.row();
	}
	
	public static interface StringProcessor{
		String get(int i);
	}

}
