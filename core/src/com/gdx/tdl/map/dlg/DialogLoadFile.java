package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdx.tdl.map.tct.SaveLoad;
import com.gdx.tdl.map.tct.Tactic;
import com.gdx.tdl.util.AssetLoader;

public class DialogLoadFile extends AbstractDialog {
    private SaveLoad saveLoad;

    public DialogLoadFile(SaveLoad saveLoad) {
        super(saveLoad);
        this.saveLoad = saveLoad;
    }

    @Override
    public void dialogDraw() {
        // tabela da label
        Table tableA = new Table(AssetLoader.skinXP);
        tableA.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()*3/4f);
        tableA.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/12f);
        stage.addActor(tableA);

        // textfield
        final Label titleL = new Label("Carregar Ficheiro", AssetLoader.skinXP, "title");
        tableA.add(titleL);

        // tabela do TF
        Table tableB = new Table(AssetLoader.skin);
        tableB.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/2.25f);
        tableB.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/10f);
        stage.addActor(tableB);

        // textfield
        final TextField nameTF = new TextField("", AssetLoader.skinXP);
        nameTF.setMessageText("  nome do ficheiro");
        tableB.add(nameTF).expand().fill();

        // tabela dos botoes
        Table tableC = new Table(AssetLoader.skin);
        tableC.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/8f);
        tableC.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/10f);
        stage.addActor(tableC);

        // botoes
        TextButton loadTB = new TextButton("Carregar", AssetLoader.skin);
        tableC.add(loadTB).expand().fill().padRight(Gdx.graphics.getWidth()/30f);
        TextButton cancelTB = new TextButton("Cancelar", AssetLoader.skin);
        tableC.add(cancelTB).expand().fill().padLeft(Gdx.graphics.getWidth()/30f);

        // listeners
        loadTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameTF.getText().trim();
                Gdx.app.log("Name", name);
                if (loadCloud(name)) {
                    Gdx.app.log("Loaded", "Cloud");
                } else {
                    loadLocal(name);
                    Gdx.app.log("Loaded", "Local");
                }

                AssetLoader.loaded = false;
            }
        });

        cancelTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setFail(false);
                setSuccess(false);
                setShowing(false);
            }
        });
    }

    private void loadLocal(String name) {
        saveLoad.loadLocalData(name);
        setShowing(false);
    }

    private boolean loadCloud(String name) {
        setShowing(false);
        return saveLoad.loadCloudData(name);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
