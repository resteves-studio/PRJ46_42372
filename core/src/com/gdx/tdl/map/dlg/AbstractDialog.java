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

    boolean showing, wasTacticLoaded;

    public AbstractDialog(Tactic tactic) {
        this.stage = new Stage();
        this.tactic = tactic;
        this.saveLoad = new SaveLoad(tactic);

        setShowing(false);
        setTacticLoaded(false);

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
    public boolean wasTacticLoaded() { return this.wasTacticLoaded; }
    public Stage getStage() { return this.stage; }
    public Tactic getTacticFromSL() { return this.saveLoad.getTactic(); }

    // ----- setters -----
    public void setShowing(boolean showing) { this.showing = showing; }
    public void setTacticLoaded(boolean wasTacticLoaded) { this.wasTacticLoaded = wasTacticLoaded; }
    public void addDialogToStage() { this.stage.addActor(dialog); }
}
