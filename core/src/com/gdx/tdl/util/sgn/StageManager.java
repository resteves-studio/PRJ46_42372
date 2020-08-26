package com.gdx.tdl.util.sgn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.map.AbstractScreen;

public class StageManager {

    // singleton instance
    private static StageManager instance;

    // referencia a Game
    private Game game;

    // construtor
    private StageManager() {
        super();
    }

    // retorna a instancia singleton
    public static StageManager getInstance() {
        if (instance == null) {
            instance = new StageManager();
        }
        return instance;
    }

    // inicializa a game class
    public void initialize(Game game) {
        this.game = game;
    }

    // mostra o tipo de screen pretendido (Court, Menu, Login, Help...)
    public void showScreen(ScreenEnum screenEnum, Object... params) {
        // screen atual a ser mostrado
        Screen currentScreen = game.getScreen();

        // troca para o novo screen
        if (screenEnum == ScreenEnum.COURT) {
            AbstractScreen newScreen = screenEnum.getScreen(params);
            game.setScreen(newScreen);
        } else {
            AbstractStage newStage = screenEnum.getStage(params);
            newStage.buildStage();
            game.setScreen(newStage);
        }

        // dispose do screen anterior
        if (currentScreen != null)
            currentScreen.dispose();
    }
}
