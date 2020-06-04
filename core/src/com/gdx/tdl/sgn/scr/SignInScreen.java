package com.gdx.tdl.sgn.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.sgn.AbstractStage;
import com.gdx.tdl.util.sgn.ButtonUtil;
import com.gdx.tdl.util.sgn.StageManager;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.functional.BiConsumer;
import pl.mk5.gdx.fireapp.functional.Consumer;

public class SignInScreen extends AbstractStage {
    private String quote;

    public SignInScreen() {
        super();
        this.quote = "The key is not the 'will to win'...everybody has that. \\n\" +\n" +
                     "\" It is the will to prepare to win that is important. \\n\" +\n" +
                     "\"   - Bob Knight";
    }

    @Override
    public void buildStage() {
        // imagem de background
        Image bg = new Image(AssetLoader.I_login);
        bg.setHeight(Gdx.graphics.getHeight());
        bg.setWidth(Gdx.graphics.getWidth());
        bg.setPosition(0, 0);
        addActor(bg);

        // coach quote
        /*Label quote = new Label("The key is not the 'will to win'...everybody has that. \n" +
                                     "It is the will to prepare to win that is important. \n" +
                                     "   - Bob Knight", AssetLoader.skin, "white");
        Container quoteContainer = new Container(quote);
        quoteContainer.setPosition(Gdx.graphics.getWidth()/3.25f,  Gdx.graphics.getHeight()*1.5f/6f);
        addActor(quoteContainer);*/

        // login table
        Table loginTable = new Table(AssetLoader.skin);
        loginTable.setPosition(Gdx.graphics.getWidth()*4/7f, Gdx.graphics.getHeight()/8f);
        loginTable.setSize(Gdx.graphics.getWidth()/2.75f, Gdx.graphics.getHeight()/1.5f);
        addActor(loginTable);

        // field do email
        final TextField mailTF = new TextField("", AssetLoader.skin, "default");
        mailTF.setMessageText("e-mail");
        loginTable.add(mailTF).expand().fill().padBottom(Gdx.graphics.getHeight()/50f);
        loginTable.row();

        // field da password
        final TextField pswdTF = new TextField("", AssetLoader.skin, "default");
        pswdTF.setMessageText("password");
        pswdTF.setPasswordMode(true);
        pswdTF.setPasswordCharacter('*');
        loginTable.add(pswdTF).expand().fill().padBottom(Gdx.graphics.getHeight()/17.5f);
        loginTable.row();

        // botao de sign up
        TextButton signUpTB = new TextButton("Sign Up", AssetLoader.skin, "default");
        loginTable.add(signUpTB).expand().fill().padBottom(Gdx.graphics.getHeight()/7.5f);
        loginTable.row();

        // botao para criar conta
        TextButton createAccountTB = new TextButton("Create Account", AssetLoader.skin, "default");
        loginTable.add(createAccountTB).expand().padBottom(Gdx.graphics.getHeight()/35f);
        loginTable.row();


        // adicao de listeners aos botoes
        createAccountTB.addListener(ButtonUtil.createListener(ScreenEnum.CREATE));

        signUpTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mailTF.getText().isEmpty() || pswdTF.getText().isEmpty()) {
                    Dialog dialog = new Dialog("", AssetLoader.skinXP, "dialog");
                    dialog.text("\nUm dos campos encontra-se em branco.\nPreencha ambos para fazer Login.\n")
                            .button("Okay", true).show(SignInScreen.this);
                } else {
                    GdxFIRAuth.inst()
                            .signInWithEmailAndPassword(mailTF.getText(), pswdTF.getText().toCharArray())
                            .then(gdxFirebaseUser -> AssetLoader.changeScreen = true)
                            .fail((s, throwable) -> {
                                Dialog dialog = new Dialog("", AssetLoader.skinXP, "dialog");
                                dialog.text("\nUser não existe ou colocou credênciais erradas.\nTente novamente.\n")
                                        .button("Okay", true).show(SignInScreen.this);
                            });
                }

                if (AssetLoader.changeScreen) {
                    AssetLoader.changeScreen = false;
                    StageManager.getInstance().showScreen(ScreenEnum.COURT);
                }
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
