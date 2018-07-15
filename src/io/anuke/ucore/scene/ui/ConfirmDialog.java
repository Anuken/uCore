package io.anuke.ucore.scene.ui;

public class ConfirmDialog extends Dialog{
    Runnable confirm;

    public ConfirmDialog(String title, String text, Runnable confirm){
        super(title, "dialog");
        this.confirm = confirm;
        content().add(text);
        buttons().addButton("Ok", () -> {
            confirm.run();
            hide();
        });
        buttons().addButton("Cancel", () -> hide());
    }

}
