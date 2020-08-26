package com.gdx.tdl.util.map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.sgn.AbstractStage;

public class ScreenManager {

    // singleton instance
    private static ScreenManager instance;

    // referencia a Game
    private Game game;

    // construtor
    private ScreenManager() {
        super();
    }

    // retorna a instancia singleton
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // inicializa a game class
    public void initialize(Game game) {
        this.game = game;
    }

    // mostra o tipo de screen pretendido (Court, Menu, Login, Help...)
    void showScreen(ScreenEnum screenEnum, Object... params) {
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
