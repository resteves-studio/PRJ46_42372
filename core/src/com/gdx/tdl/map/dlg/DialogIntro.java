package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.sgn.StageManager;

public class DialogIntro extends AbstractDialog {
    protected boolean load;

    public DialogIntro() {
        super(null);
        this.showing = true;
        this.load = false;
    }

    @Override
    public void dialogDraw() {
        // tabela do TF
        Table tableA = new Table(AssetLoader.skinXP);
        tableA.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()*3/4f);
        tableA.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/12f);
        stage.addActor(tableA);

        // textfield
        final Label titleL = new Label("Bem-vindo, Coach", AssetLoader.skinXP, "title");
        tableA.add(titleL);

        // tabela dos botoes
        Table tableB = new Table(AssetLoader.skin);
        tableB.setPosition(Gdx.graphics.getWidth()/2.5f, Gdx.graphics.getHeight()/4f);
        tableB.setSize(Gdx.graphics.getWidth()/5f, Gdx.graphics.getHeight()/3f);
        stage.addActor(tableB);

        // botoes
        TextButton newTacticTB = new TextButton("Gerir Tática", AssetLoader.skin);
        tableB.add(newTacticTB).expand().fill().padBottom(Gdx.graphics.getHeight()/50f);
        tableB.row();
        TextButton updPwdTB = new TextButton("Recuperar Palavra-Passe", AssetLoader.skin);
        tableB.add(updPwdTB).expand().fill();


        // listeners
        newTacticTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setShowing(false);
            }
        });

        updPwdTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StageManager.getInstance().showScreen(ScreenEnum.UPD_PWD);
            }
        });
    }
}
