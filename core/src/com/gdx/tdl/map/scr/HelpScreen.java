package com.gdx.tdl.map.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gdx.tdl.util.AssetLoader;

/**
 * Ecra destinado a explicar o que faz cada parte da aplicacao
 *
 * Tera a imagem de cada botao e o procedimento de cada movimento
 * Tem um botao de voltar atras para o Court
 */
class HelpScreen {
    private Table tableOption1, tableOption2, tableOption3, tableOption4;
    private Color normalColor, selectedColor;
    private Button[] buttons;
    private Stage stage;
    private boolean selected;
    private int option;

    HelpScreen() {
        this.stage = new Stage();
        this.selected = false;
        this.option = 1;

        this.normalColor = Color.ORANGE;
        this.selectedColor = Color.BROWN;

        this.buttons = buttons();
        stage.addActor(buttons[0]);

        this.tableOption1 = option1();
        this.tableOption2 = option2();
        this.tableOption3 = option3();
        this.tableOption4 = option4();
    }

    public boolean helpDraw() {
        // cor de fundo
        Gdx.gl.glClearColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        removeAllTables();

        switch (option) {
            case 1:
                stage.addActor(tableOption1);
                setOptionColor(1);
                break;
            case 2:
                stage.addActor(tableOption2);
                setOptionColor(2);
                break;
            case 3:
                stage.addActor(tableOption3);
                setOptionColor(3);
                break;
            case 4:
                stage.addActor(tableOption4);
                setOptionColor(4);
                break;
        }

        stage.draw();

        return selected;
    }

    //
    private Button[] buttons() {
        // go back button
        ImageButton backBtn = new ImageButton(AssetLoader.skinXP, "default");
        backBtn.setPosition(Gdx.graphics.getWidth()/60f, Gdx.graphics.getWidth()/50f);
        backBtn.setSize(Gdx.graphics.getHeight()/8.5f, Gdx.graphics.getHeight()/8.5f);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(AssetLoader.goback));
        backBtn.setStyle(style);
        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                selected = false;
                Gdx.app.log("GO", "BACK");
            }
        });

        // option buttons
        Table tableHelpOptions = new Table(AssetLoader.skin);
        tableHelpOptions.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()/6f);
        tableHelpOptions.setPosition(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()*4/5f);

        Button option1 = new TextButton("Ataque", AssetLoader.skin, "default");
        option1.setColor(selectedColor);
        option1.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                setOptionToShow(1);
                Gdx.app.log("OPTION 1", "CHANGE");
            }
        });

        Button option2 = new TextButton("Defesa", AssetLoader.skin, "default");
        option2.setColor(normalColor);
        option2.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                setOptionToShow(2);
                Gdx.app.log("OPTION 2", "CHANGE");
            }
        });

        Button option3 = new TextButton("Tatica", AssetLoader.skin, "default");
        option3.setColor(normalColor);
        option3.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                setOptionToShow(3);
                Gdx.app.log("OPTION 3", "CHANGE");
            }
        });

        Button option4 = new TextButton("Ficheiros", AssetLoader.skin, "default");
        option4.setColor(normalColor);
        option4.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                setOptionToShow(4);
                Gdx.app.log("OPTION 4", "CHANGE");
            }
        });

        tableHelpOptions.add(option1).expand().fill().padRight(Gdx.graphics.getHeight() / 75f);
        tableHelpOptions.add(option2).expand().fill().padRight(Gdx.graphics.getHeight() / 75f);
        tableHelpOptions.add(option3).expand().fill().padRight(Gdx.graphics.getHeight() / 75f);
        tableHelpOptions.add(option4).expand().fill();

        stage.addActor(tableHelpOptions);

        return new Button[] {backBtn, option1, option2, option3, option4};
    }

    //
    private Table option1() {
        // tabela principal
        Table tableOption = new Table(AssetLoader.skinXP);
        tableOption.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()*2/3f);
        tableOption.setPosition(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/10f);

        Image option1 = new Image(AssetLoader.helpAtaque);
        tableOption.add(option1);

        return tableOption;
    }

    //
    private Table option2() {
        // tabela principal
        Table tableOption = new Table(AssetLoader.skinXP);
        tableOption.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()*2/3f);
        tableOption.setPosition(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/10f);

        Image option2 = new Image(AssetLoader.helpDefesa);
        tableOption.add(option2);

        return tableOption;
    }

    //
    private Table option3() {
        // tabela principal
        Table tableOption = new Table(AssetLoader.skinXP);
        tableOption.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()*2/3f);
        tableOption.setPosition(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/10f);

        Image option3 = new Image(AssetLoader.helpTatica);
        tableOption.add(option3);

        return tableOption;
    }

    //
    private Table option4() {
        // tabela principal
        Table tableOption = new Table(AssetLoader.skinXP);
        tableOption.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()*2/3f);
        tableOption.setPosition(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/10f);

        Image option4 = new Image(AssetLoader.helpFicheiros);
        tableOption.add(option4);

        return tableOption;
    }

    //
    private void setOptionColor(int option) {
        for (int i = 1; i < buttons.length; i++) {
            if (option == i) buttons[i].setColor(selectedColor);
            else buttons[i].setColor(normalColor);
        }
    }

    //
    private void removeAllTables() {
        for (Actor actor : stage.getActors()) {
            if (actor.isDescendantOf(tableOption1) || actor.isDescendantOf(tableOption2) || actor.isDescendantOf(tableOption3) || actor.isDescendantOf(tableOption4)) {
                actor.remove();
            }
        }
    }

    // getters
    Stage getStage() { return this.stage; }

    // setters
    void setSelected() { this.selected = true; }
    void setOptionToShow(int option) { this.option = option; }
}
