package com.gdx.tdl.map.tct;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class SaveLoad {
    Json json = new Json();
    FileHandle fileHandle;
    Tactic tactic;

    public SaveLoad(Tactic tactic) {
        if (tactic != null) {
            setTactic(tactic);
            setFileHandle(Gdx.files.getLocalStoragePath() + tactic.getName() + ".json");
        }
    }

    public void saveData() {
        if (tactic != null) {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(tactic)), false);
        }
    }

    public void loadData() {
        tactic = json.fromJson(Tactic.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    // ----- getters -----
    public Tactic getTactic() { return this.tactic; }

    // ----- setters -----
    public void setTactic(Tactic tactic) { this.tactic = tactic; }
    public void setTacticName(String name) { this.tactic.setName(name); }
    public void setFileHandle(String path) { this.fileHandle = Gdx.files.local(path); }

}
