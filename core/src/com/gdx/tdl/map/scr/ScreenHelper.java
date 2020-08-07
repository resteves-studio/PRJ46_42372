package com.gdx.tdl.map.scr;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class ScreenHelper {
    private MenuScreen menuScreen;
    private HelpScreen helpScreen;
    private boolean menuSelected, helpSelected;

    ScreenHelper() {
        this.menuScreen = new MenuScreen();
        this.helpScreen = new HelpScreen();
        this.menuSelected = this.helpSelected = false;
    }

    void screenDraw() {
        if (isMenuSelected()) { setMenuSelected(this.menuScreen.menuDraw()); }
        if (isHelpSelected()) { setHelpSelected(this.helpScreen.helpDraw()); }
    }

    // ----- getters -----
    MenuScreen getMenuScreen() { return this.menuScreen; }
    HelpScreen getHelpScreen() { return this.helpScreen; }
    Stage getStage() { return isMenuSelected() ? this.menuScreen.getStage() : this.helpScreen.getStage(); }
    boolean isMenuSelected() { return this.menuSelected; }
    boolean isHelpSelected() { return this.helpSelected; }
    boolean isActivated() { return this.menuSelected || this.helpSelected; }

    // ----- setters -----
    void setMenuSelected(boolean menuSelected) { this.menuSelected = menuSelected; }
    void setHelpSelected(boolean helpSelected) { this.helpSelected = helpSelected; }
}
