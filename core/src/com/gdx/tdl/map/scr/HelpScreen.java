package com.gdx.tdl.map.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.map.OptionButton;
import com.gdx.tdl.util.map.OptionsTable;

/**
 * Ecra destinado a explicar o que faz cada parte da aplicacao
 *
 * Tera a imagem de cada botao e o procedimento de cada movimento
 * Tera um botao de voltar atras para o Court
 *
 * TODO por enquanto, colocar so o botao para voltar atras
 */
class HelpScreen {
    private OptionButton[] helpOptions;
    private OptionsTable optionsTable;

    HelpScreen(World world, OptionsTable optionsTable) {
        super();

        this.optionsTable = optionsTable;

        optionsTable.helpOptionsDraw(world);
        helpOptions = optionsTable.getHelpOptions();
    }

    public void helpDraw() {
        // cor de fundo
        Gdx.gl.glClearColor(200.0f/255, 120.0f/255, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        optionsTable.update(helpOptions);
    }

    // getters
    OptionButton[] getHelpOptions() { return helpOptions; }
}
