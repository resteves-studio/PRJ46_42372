package com.gdx.tdl.map.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.map.OptionButton;
import com.gdx.tdl.util.map.OptionsTable;

/**
 * Ecra destinado a possibilitar a escolha de qualquer opcao existente na app
 *
 * Tera a opcao de guardar a frame, reproduzir a jogada, guardar o ficheiro
 * Tera tambem o botao para o menu de ajuda
 *
 * TODO talvez estender de AbstractScreen e la colocar um automato entre "ecras"
 */
class MenuScreen {
    private OptionButton[] menuOptions;
    private OptionsTable optionsTable;

    boolean frameAdded;
    int numFrames;

    MenuScreen(World world, OptionsTable optionsTable) {
        super();

        this.optionsTable = optionsTable;
        this.frameAdded = false;
        this.numFrames = 0;

        optionsTable.menuOptionsDraw(world);
        menuOptions = optionsTable.getMenuOptions();
    }

    public void menuDraw() {
        // cor de fundo
        Gdx.gl.glClearColor(200.0f/255, 120.0f/255, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        optionsTable.update(menuOptions);
    }

    // getters
    OptionButton[] getMenuOptions() { return menuOptions; }
    boolean isFrameAdded() { return frameAdded; }

    // setters
    void setFrameAdded(boolean frameAdded) { this.frameAdded = frameAdded; }
    void setNumFrames(int numFrames) { this.numFrames = numFrames; }
}
