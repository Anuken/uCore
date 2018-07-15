package io.anuke.ucore.scene.utils;

import com.badlogic.gdx.graphics.Color;
import io.anuke.ucore.function.BooleanConsumer;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.scene.ui.CheckBox;
import io.anuke.ucore.scene.ui.ImageButton;
import io.anuke.ucore.scene.ui.TextButton;
import io.anuke.ucore.scene.ui.TextField;

import static io.anuke.ucore.core.Core.skin;

public class Elements{

    public static CheckBox newCheck(String text, BooleanConsumer listener){
        CheckBox button = new CheckBox(text);
        if(listener != null)
            button.changed(() -> listener.accept(button.isChecked()));
        return button;
    }

    public static TextButton newButton(String text, Runnable listener){
        TextButton button = new TextButton(text);
        if(listener != null)
            button.changed(listener);

        return button;
    }

    public static TextButton newButton(String text, String style, Runnable listener){
        TextButton button = new TextButton(text, style);
        if(listener != null)
            button.changed(listener);

        return button;
    }

    public static ImageButton newImageButton(String icon, Runnable listener){
        ImageButton button = new ImageButton(skin.getDrawable(icon));
        if(listener != null)
            button.changed(listener);
        return button;
    }

    public static ImageButton newImageButton(String icon, float size, Runnable listener){
        ImageButton button = new ImageButton(skin.getDrawable(icon));
        button.resizeImage(size);
        if(listener != null)
            button.changed(listener);
        return button;
    }

    public static ImageButton newImageButton(String style, String icon, float size, Runnable listener){
        ImageButton button = new ImageButton(icon, style);
        button.resizeImage(size);
        if(listener != null)
            button.changed(listener);
        return button;
    }

    public static ImageButton newImageButton(String icon, float size, Color color, Runnable listener){
        ImageButton button = new ImageButton(skin.getDrawable(icon));
        button.resizeImage(size);
        button.getImage().setColor(color);
        if(listener != null)
            button.changed(listener);
        return button;
    }

    public static ImageButton newToggleImageButton(String icon, float size, boolean on, BooleanConsumer listener){
        ImageButton button = new ImageButton(icon, "toggle");
        button.setChecked(on);
        button.resizeImage(size);
        button.clicked(() -> listener.accept(button.isChecked()));
        return button;
    }

    public static TextField newField(String text, Consumer<String> listener){
        TextField field = new TextField(text);
        if(listener != null)
            field.changed(() -> listener.accept(field.getText()));

        return field;
    }
}
