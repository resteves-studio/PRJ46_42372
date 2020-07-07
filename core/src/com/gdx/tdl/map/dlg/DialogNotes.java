package com.mov.ags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class DialogNotes extends AbstractDialog {

    public DialogNotes(Tactic tactic) {
        super(tactic);
    }

    @Override
    public void dialogDraw() {
        // tabela do TA
        Table tableB = new Table(AssetLoader.skinXP);
        tableB.setPosition(Gdx.graphics.getWidth()/6f, Gdx.graphics.getHeight()/3f);
        tableB.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()/2f);
        stage.addActor(tableB);

        // text area
        final TextArea notesTA = new TextArea("", AssetLoader.skinXP);
        notesTA.setMessageText(" notes");
        tableB.add(notesTA).expand().fill();

        /*/ scroll panel
        Table container = new Table(AssetLoader.skinXP);
        ScrollPane scroll = new ScrollPane(container);
        scroll.setScrollbarsVisible(true);
        tableB.add(container);*/

        // tabela dos botoes
        Table tableC = new Table(AssetLoader.skinXP);
        tableC.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/8f);
        tableC.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/10f);
        stage.addActor(tableC);

        // botoes
        TextButton submitTB = new TextButton("Submit", AssetLoader.skinXP);
        tableC.add(submitTB).expand().fill().padRight(Gdx.graphics.getWidth()/30f);
        TextButton cancelTB = new TextButton("Cancel", AssetLoader.skinXP);
        tableC.add(cancelTB).expand().fill().padLeft(Gdx.graphics.getWidth()/30f);

        // listeners
        submitTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO
            }
        });

        cancelTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showing = false;
            }
        });
    }
}
