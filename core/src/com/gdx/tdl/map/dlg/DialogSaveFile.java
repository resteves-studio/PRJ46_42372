package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.gdx.tdl.util.AssetLoader;

public class DialogSaveFile extends AbstractDialog {
    public DialogSaveFile(String header) {
        super(header);
    }

    @Override
    protected void dialogDraw() {
        TextField tacticNameTF = new TextField("", AssetLoader.skinXP);
        tacticNameTF.setMessageText("tactic's name");

        dialog.add(tacticNameTF);
        dialog.row();
        dialog.button("Save", true);
        dialog.button("Cancel", false);
        dialog.padBottom(Gdx.graphics.getHeight()/25f);
        dialog.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/4f);
        dialog.setPosition(Gdx.graphics.getWidth()/2f - dialog.getWidth()/2f, Gdx.graphics.getHeight()/2f - dialog.getHeight()/2f);

        addDialogToStage();
    }

    @Override
    protected void dialogResult() {
        Gdx.app.log("RESULT", "hiding");
        dialog.hide(); // TODO esta a remover o dialog do stage e nao da para adicionar de novo
        showing = false;
    }
}
