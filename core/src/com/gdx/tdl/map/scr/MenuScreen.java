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
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.sgn.ButtonUtil;
import com.gdx.tdl.util.sgn.StageManager;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.functional.Consumer;

/**
 * Ecra destinado a possibilitar a escolha de qualquer opcao existente na app
 *
 * Tera a opcao de guardar a frame, reproduzir a jogada, guardar o ficheiro
 * Tera tambem o botao para o menu de ajuda
 *
 */
class MenuScreen {
    private String offenseColor, defenseColor, ballColor, courtColor;
    private Table tableOption1, tableOption2;
    private Color normalColor, selectedColor;
    private Table[] buttons;
    private Stage stage;
    private boolean selected;
    private int currentOption;

    MenuScreen() {
        this.stage = new Stage();
        this.selected = false;
        this.currentOption = 1;

        this.offenseColor = "Azul";
        this.defenseColor = "Vermelho";
        this.ballColor = "Laranja";
        this.courtColor = "Madeira";

        this.normalColor = Color.ORANGE;
        this.selectedColor = Color.BROWN;

        this.buttons = buttons();
        stage.addActor(buttons[0]);

        this.tableOption1 = option1();
        this.tableOption2 = option2();
    }

    //
    public boolean menuDraw() {
        // cor de fundo
        Gdx.gl.glClearColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        removeAllTables();

        switch (currentOption) {
            case 1:
                stage.addActor(tableOption1);
                setOptionColor(1);
                break;
            case 2:
                stage.addActor(tableOption2);
                setOptionColor(2);
                break;
        }

        stage.draw();

        return selected;
    }

