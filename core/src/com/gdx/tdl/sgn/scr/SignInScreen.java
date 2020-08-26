package com.gdx.tdl.sgn.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

public class SignInScreen extends AbstractStage {

    public SignInScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // imagem de background
        Image bg = new Image(AssetLoader.I_login);
        bg.setHeight(Gdx.graphics.getHeight());
        bg.setWidth(Gdx.graphics.getWidth());
        bg.setPosition(0, 0);
        addActor(bg);

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
        pswdTF.setMessageText("palavra-passe");
        pswdTF.setPasswordMode(true);
        pswdTF.setPasswordCharacter('*');
        loginTable.add(pswdTF).expand().fill().padBottom(Gdx.graphics.getHeight()/17.5f);
        loginTable.row();

        // botao de sign up
        TextButton signUpTB = new TextButton("Iniciar Sessão", AssetLoader.skin, "default");
        loginTable.add(signUpTB).expand().fill().padBottom(Gdx.graphics.getHeight()/7.5f);
        loginTable.row();

        // botao para criar conta
        TextButton createAccountTB = new TextButton("Criar Conta", AssetLoader.skin, "default");
        loginTable.add(createAccountTB).expand().padBottom(Gdx.graphics.getHeight()/35f);
        loginTable.row();


        // adicao de listeners aos botoes
        createAccountTB.addListener(ButtonUtil.createListener(ScreenEnum.CREATE));

        signUpTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mailTF.getText().isEmpty() || pswdTF.getText().isEmpty()) {
                    Dialog dialog = new Dialog("", AssetLoader.skin, "default");
                    dialog.text("\nUm dos campos encontra-se em branco.\nPreencha ambos para fazer Login.\n")
                            .button("Okay", true).show(SignInScreen.this);
                } else {
                    GdxFIRAuth.inst()
                            .signInWithEmailAndPassword(mailTF.getText(), pswdTF.getText().toCharArray())
                            .then(gdxFirebaseUser -> AssetLoader.changeScreen = true)
                            .fail((s, throwable) -> {
                                Dialog dialog = new Dialog("", AssetLoader.skin, "default");
                                dialog.text("\nUser não existe ou colocou credenciais erradas.\nTente novamente.\n")
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
