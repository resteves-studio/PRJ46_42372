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
import com.gdx.tdl.util.sgn.ButtonUtil;
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
        Table createUserTable = new Table(AssetLoader.skin);
        createUserTable.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/12f);
        createUserTable.setSize(Gdx.graphics.getWidth()*4/11f, Gdx.graphics.getHeight()/1.25f);
        addActor(createUserTable);

        // field do email
        final TextField mailTF = new TextField("", AssetLoader.skin, "default");
        mailTF.setMessageText("e-mail");
        createUserTable.add(mailTF).expand().fill().padBottom(Gdx.graphics.getHeight()/35f);
        createUserTable.row();

        // field da password
        final TextField pswdTF = new TextField("", AssetLoader.skin, "default");
        pswdTF.setMessageText("palavra-passe");
        pswdTF.setPasswordMode(true);
        pswdTF.setPasswordCharacter('*');
        createUserTable.add(pswdTF).expand().fill().padBottom(Gdx.graphics.getHeight()/11.5f);
        createUserTable.row();

        // botao de upload
        TextButton signUpTB = new TextButton("Registar", AssetLoader.skin, "default");
        createUserTable.add(signUpTB).expand().fill().padBottom(Gdx.graphics.getHeight()/5f);
        createUserTable.row();

        // botao de voltar atras
        TextButton goBackTB = new TextButton("Voltar", AssetLoader.skin, "default");
        createUserTable.add(goBackTB);

        // adicao de listeners aos botoes
        goBackTB.addListener(ButtonUtil.createListener(ScreenEnum.LOGIN));

        signUpTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mailTF.getText().isEmpty() || pswdTF.getText().isEmpty()) {
                    Dialog dialog = new Dialog("", AssetLoader.skin, "default");
                    dialog.text("\nUm dos campos encontra-se em branco.\nPreencha ambos para criar a conta.\n")
                            .button("Percebi", true).show(CreateUserScreen.this);
                } else if (!validate(mailTF.getText())) {
                    Dialog dialog = new Dialog("", AssetLoader.skin, "default");
                    dialog.text("\nO e-mail introduzido não é valido\n")
                            .button("Percebi", true).show(CreateUserScreen.this);
                } else {
                    GdxFIRAuth.inst()
                            .createUserWithEmailAndPassword(mailTF.getText(), pswdTF.getText().toCharArray())
                            .then(gdxFirebaseUser -> {
                                Dialog dialog = new Dialog("", AssetLoader.skin, "default");
                                dialog.text("\nUser criado! Faça login para entrar.\n")
                                        .button("Obrigado(a)", true).show(CreateUserScreen.this);
                                AssetLoader.changeScreen = true;
                            })
                            .fail((s, throwable) -> {
                                Dialog dialog = new Dialog("", AssetLoader.skin, "default");
                                dialog.text("\nUser já existe...\n").button("Percebi", true).show(CreateUserScreen.this);
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