    //
    private Button[] buttons() {
        // menu button
        ImageButton menuBtn = new ImageButton(AssetLoader.skinXP, "default");
        menuBtn.setPosition(Gdx.graphics.getWidth()/60f, Gdx.graphics.getHeight()*6/7f);
        menuBtn.setSize(Gdx.graphics.getHeight()/8.5f, Gdx.graphics.getHeight()/8.5f);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(AssetLoader.menuOption));
        menuBtn.setStyle(style);
        menuBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                selected = false;
            }
        });

        // TODO option buttons
        Table tableHelpOptions = new Table(AssetLoader.skinXP);
        tableHelpOptions.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/6f);
        tableHelpOptions.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()*4/5f);
        tableHelpOptions.align(Align.left);

        Button option1 = new TextButton("Interface", AssetLoader.skin, "default");
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

        Button option2 = new TextButton("Utilizador", AssetLoader.skin, "default");
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

        tableHelpOptions.add(option1).expand().fill().padRight(Gdx.graphics.getHeight()/75f);
        tableHelpOptions.add(option2).expand().fill();

        stage.addActor(tableHelpOptions);

        return new Button[] { menuBtn, option1, option2 };
    }

    //
    private Table option1() {
        // tabela principal
        Table tableOption = new Table(AssetLoader.skinXP);
        tableOption.setSize(Gdx.graphics.getWidth()*2/3f, Gdx.graphics.getHeight()*2/3f);
        tableOption.setPosition(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/10f);

        tableOption.add(new Label("Cor do Ataque", AssetLoader.skinXP)).padRight(Gdx.graphics.getWidth()/25f);
        tableOption.add(new Label("Cor da Defesa", AssetLoader.skinXP)).padRight(Gdx.graphics.getWidth()/25f);
        tableOption.add(new Label("Cor da Bola", AssetLoader.skinXP)).padRight(Gdx.graphics.getWidth()/25f);
        tableOption.add(new Label("Cor do Campo", AssetLoader.skinXP)).row();
        tableOption.add(new Label("", AssetLoader.skinXP)).padBottom(Gdx.graphics.getWidth()/50f).row();

        ButtonGroup<CheckBox> offenseColorGroup = new ButtonGroup();
        offenseColorGroup.setMinCheckCount(1);
        offenseColorGroup.setMaxCheckCount(1);

        ButtonGroup<CheckBox> defenseColorGroup = new ButtonGroup();
        defenseColorGroup.setMinCheckCount(1);
        defenseColorGroup.setMaxCheckCount(1);

        ButtonGroup<CheckBox> ballColorGroup = new ButtonGroup();
        ballColorGroup.setMinCheckCount(1);
        ballColorGroup.setMaxCheckCount(1);

        ButtonGroup<CheckBox> courtColorGroup = new ButtonGroup();
        courtColorGroup.setMinCheckCount(1);
        courtColorGroup.setMaxCheckCount(1);

        String[] defaultColorNames = new String[] { "Laranja", "Vermelho", "Azul", "Verde", "Amarelo", "Escuro", "Claro" };

        for (int i = 0; i < defaultColorNames.length; i++) {
            CheckBox cb1 = new CheckBox(defaultColorNames[i], AssetLoader.skinXP, "radio");
            offenseColorGroup.add(cb1);
            tableOption.add(cb1).padRight(Gdx.graphics.getWidth()/20f).left();

            CheckBox cb2 = new CheckBox(defaultColorNames[i], AssetLoader.skinXP, "radio");
            defenseColorGroup.add(cb2);
            tableOption.add(cb2).padRight(Gdx.graphics.getWidth()/20f).left();

            CheckBox cb3 = new CheckBox(defaultColorNames[i], AssetLoader.skinXP, "radio");
            ballColorGroup.add(cb3);
            tableOption.add(cb3).padRight(Gdx.graphics.getWidth()/20f).left();

            CheckBox cb4 = new CheckBox(i == 0 ? "Madeira" : defaultColorNames[i], AssetLoader.skinXP, "radio");
            courtColorGroup.add(cb4);
            tableOption.add(cb4).padBottom(Gdx.graphics.getHeight()/75f).left();
            tableOption.row();
        }

        Button updateButton = new TextButton("Atualizar", AssetLoader.skin);
        updateButton.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("UPDATE", "COLORS");
                setOffenseColor(String.valueOf(offenseColorGroup.getChecked().getLabel().getText()));
                setDefenseColor(String.valueOf(defenseColorGroup.getChecked().getLabel().getText()));
                setBallColor(String.valueOf(ballColorGroup.getChecked().getLabel().getText()));
                setCourtColor(String.valueOf(courtColorGroup.getChecked().getLabel().getText()));
                selected = false;
            }
        });

        tableOption.add(new Label("", AssetLoader.skin)).fill();
        tableOption.add(updateButton).padTop(Gdx.graphics.getWidth()/20f).fill();

        /*Image option2 = new Image(AssetLoader.court_green);
        tableOption.add(option2);*/

        return tableOption;
    }

    //
    private Table option2() {
        // tabela principal
        Table tableOption = new Table(AssetLoader.skin);
        tableOption.setSize(Gdx.graphics.getWidth()/5f, Gdx.graphics.getHeight()*3/5f);
        tableOption.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/9f);

        Image icon = new Image(AssetLoader.icon);

        Button resetPwd = new TextButton("Recuperar Palavra-Passe", AssetLoader.skin);
        resetPwd.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                setOptionToShow(2);
                StageManager.getInstance().showScreen(ScreenEnum.UPD_PWD);
                Gdx.app.log("RESET", "PASSWORD");
            }
        });

        Button logout = new TextButton("Terminar Sessão", AssetLoader.skin);
        logout.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                setOptionToShow(2);
                GdxFIRAuth.inst().signOut().then(o -> Gdx.app.log("LOGOUT", "SUCCESS"));
                StageManager.getInstance().showScreen(ScreenEnum.LOGIN);
            }
        });

        tableOption.add(icon).expand().fill().padBottom(Gdx.graphics.getHeight() / 10f).row();
        tableOption.add(resetPwd).expand().fill().padBottom(Gdx.graphics.getHeight() / 15f).row();
        tableOption.add(logout).expand().fill();

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
            if (actor.isDescendantOf(tableOption1) || actor.isDescendantOf(tableOption2)) {
                actor.remove();
            }
        }
    }

    // getters
    Stage getStage() { return this.stage; }
    String getOffenseColor() { return this.offenseColor; }
    String getDefenseColor() { return this.defenseColor; }
    String getBallColor() { return this.ballColor; }
    String getCourtColor() { return this.courtColor; }
    int currentOptionToShow() { return this.currentOption; }

    // setters
    void setSelected() { this.selected = true; }
    private void setOffenseColor(String offenseColor) { this.offenseColor = offenseColor; }
    private void setDefenseColor(String defenseColor) { this.defenseColor = defenseColor; }
    private void setBallColor(String ballColor) { this.ballColor = ballColor; }
    private void setCourtColor(String courtColor) { this.courtColor = courtColor; }
    private void setOptionToShow(int currentOption) { this.currentOption = currentOption; }
}
