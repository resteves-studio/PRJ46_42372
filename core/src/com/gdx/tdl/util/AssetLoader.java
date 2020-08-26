package com.gdx.tdl.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetLoader {
    public static Texture lightSelected, lightIISelected, darkSelected, darkIISelected, redSelected, redIISelected, blueSelected, blueIISelected;
    public static Texture play, plus, plusO, reset, runExtra, passExtra, plusOExtra, saveFile, loadFile, notes, menuOption, helpOption;
    public static Texture orange, orangeII, yellow, yellowII, green, greenII, light, lightII, dark, darkII, red, redII, blue, blueII;
    public static Texture whiteOne, whiteTwo, whiteThree, whiteFour, whiteFive, darkOne, darkTwo, darkThree, darkFour, darkFive;
    public static Texture orangeSelected, orangeIISelected, yellowSelected, yellowIISelected, greenSelected, greenIISelected;
    public static Texture playSelected, plusSelected, resetSelected, manToManSelectedOption, zone23SelectedOption;
    public static Texture court_wood, court_dark, court_light, court_green, court_blue, court_red, court_yellow;
    public static Texture ball_orange, ball_dark, ball_light, ball_green, ball_blue, ball_red, ball_yellow;
    public static Texture runOption, dribleOption, passOption, manToManOption, zone23Option, goback;
    public static Texture runSelectedOption, dribleSelectedOption, passSelectedOption;
    public static Texture helpAtaque, helpDefesa, helpTatica, helpFicheiros;
    public static Texture I_login, icon, empty, check;

    public static boolean changeScreen, loaded;

    public static SpriteBatch batch;
    public static BitmapFont font;
    public static Skin skin, skinXP;

    public static void load() {
        batch = new SpriteBatch();
        icon = new Texture("icon.png");
        empty = new Texture("empty.png");
        check = new Texture("check.png");
        I_login = new Texture("login.png");

        optionsLoad();
        optionsSelectedLoad();
        playersLoad();
        playersSelectedLoad();
        ballLoad();
        courtLoad();
        numbersLoad();
        skinsFontsLoad();
        helpMenuLoad();

        changeScreen = loaded = false;
    }

    public static void dispose() {
        batch.dispose();
        icon.dispose();
        empty.dispose();
        check.dispose();
        I_login.dispose();

        optionsDispose();
        optionsSelectedDispose();
        playersDispose();
        playersSelectedDispose();
        ballDispose();
        courtDispose();
        numbersDispose();
        skinsFontsDispose();
        helpMenuDispose();
    }

    /** * * * * * * * * * * * * **
     **     Load methods     **
     ** * * * * * * * * * * * * **/
    private static void optionsLoad() {
        play = new Texture("play.png");
        plus = new Texture("plus.png");
        plusO = new Texture("plusO.png");
        reset = new Texture("reset.png");
        runExtra = new Texture("runExtra.png");
        passExtra = new Texture("passExtra.png");
        plusOExtra = new Texture("plusOExtra.png");
        saveFile = new Texture("saveFile.png");
        loadFile = new Texture("loadFile.png");
        notes = new Texture("notes.png");
        menuOption = new Texture("menu.png");
        helpOption = new Texture("help.png");
        runOption = new Texture("run.png");
        dribleOption = new Texture("drible.png");
        passOption = new Texture("pass.png");
        manToManOption = new Texture("mantoman.png");
        zone23Option = new Texture("zona23.png");
        goback = new Texture("goback.png");
    }

    private static void optionsSelectedLoad() {
        playSelected = new Texture("playSelected.png");
        plusSelected = new Texture("plusSelected.png");
        resetSelected = new Texture("resetSelected.png");
        runSelectedOption = new Texture("runSelected.png");
        dribleSelectedOption = new Texture("dribleSelected.png");
        passSelectedOption = new Texture("passSelected.png");
        manToManSelectedOption = new Texture("mantomanSelected.png");
        zone23SelectedOption = new Texture("zona23Selected.png");
    }

    private static void playersLoad() {
        orange = new Texture("orange.png");
        orangeII = new Texture("orangeII.png");
        yellow = new Texture("yellow.png");
        yellowII = new Texture("yellowII.png");
        green = new Texture("green.png");
        greenII = new Texture("greenII.png");
        dark = new Texture("dark.png");
        darkII = new Texture("darkII.png");
        light = new Texture("light.png");
        lightII = new Texture("lightII.png");
        red = new Texture("red.png");
        redII = new Texture("redII.png");
        blue = new Texture("blue.png");
        blueII = new Texture("blueII.png");
    }

    private static void playersSelectedLoad() {
        orangeSelected = new Texture("orangeSelected.png");
        orangeIISelected = new Texture("orangeIISelected.png");
        yellowSelected = new Texture("yellowSelected.png");
        yellowIISelected = new Texture("yellowIISelected.png");
        greenSelected = new Texture("greenSelected.png");
        greenIISelected = new Texture("greenIISelected.png");
        darkSelected = new Texture("darkSelected.png");
        darkIISelected = new Texture("darkIISelected.png");
        lightSelected = new Texture("lightSelected.png");
        lightIISelected = new Texture("lightIISelected.png");
        redSelected = new Texture("redSelected.png");
        redIISelected = new Texture("redIISelected.png");
        blueSelected = new Texture("blueSelected.png");
        blueIISelected = new Texture("blueIISelected.png");
    }

    private static void ballLoad() {
        ball_orange = new Texture("ball_orange.png");
        ball_dark = new Texture("ball_dark.png");
        ball_light = new Texture("ball_light.png");
        ball_green = new Texture("ball_green.png");
        ball_blue = new Texture("ball_blue.png");
        ball_red = new Texture("ball_red.png");
        ball_yellow = new Texture("ball_yellow.png");
    }

    private static void courtLoad() {
        court_wood = new Texture("court_wood.png");
        court_dark = new Texture("court_dark.png");
        court_light = new Texture("court_light.png");
        court_green = new Texture("court_green.png");
        court_blue = new Texture("court_blue.png");
        court_red = new Texture("court_red.png");
        court_yellow = new Texture("court_yellow.png");
    }

    private static void numbersLoad() {
        whiteOne = new Texture("white1.png");
        whiteTwo = new Texture("white2.png");
        whiteThree = new Texture("white3.png");
        whiteFour = new Texture("white4.png");
        whiteFive = new Texture("white5.png");
        darkOne = new Texture("dark1.png");
        darkTwo = new Texture("dark2.png");
        darkThree = new Texture("dark3.png");
        darkFour = new Texture("dark4.png");
        darkFive = new Texture("dark5.png");
    }

    private static void skinsFontsLoad() {
        // TODO more


        font = new BitmapFont(Gdx.files.internal("normal.fnt"), false);
        font.getData().setScale(Gdx.graphics.getWidth()/800f);
        
        skin = new Skin(Gdx.files.internal("Particle Park UI.json"));
        skin.getFont("font").getData().setScale(Gdx.graphics.getHeight()/500f);

        skinXP = new Skin(Gdx.files.internal("expee-ui.json"));
        skinXP.getFont("font").getData().setScale(Gdx.graphics.getHeight()/400f);
        skinXP.getFont("title").getData().setScale(Gdx.graphics.getHeight()/200f);
    }

    private static void helpMenuLoad() {
        helpAtaque = new Texture("helpAtaque.png");
        helpDefesa = new Texture("helpDefesa.png");
        helpTatica = new Texture("helpTatica.PNG");
        helpFicheiros = new Texture("helpFicheiros.PNG");
    }


    /** * * * * * * * * * * * * **
     **     Dispose methods     **
     ** * * * * * * * * * * * * **/
    private static void optionsDispose() {
        play.dispose();
        plus.dispose();
        plusO.dispose();
        reset.dispose();
        runExtra.dispose();
        passExtra.dispose();
        plusOExtra.dispose();
        saveFile.dispose();
        loadFile.dispose();
        notes.dispose();
        menuOption.dispose();
        helpOption.dispose();
        runOption.dispose();
        dribleOption.dispose();
        passOption.dispose();
        manToManOption.dispose();
        zone23Option.dispose();
        goback.dispose();
    }

    private static void optionsSelectedDispose() {
        playSelected.dispose();
        plusSelected.dispose();
        resetSelected.dispose();
        runSelectedOption.dispose();
        dribleSelectedOption.dispose();
        passSelectedOption.dispose();
        manToManSelectedOption.dispose();
        zone23SelectedOption.dispose();
    }

    private static void playersDispose() {
        orange.dispose();
        orangeII.dispose();
        yellow.dispose();
        yellowII.dispose();
        green.dispose();
        greenII.dispose();
        dark.dispose();
        darkII.dispose();
        light.dispose();
        lightII.dispose();
        red.dispose();
        redII.dispose();
        blue.dispose();
        blueII.dispose();
    }

    private static void playersSelectedDispose() {
        orangeSelected.dispose();
        orangeIISelected.dispose();
        yellowSelected.dispose();
        yellowIISelected.dispose();
        greenSelected.dispose();
        greenIISelected.dispose();
        darkSelected.dispose();
        darkIISelected.dispose();
        lightSelected.dispose();
        lightIISelected.dispose();
        redSelected.dispose();
        redIISelected.dispose();
        blueSelected.dispose();
        blueIISelected.dispose();
    }

    private static void ballDispose() {
        ball_orange.dispose();
        ball_dark.dispose();
        ball_light.dispose();
        ball_green.dispose();
        ball_blue.dispose();
        ball_red.dispose();
        ball_yellow.dispose();
    }

    private static void courtDispose() {
        court_wood.dispose();
        court_dark.dispose();
        court_light.dispose();
        court_green.dispose();
        court_blue.dispose();
        court_red.dispose();
        court_yellow.dispose();
    }

    private static void numbersDispose() {
        whiteOne.dispose();
        whiteTwo.dispose();
        whiteThree.dispose();
        whiteFour.dispose();
        whiteFive.dispose();
        darkOne.dispose();
        darkTwo.dispose();
        darkThree.dispose();
        darkFour.dispose();
        darkFive.dispose();
    }

    private static void skinsFontsDispose() {
        // TODO more
        
        skin.dispose();
        skinXP.dispose();
        font.dispose();
    }

    private static void helpMenuDispose() {
        helpAtaque.dispose();
        helpDefesa.dispose();
        helpTatica.dispose();
        helpFicheiros.dispose();
    }

}
