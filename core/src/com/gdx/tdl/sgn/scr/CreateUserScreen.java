package com.gdx.tdl.sgn.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.sgn.AbstractStage;
import com.gdx.tdl.util.sgn.StageManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.functional.BiConsumer;
import pl.mk5.gdx.fireapp.functional.Consumer;

public class CreateUserScreen extends AbstractStage {

    public CreateUserScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // table
        Table loginTable = new Table(AssetLoader.skin);
        loginTable.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/2f);
        loginTable.setSize(Gdx.graphics.getWidth()*4/11f, Gdx.graphics.getHeight()/2f);
        addActor(loginTable);

        // field do email
        final TextField mailTF = new TextField("", AssetLoader.skin, "login");
        mailTF.setMessageText("e-mail");
        loginTable.add(mailTF).expand().fill();
        loginTable.row();

        // field da password
        final TextField pswdTF = new TextField("", AssetLoader.skin, "password");
        pswdTF.setMessageText("password");
        pswdTF.setPasswordMode(true);
        pswdTF.setPasswordCharacter('•');
        loginTable.add(pswdTF).expand().fill();
        loginTable.row();

        // botao de upload
        TextButton signUpTB = new TextButton("Create Account", AssetLoader.skin, "menu");
        loginTable.add(signUpTB).expand().fill();

        // adicao de listeners aos botoes
        signUpTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mailTF.getText().isEmpty() || pswdTF.getText().isEmpty()) {
                    Dialog dialog = new Dialog("Erro", AssetLoader.skin, "dialog");
                    dialog.text("Um dos campos encontra-se em branco.\nPreencha ambos para criar conta.")
                            .button("Percebi", true).show(CreateUserScreen.this);
                } else if (!validate(mailTF.getText())) {
                    Dialog dialog = new Dialog("Erro", AssetLoader.skin, "dialog");
                    dialog.text("O e-mail introduzido nao e valido.")
                            .button("Percebi", true).show(CreateUserScreen.this);
                } else {
                    GdxFIRAuth.inst()
                            .createUserWithEmailAndPassword(mailTF.getText(), pswdTF.getText().toCharArray())
                            .then(new Consumer<GdxFirebaseUser>() {
                                @Override
                                public void accept(GdxFirebaseUser gdxFirebaseUser) {
                                    Dialog dialog = new Dialog("", AssetLoader.skin, "dialog");
                                    dialog.text("User criado! Faca login para entrar")
                                            .button("Obrigado(a)", true).show(CreateUserScreen.this);
                                    AssetLoader.changeScreen = true;
                                }
                            })
                            .fail(new BiConsumer<String, Throwable>() {
                                @Override
                                public void accept(String s, Throwable throwable) {
                                    Dialog dialog = new Dialog("Erro", AssetLoader.skin, "dialog");
                                    dialog.text("User ja existe...").button("Percebi", true).show(CreateUserScreen.this);
                                }
                            });
                }
                if (AssetLoader.changeScreen) {
                    AssetLoader.changeScreen = false;
                    StageManager.getInstance().showScreen(ScreenEnum.LOGIN);
                }
            }
        });
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
