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

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.functional.BiConsumer;
import pl.mk5.gdx.fireapp.functional.Consumer;

public class UpdatePasswordScreen extends AbstractStage {

    public UpdatePasswordScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // table
        Table updPwdTable = new Table(AssetLoader.skin);
        updPwdTable.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/12f);
        updPwdTable.setSize(Gdx.graphics.getWidth()*4/11f, Gdx.graphics.getHeight()/1.25f);
        addActor(updPwdTable);

        // field da password antiga
        final TextField oldPwdTF = new TextField("", AssetLoader.skin, "default");
        oldPwdTF.setMessageText("old password");
        oldPwdTF.setPasswordMode(true);
        oldPwdTF.setPasswordCharacter('�');
        updPwdTable.add(oldPwdTF).expand().fill().padBottom(Gdx.graphics.getHeight()/35f);
        updPwdTable.row();

        // field da password nova
        final TextField newPwdTF = new TextField("", AssetLoader.skin, "default");
        newPwdTF.setMessageText("new password");
        newPwdTF.setPasswordMode(true);
        newPwdTF.setPasswordCharacter('�');
        updPwdTable.add(newPwdTF).expand().fill().padBottom(Gdx.graphics.getHeight()/11.5f);
        updPwdTable.row();

        // botao de upload
        TextButton updPwdTB = new TextButton("Update Password", AssetLoader.skin, "default");
        updPwdTable.add(updPwdTB).expand().fill().padBottom(Gdx.graphics.getHeight()/5f);
        updPwdTable.row();

        // botao de voltar atras
        TextButton goBackTB = new TextButton("Go Back", AssetLoader.skin, "default");
        updPwdTable.add(goBackTB);

        // adicao de listeners aos botoes
        goBackTB.addListener(ButtonUtil.createListener(ScreenEnum.LOGIN));

        updPwdTB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(AssetLoader.changeScreen + "WTF");
                if (oldPwdTF.getText().isEmpty() || newPwdTF.getText().isEmpty()) {
                    Dialog dialog = new Dialog("Erro", AssetLoader.skin, "dialog");
                    dialog.text("Um dos campos encontra-se em branco.\nPreencha ambos para atualizar password.")
                            .button("Percebi", true).show(UpdatePasswordScreen.this);
                } else {
                    GdxFIRAuth.inst()
                            .signInWithEmailAndPassword(GdxFIRAuth.instance().getCurrentUser().getUserInfo().getEmail(),
                                    oldPwdTF.getText().toCharArray())
                            .then(new Consumer<GdxFirebaseUser>() {
                                @Override
                                public void accept(GdxFirebaseUser gdxFirebaseUser) {
                                    GdxFIRAuth.instance().getCurrentUser().updatePassword(newPwdTF.getText().toCharArray());
                                    Dialog dialog = new Dialog("", AssetLoader.skin, "dialog");
                                    dialog.text("Password alterada!")
                                            .button("Obrigado(a)", true).show(UpdatePasswordScreen.this);
                                    //AssetLoader.changeScreen = true;
                                }
                            })
                            .fail(new BiConsumer<String, Throwable>() {
                                @Override
                                public void accept(String s, Throwable throwable) {
                                    Dialog dialog = new Dialog("Erro", AssetLoader.skin, "dialog");
                                    dialog.text("Password antiga errada. Tente novamente.")
                                            .button("Percebi", true).show(UpdatePasswordScreen.this);
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

    @Override
    public void dispose() {
        super.dispose();
    }
}
