package com.gdx.tdl.util.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class OptionsTable {
    public static final int OFFE = 0;
    static final int DEFE = 1;

    OptionButton[] offensiveOptions, defensiveOptions, menuOptions, helpOptions;

    int currentOption;

    OptionsTable(World world) {
        offensiveOptions = new OptionButton[6];
        defensiveOptions = new OptionButton[0];
        menuOptions = new OptionButton[9];
        helpOptions = new OptionButton[1];

        currentOption = OFFE;
    }

    // TODO keep open the idea of creating menu and deffensive options with this

    public void offensiveOptionsDraw(World world) {
        offensiveOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5/6f, AssetLoader.menuOption);
        offensiveOptions[1] = new OptionButton(world, 0, Gdx.graphics.getHeight()*4/6f, AssetLoader.runOption, AssetLoader.runSelectedOption);
        offensiveOptions[2] = new OptionButton(world, 0, Gdx.graphics.getHeight()*3/6f, AssetLoader.dribleOption, AssetLoader.dribleSelectedOption);
        offensiveOptions[3] = new OptionButton(world, 0, Gdx.graphics.getHeight()*2/6f, AssetLoader.passOption, AssetLoader.passSelectedOption);
        offensiveOptions[4] = new OptionButton(world, 0, Gdx.graphics.getHeight()  /6f, AssetLoader.screenOption, AssetLoader.screenSelectedOption);
        offensiveOptions[5] = new OptionButton(world, 0, 0, AssetLoader.helpOption);
    }

    void defensiveOptionsDraw(World world) {
        // TODO
    }

    public void menuOptionsDraw(World world) {
        menuOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5/6f, AssetLoader.menuOption, "Menu", Gdx.graphics.getHeight()/300f);
        menuOptions[1] = new OptionButton(world, 0, 0, AssetLoader.helpOption, "Help", Gdx.graphics.getHeight()/300f);
        menuOptions[2] = new OptionButton(world, Gdx.graphics.getWidth()*3/10f, Gdx.graphics.getHeight()*5/6f, AssetLoader.play, "Play", Gdx.graphics.getHeight()/350f);
        menuOptions[3] = new OptionButton(world, Gdx.graphics.getWidth()*3/10f, Gdx.graphics.getHeight()*4/6f, AssetLoader.plus, "Frame", Gdx.graphics.getHeight()/350f);
        menuOptions[4] = new OptionButton(world, Gdx.graphics.getWidth()*3/10f, Gdx.graphics.getHeight()*3/6f, AssetLoader.reset, "Reset", Gdx.graphics.getHeight()/350f);
        menuOptions[5] = new OptionButton(world, Gdx.graphics.getWidth()*6/10f, Gdx.graphics.getHeight()*5/6f, AssetLoader.saveFile, "Save File", Gdx.graphics.getHeight()/450f);
        menuOptions[6] = new OptionButton(world, Gdx.graphics.getWidth()*6/10f, Gdx.graphics.getHeight()*4/6f, AssetLoader.savePDF, "Save PDF", Gdx.graphics.getHeight()/450f);
        menuOptions[7] = new OptionButton(world, Gdx.graphics.getWidth()*6/10f, Gdx.graphics.getHeight()*3/6f, AssetLoader.saveVideo, "Save Video", Gdx.graphics.getHeight()/450f);
        menuOptions[8] = new OptionButton(world, Gdx.graphics.getWidth()*6/10f, Gdx.graphics.getHeight()*2/6f, AssetLoader.notes, "Notes", Gdx.graphics.getHeight()/450f);
    }

    public void helpOptionsDraw(World world) {
        helpOptions[0]  = new OptionButton(world, 0, 0, AssetLoader.goback, AssetLoader.runSelectedOption, "Go Back", Gdx.graphics.getHeight()/400f);
    }

    public void update(OptionButton[] options) {
        AssetLoader.batch.begin();
        for (OptionButton btn : options)
            btn.update();
        AssetLoader.batch.end();
    }

    // getters
    public OptionButton[] getOffensiveOptions() {
        return offensiveOptions;
    }
    OptionButton[] getDefensiveOptions() {
        return defensiveOptions;
    }
    public OptionButton[] getMenuOptions() {
        return menuOptions;
    }
    public OptionButton[] getHelpOptions() {
        return helpOptions;
    }
    public int getCurrentOption() { return currentOption; }

    // setters
    void setCurrentOption(int currentOption) { this.currentOption = currentOption; }

}
