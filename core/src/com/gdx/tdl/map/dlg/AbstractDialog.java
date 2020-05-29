package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.gdx.tdl.util.AssetLoader;

public abstract class AbstractDialog {
    boolean showing = false;
    Dialog dialog;
    Stage stage;

    public AbstractDialog(String header) {
        this.stage = new Stage();

        dialog = new Dialog(header, AssetLoader.skinXP, "dialog") {
            @Override
            protected void result(Object object) {
                dialogResult();
            }
        };

        dialogDraw();
    }

    // abstract methods
    protected abstract void dialogDraw();
    protected abstract void dialogResult();

    public void dialogStageDraw() {
        Gdx.gl.glClearColor(150/255f, 150/255f, 150/255f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    // ----- getters -----
    public boolean getShowing() { return this.showing; }
    public Stage getStage() { return this.stage; }

    // ----- setters -----
    public void setShowing(boolean showing) { this.showing = showing; }
    void addDialogToStage() { this.stage.addActor(dialog); }
}
