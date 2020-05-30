package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.gdx.tdl.util.AssetLoader;

import javax.swing.GroupLayout;

public class DialogSaveFile extends AbstractDialog {
    public DialogSaveFile(String header) {
        super(header);
    }

    @Override
    public void dialogDraw() {
        dialog = new Dialog(header, AssetLoader.skinXP, "dialog") { // TODO header fica fora do sitio
            @Override
            protected void result(Object object) {
                dialogResult(object);
            }
        };

        TextField tacticNameTF = new TextField("", AssetLoader.skinXP);
        tacticNameTF.setMessageText("tactic's name");

        dialog.getContentTable().add(tacticNameTF).width(500).center(); // TODO ver tamanho e posicao
        dialog.button("Save", true).pad(Gdx.graphics.getWidth()/15f);
        dialog.button("Cancel", false);
        dialog.padBottom(Gdx.graphics.getHeight()/25f);
        dialog.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/4f);
        dialog.setPosition(Gdx.graphics.getWidth()/2f - dialog.getWidth()/2f, Gdx.graphics.getHeight()/2f - dialog.getHeight()/2f);

        addDialogToStage();
    }

    @Override
    protected void dialogResult(Object object) {
        if (object.equals(true)) {
            // true save as
        } else {
            dialog.remove();
            showing = false;
        }
    }
}
