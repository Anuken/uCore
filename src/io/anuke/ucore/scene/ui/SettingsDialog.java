package io.anuke.ucore.scene.ui;

import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.Musics;
import io.anuke.ucore.core.Settings;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.StringProcessor;
import io.anuke.ucore.scene.ui.layout.Table;

public class SettingsDialog extends Dialog{
	private Array<Setting> list = new Array<>();
	private Table table;

	public SettingsDialog() {
		super("Settings");
		addCloseButton();
		
		table = new Table();
		
		table.left();
		content().add(table);
		content().row();
		content().addButton("Reset to Defaults", ()->{
			for(Setting setting : list){
				Settings.put(setting.name, Settings.getDefault(setting.name));
				Settings.save();
			}
			rebuild();
		}).pad(4).left();
	}
	
	public void screenshakePref(){
		sliderPref("screenshake", "Screen Shake", 4, 0, 8, i -> (i / 4f) + "x");
	}
	
	public void volumePrefs(){
		
		sliderPref("sfxvol", "SFX Volume", 10, 0, 10, 1, i-> (int)(i*10) + "%");
		
		sliderPref("musicvol", "Music Volume", 10, 0, 10, 1, i->{
			Musics.updateVolume();
			return (int)(i*10) + "%";
		});
	}
	
	public void sliderPref(String name, String title, int def, int min, int max, StringProcessor s){
		sliderPref(name, title, def, min, max, 1, s);
	}
	
	public void sliderPref(String name, String title, int def, int min, int max, int step, StringProcessor s){
		list.add(new SliderSetting(name, title, def, min, max, step, s));
		Settings.defaults(name, def);
		rebuild();
	}
	
	public void checkPref(String name, String title, boolean def){
		list.add(new CheckSetting(name, title, def, null));
		Settings.defaults(name, def);
		rebuild();
	}
	
	public void checkPref(String name, String title, boolean def, Consumer<Boolean> changed){
		list.add(new CheckSetting(name, title, def, changed));
		Settings.defaults(name, def);
		rebuild();
	}
	
	void rebuild(){
		table.clearChildren();
		
		for(Setting setting : list){
			setting.add();
		}
	}
	
	private abstract class Setting{
		String name;
		
		abstract void add();
	}
	
	private class CheckSetting extends Setting{
		String title;
		boolean def;
		Consumer<Boolean> changed;
		
		CheckSetting(String name, String title, boolean def, Consumer<Boolean> changed){
			this.name = name; this.title = title; this.def = def;
			this.changed = changed;
		}
		
		void add(){
			CheckBox box = new CheckBox(title);
			
			box.setChecked(Settings.getBool(name));
			
			box.changed(()->{
				Settings.putBool(name, box.isChecked);
				Settings.save();
				if(changed != null){
					changed.accept(box.isChecked);
				}
			});
			
			box.left();
			table.add(box).minWidth(box.getPrefWidth()+50).left().padTop(3f);
			table.add().grow();
			table.row();
		}
	}
	
	private class SliderSetting extends Setting{
		String title;
		int def;
		int min;
		int max;
		int step;
		StringProcessor sp;
		
		SliderSetting(String name, String title, int def, int min, int max, int step, StringProcessor s){
			this.name = name; this.title = title; this.def = def; this.min = min; this.max = max; this.step = step; this.sp = s;
		}
		
		void add(){
			Slider slider = new Slider(min, max, step, false);
			
			slider.setValue(Settings.getInt(name));
			
			Label label = new Label(title);
			slider.changed(()->{
				Settings.putInt(name, (int)slider.getValue());
				Settings.save();
				label.setText(title + ": " + sp.get((int)slider.getValue()));
			});
			
			slider.change();
			
			table.add(label).minWidth(label.getPrefWidth()+50).left().padTop(3f);
			table.add(slider).width(180).padTop(3f);
			table.row();
		}
	}

}
