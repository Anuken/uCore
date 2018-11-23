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
        buttons().addButton("Cancel", this::hide);
    }

    public ConfirmDialog(String title, String text, Runnable confirm, Runnable cancel){
        super(title, "dialog");
        this.confirm = confirm;
        content().add(text);
        buttons().addButton("Ok", () -> {
            confirm.run();
            hide();
        });
        buttons().addButton("Cancel", () -> {
            cancel.run();
            hide();
        }).width(110f);
    }

}
