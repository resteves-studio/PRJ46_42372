package com.gdx.tdl.map.tct;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.GdxFIRStorage;

public class SaveLoad {
    Json json = new Json();
    FileHandle fileHandle;
    Tactic tactic;

    public SaveLoad(Tactic tactic) {
        if (tactic != null) {
            setTactic(tactic);
            setFileHandle(tactic.getName() + ".json");
        }

        GdxFIRAuth.instance()
                .signInWithEmailAndPassword("resteves.studio@gmail.com", "basket".toCharArray())
                .then(gdxFirebaseUser -> Gdx.app.log("LOGGED IN TO", "resteves.studio@gmail.com"))
                .fail((s, throwable) -> Gdx.app.log("FAILED SIGNIN", s));
    }

    public void saveLocalData() {
        if (tactic != null) {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(tactic)), false);
            Gdx.app.log("SAVED TO", "Local");
        } else {
            Gdx.app.log("NOT SAVED", "Local");
            // TODO dialog a informar do sucedido
        }
    }

    public void saveCloudData() {
        GdxFIRStorage.instance()
                .upload(fileHandle.path(), fileHandle)
                .after(GdxFIRAuth.instance().getCurrentUserPromise())
                .then(fileMetadata -> { Gdx.app.log("SAVED TO", "Cloud"); })
                .fail((s, throwable) -> {
                    Gdx.app.log("NOT SAVED TO", "Cloud");
                    // TODO dialog a informar do sucedido
                });
    }

    public void loadLocalData() {
        tactic = json.fromJson(Tactic.class, Base64Coder.decodeString(fileHandle.readString()));
        Gdx.app.log("LOADED FROM", "Local");
    }

    public void loadCloudData() {
        GdxFIRStorage.instance()
                .download(fileHandle.path(), fileHandle)
                .after(GdxFIRAuth.instance().getCurrentUserPromise())
                .then(fileMetadata -> {
                    Gdx.app.log("LOADED FROM", "Cloud");
                    // TODO guardar a tatica
                })

                .fail((s, throwable) -> {
                    Gdx.app.log("NOT SAVED TO", "Cloud");
                    // TODO dialog a informar do sucedido
                });
    }

    // ----- getters -----
    public Tactic getTactic() { return this.tactic; }

    // ----- setters -----
    public void setTactic(Tactic tactic) { this.tactic = tactic; }
    public void setTacticName(String name) { this.tactic.setName(name); }
    public void setFileHandle(String path) { this.fileHandle = Gdx.files.local(path); }

}
