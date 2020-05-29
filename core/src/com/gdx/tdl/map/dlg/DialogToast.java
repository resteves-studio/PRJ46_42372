package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;

public class DialogToast extends AbstractDialog {
    public DialogToast(String header) {
        super(header);
    }

    @Override
    protected void dialogDraw() {
        dialog.text("Nenhuma tatica por reproduzir");
        dialog.button("Percebi", true);
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
