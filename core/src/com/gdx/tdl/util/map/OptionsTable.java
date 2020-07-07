package com.gdx.tdl.util.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class OptionsTable {
    public static final int OFFE = 0;
    public static final int DEFE = 1;
    public static final int TACT = 2;
    public static final int SAVE = 3;

    private OptionButton[] offensiveOptions, defensiveOptions, tacticOptions, saveOptions, menuOptions, helpOptions;

    private int currentOption;
    private World world;

    OptionsTable(World world) {
        this.world = world;

        this.offensiveOptions = new OptionButton[6];
        this.defensiveOptions = new OptionButton[4];
        this.tacticOptions = new OptionButton[5];
        this.saveOptions = new OptionButton[5];
        this.menuOptions = new OptionButton[2];
        this.helpOptions = new OptionButton[1];

        this.currentOption = OFFE;
    }

    public void offensiveOptionsDraw() {
        offensiveOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5/6f, AssetLoader.menuOption);
        offensiveOptions[1] = new OptionButton(world, 0, Gdx.graphics.getHeight()*4/6f, AssetLoader.plusO, AssetLoader.plusSelected);
        offensiveOptions[2] = new OptionButton(world, 0, Gdx.graphics.getHeight()*3/6f, AssetLoader.runOption, AssetLoader.runSelectedOption);
        offensiveOptions[3] = new OptionButton(world, 0, Gdx.graphics.getHeight()*2/6f, AssetLoader.passOption, AssetLoader.passSelectedOption);
        offensiveOptions[4] = new OptionButton(world, 0, Gdx.graphics.getHeight()  /6f, AssetLoader.screenOption, AssetLoader.screenSelectedOption);
        offensiveOptions[5] = new OptionButton(world, 0, 0, AssetLoader.helpOption);
    }

    public void defensiveOptionsDraw() {
        defensiveOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5/6f, AssetLoader.menuOption);
        defensiveOptions[1] = new OptionButton(world, 0, Gdx.graphics.getHeight()*4/6f, AssetLoader.manToManOption, AssetLoader.manToManSelectedOption);
        defensiveOptions[2] = new OptionButton(world, 0, Gdx.graphics.getHeight()*3/6f, AssetLoader.zone23Option, AssetLoader.zone23SelectedOption);
        defensiveOptions[3] = new OptionButton(world, 0, 0, AssetLoader.helpOption);
    }

    public void tacticCreationOptionsDraw() {
        tacticOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5/6f, AssetLoader.menuOption);
        tacticOptions[1] = new OptionButton(world, 0, Gdx.graphics.getHeight()*4/6f, AssetLoader.play, AssetLoader.playSelected);
        tacticOptions[2] = new OptionButton(world, 0, Gdx.graphics.getHeight()*3/6f, AssetLoader.plus, AssetLoader.plusSelected);
        tacticOptions[3] = new OptionButton(world, 0, Gdx.graphics.getHeight()*2/6f, AssetLoader.reset, AssetLoader.resetSelected);
        tacticOptions[4] = new OptionButton(world, 0, 0, AssetLoader.helpOption);
    }

    public void saveOptionsDraw() {
        saveOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5f/6f, AssetLoader.menuOption);
        saveOptions[1] = new OptionButton(world, 0, Gdx.graphics.getHeight()*4/6f, AssetLoader.saveFile);
        saveOptions[2] = new OptionButton(world, 0, Gdx.graphics.getHeight()*3/6f, AssetLoader.loadFile);
        saveOptions[3] = new OptionButton(world, 0, Gdx.graphics.getHeight()*2/6f, AssetLoader.notes);
        saveOptions[4] = new OptionButton(world, 0, 0, AssetLoader.helpOption);
        //saveOptions[2] = new OptionButton(world, 0, Gdx.graphics.getHeight()*3/6f, AssetLoader.savePDF);
        //saveOptions[3] = new OptionButton(world, 0, Gdx.graphics.getHeight()*2/6f, AssetLoader.saveVideo);
    }

    public void menuOptionsDraw(World world) {
        menuOptions[0] = new OptionButton(world, 0, Gdx.graphics.getHeight()*5/6f, AssetLoader.menuOption, "Menu", Gdx.graphics.getHeight()/300f);
        menuOptions[1] = new OptionButton(world, 0, 0, AssetLoader.helpOption, "Help", Gdx.graphics.getHeight()/300f);
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
    public OptionButton[] getOffensiveOptions() { return offensiveOptions; }
    public OptionButton[] getDefensiveOptions() { return defensiveOptions; }
    public OptionButton[] getTacticCreationOptions() { return tacticOptions; }
    public OptionButton[] getSaveOptions() { return saveOptions; }
    public OptionButton[] getMenuOptions() { return menuOptions; }
    public OptionButton[] getHelpOptions() { return helpOptions; }
    public int getCurrentOption() { return currentOption; }

    // setters
    public void setCurrentOption(int currentOption) { this.currentOption = currentOption; }

}
