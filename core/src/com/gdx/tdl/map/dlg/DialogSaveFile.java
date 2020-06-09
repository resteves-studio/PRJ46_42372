package com.gdx.tdl.map.dlg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdx.tdl.map.tct.Tactic;
import com.gdx.tdl.util.AssetLoader;

public class DialogSaveFile extends AbstractDialog {

    public DialogSaveFile(Tactic tactic) {
        super(tactic);
    }

    @Override
    public void dialogDraw() {
        // tabela do TF
        Table tableA = new Table(AssetLoader.skinXP);
        tableA.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()*4/5f);
        tableA.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/12f);
        stage.addActor(tableA);

        // textfield
        final Label titleL = new Label("Save File", AssetLoader.skinXP, "title");
        tableA.add(titleL);

        // tabela do TF
        Table tableB = new Table(AssetLoader.skinXP);
        tableB.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/1.75f);
        tableB.setSize(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/12f);
        stage.addActor(tableB);

        // textfield
        final TextField nameTF = new TextField("", AssetLoader.skinXP);
        nameTF.setMessageText("  name");
        tableB.add(nameTF).expand().fill();

        // tabela dos botoes
        Table tableC = new Table(AssetLoader.skinXP);
        tableC.setPosition(Gdx.graphics.getWidth()/2.5f, Gdx.graphics.getHeight()/8f);
        tableC.setSize(Gdx.graphics.getWidth()/5f, Gdx.graphics.getHeight()/3f);
        stage.addActor(tableC);

        // botoes
        TextButton localTB = new TextButton("Local", AssetLoader.skinXP);
        tableC.add(localTB).expand().fill().padBottom(Gdx.graphics.getHeight()/75f);
        tableC.row();
        TextButton cloudTB = new TextButton("Cloud", AssetLoader.skinXP);
        tableC.add(cloudTB).expand().fill().padBottom(Gdx.graphics.getHeight()/75f);
        tableC.row();
        TextButton bothTB = new TextButton("Both", AssetLoader.skinXP);
        tableC.add(bothTB).expand().fill();
        tableC.row();
        TextButton cancelTB = new TextButton("Cancel", AssetLoader.skinXP);
        tableC.add(cancelTB).expand().fill().padTop(Gdx.graphics.getHeight()/20f);


        // listeners
        localTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveLocal(nameTF.getText().trim());
            }
        });

        cloudTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveCloud(nameTF.getText().trim());
            }
        });

        bothTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameTF.getText().trim();
                saveLocal(name);
                saveCloud(name);
            }
        });

        cancelTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showing = false;
            }
        });
    }

    private void saveLocal(String name) {
        // TODO
        if (!name.isEmpty())
            saveLoad.getTactic().setName(name);
        saveLoad.saveLocalData();

        showing = false;
    }

    private void saveCloud(String name) {
        // TODO
        if (!name.isEmpty())
            saveLoad.getTactic().setName(name);
        saveLoad.saveCloudData();

        showing = false;
    }
}
