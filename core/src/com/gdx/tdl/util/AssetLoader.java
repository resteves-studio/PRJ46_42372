package com.gdx.tdl.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetLoader {
    public static SpriteBatch batch;
    public static Texture I_login, I_court;
    public static Skin skin, skinXP;
    public static Texture red, redII;
    public static Texture blue, blueII;
    public static Texture blueSelected, blueIISelected;
    public static Texture ball;
    public static Texture empty;
    public static Texture check;
    public static Texture play, plus, reset;
    public static Texture playSelected, plusSelected, resetSelected;
    public static Texture saveFile, savePDF, saveVideo, notes;
    public static Texture menuOption, helpOption;
    public static Texture runOption, dribleOption, passOption, screenOption;
    public static Texture runSelectedOption, dribleSelectedOption, passSelectedOption, screenSelectedOption;
    public static Texture manToManOption, zone23Option;
    public static Texture manToManSelectedOption, zone23SelectedOption;
    public static Texture goback;
    //public static Texture one, two, three, four, five;
    public static BitmapFont font;
    public static boolean changeScreen;

    public static void load() {
        batch = new SpriteBatch();

        I_login = new Texture("login.png");
        I_court = new Texture("court.png");
        skin = new Skin(Gdx.files.internal("Particle Park UI.json"));
        skin.getFont("font").getData().setScale(Gdx.graphics.getHeight()/500f);
        skinXP = new Skin(Gdx.files.internal("expee-ui.json"));
        skinXP.getFont("font").getData().setScale(Gdx.graphics.getHeight()/400f);
        red = new Texture("red.png");
        redII = new Texture("redII.png");
        blue = new Texture("blue.png");
        blueII = new Texture("blueII.png");
        blueSelected = new Texture("blueSelected.png");
        blueIISelected = new Texture("blueIISelected.png");
        ball = new Texture("ball.png");
        empty = new Texture("empty.png");
        check = new Texture("check.png");
        play = new Texture("play.png");
        plus = new Texture("plus.png");
        reset = new Texture("reset.png");
        playSelected = new Texture("playSelected.png");
        plusSelected = new Texture("plusSelected.png");
        resetSelected = new Texture("resetSelected.png");
        saveFile = new Texture("saveFile.png");
        savePDF = new Texture("savePDF.png");
        saveVideo = new Texture("saveVideo.png");
        notes = new Texture("notes.png");
        menuOption = new Texture("menu.png");
        helpOption = new Texture("help.png");
        runOption = new Texture("run.png");
        dribleOption = new Texture("drible.png");
        passOption = new Texture("pass.png");
        screenOption = new Texture("screen.png");
        runSelectedOption = new Texture("runSelected.png");
        dribleSelectedOption = new Texture("dribleSelected.png");
        passSelectedOption = new Texture("passSelected.png");
        screenSelectedOption = new Texture("screenSelected.png");
        manToManOption = new Texture("mantoman.png");
        zone23Option = new Texture("zona23.png");
        manToManSelectedOption = new Texture("mantomanSelected.png");
        zone23SelectedOption = new Texture("zona23Selected.png");
        goback = new Texture("goback.png");
        /*one = new Texture("1.png");
        two = new Texture("2.png");
        three = new Texture("3.png");
        four = new Texture("4.png");
        five = new Texture("5.png");*/

        font = new BitmapFont(Gdx.files.internal("normal.fnt"), false);
        font.getData().setScale(Gdx.graphics.getWidth()/800f);

        changeScreen = false;
    }

    public static void dispose() {
        batch.dispose();
        I_login.dispose();
        I_court.dispose();
        skin.dispose();
        skinXP.dispose();
        red.dispose();
        redII.dispose();
        blueSelected.dispose();
        blueIISelected.dispose();
        blue.dispose();
        blueII.dispose();
        ball.dispose();
        empty.dispose();
        check.dispose();
        play.dispose();
        plus.dispose();
        reset.dispose();
        playSelected.dispose();
        plusSelected.dispose();
        resetSelected.dispose();
        saveFile.dispose();
        savePDF.dispose();
        saveVideo.dispose();
        notes.dispose();
        menuOption.dispose();
        helpOption.dispose();
        runOption.dispose();
        dribleOption.dispose();
        passOption.dispose();
        screenOption.dispose();
        runSelectedOption.dispose();
        dribleSelectedOption.dispose();
        passSelectedOption.dispose();
        screenSelectedOption.dispose();
        manToManOption.dispose();
        zone23Option.dispose();
        manToManSelectedOption.dispose();
        zone23SelectedOption.dispose();
        goback.dispose();
        /*one.dispose();
        two.dispose();
        three.dispose();
        four.dispose();
        five.dispose();*/
        font.dispose();
    }

}
