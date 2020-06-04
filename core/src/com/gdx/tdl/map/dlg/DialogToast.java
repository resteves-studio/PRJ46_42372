package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.gdx.tdl.util.AssetLoader;

public class DialogToast extends AbstractDialog {

    public DialogToast() {
        super(null);
    }

    @Override
    public void dialogDraw() {
        dialog = new Dialog("Sem taticas", AssetLoader.skinXP, "dialog") {
            @Override
            protected void result(Object object) {
                dialog.remove();
                showing = false;
            }
        };

        dialog.text("Nenhuma tatica por reproduzir");
        dialog.button("Percebi", true);
        dialog.padBottom(Gdx.graphics.getHeight()/25f);
        dialog.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/4f);
        dialog.setPosition(Gdx.graphics.getWidth()/2f - dialog.getWidth()/2f, Gdx.graphics.getHeight()/2f - dialog.getHeight()/2f);

        addDialogToStage();
    }
}
