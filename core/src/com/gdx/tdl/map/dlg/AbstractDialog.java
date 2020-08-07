package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.gdx.tdl.map.tct.SaveLoad;
import com.gdx.tdl.map.tct.Tactic;

public abstract class AbstractDialog {
    SaveLoad saveLoad;
    Tactic tactic;
    Dialog dialog;
    Stage stage;

    boolean showing;

    public AbstractDialog(SaveLoad saveLoad) {
        this.stage = new Stage();
        this.saveLoad = saveLoad;

        setShowing(false);

        dialogDraw();
    }

    // abstract methods
    protected abstract void dialogDraw();

    public void dialogStageDraw() {
        Gdx.gl.glClearColor(100/255f, 100/255f, 100/255f, 255f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    // ----- getters -----
    public boolean isShowing() { return this.showing; }
    public boolean isFail() { return this.saveLoad.isFail(); }
    public boolean isSuccess() { return this.saveLoad.isSuccess(); }
    public Stage getStage() { return this.stage; }
    public Tactic getTacticFromSaveLoad() { return this.saveLoad.getTactic(); }

    // ----- setters -----
    public void setShowing(boolean showing) { this.showing = showing; }
    public void setFail(boolean fail) { this.saveLoad.setFail(fail); }
    public void setSuccess(boolean success) { this.saveLoad.setSuccess(success); }
    public void addDialogToStage() { this.stage.addActor(dialog); }
}
