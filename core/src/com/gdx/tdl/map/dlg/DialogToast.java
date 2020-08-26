package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.gdx.tdl.map.tct.SaveLoad;
import com.gdx.tdl.util.AssetLoader;

public class DialogToast extends AbstractDialog {
    private String text;

    public DialogToast(String text, SaveLoad saveLoad) {
        super(saveLoad);
        this.text = text;
    }

    @Override
    public void dialogDraw() {
        dialog = new Dialog("", AssetLoader.skin, "default") {
            @Override
            protected void result(Object object) {
                setFail(false);
                setSuccess(false);
                setShowing(false);
            }
        };

        dialog.text(String.valueOf(this.text));
        dialog.button("Percebi", true);
        dialog.padBottom(Gdx.graphics.getHeight()/25f);
        dialog.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/4f);
        dialog.setPosition(Gdx.graphics.getWidth()/2f - dialog.getWidth()/2f, Gdx.graphics.getHeight()/2f - dialog.getHeight()/2f);

        addDialogToStage();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
