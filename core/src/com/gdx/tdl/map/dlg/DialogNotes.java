package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdx.tdl.map.tct.SaveLoad;
import com.gdx.tdl.map.tct.TacticSingleton;
import com.gdx.tdl.util.AssetLoader;

public class DialogNotes extends AbstractDialog {

    public DialogNotes(SaveLoad saveLoad) {
        super(saveLoad);
    }

    @Override
    public void dialogDraw() {
        // tabela do TA
        Table tableB = new Table(AssetLoader.skinXP);
        tableB.setPosition(Gdx.graphics.getWidth()/6f, Gdx.graphics.getHeight()/3f);
        tableB.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()/2f);
        stage.addActor(tableB);

        // text area
        final TextArea notesTA = new TextArea(TacticSingleton.getInstance().getTactic().getNotes(), AssetLoader.skinXP);
        final StringBuilder strBuilder = new StringBuilder();
        notesTA.setMessageText(" notas");
        // TODO resolver problema da newline
        notesTA.setTextFieldListener((textField, c) -> {
            if (c == '\t') {
                notesTA.setMessageText(strBuilder.append("\n").toString());
            }
        });
        tableB.add(notesTA).expand().fill();

        // tabela dos botoes
        Table tableC = new Table(AssetLoader.skin);
        tableC.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/8f);
        tableC.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/10f);
        stage.addActor(tableC);

        // botoes
        TextButton submitTB = new TextButton("Submeter", AssetLoader.skin);
        tableC.add(submitTB).expand().fill().padRight(Gdx.graphics.getWidth()/30f);
        TextButton cancelTB = new TextButton("Cancelar", AssetLoader.skin);
        tableC.add(cancelTB).expand().fill().padLeft(Gdx.graphics.getWidth()/30f);

        // listeners
        submitTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TacticSingleton.getInstance().getTactic().setNotes(notesTA.getText());
                Gdx.app.log("NOTES", TacticSingleton.getInstance().getTactic().getNotes());
                setShowing(false);
            }
        });

        cancelTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setShowing(false);
            }
        });
    }
}
