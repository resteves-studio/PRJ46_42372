package com.gdx.tdl.map.tct;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.GdxFIRStorage;
import com.badlogic.gdx.Gdx;
import com.gdx.tdl.util.AssetLoader;

public class SaveLoad {
    TacticFileHandle tacticFileHandle;
    Tactic tactic;

    private boolean success, fail, tacticLoaded;

    public SaveLoad(Tactic tactic) {
        if (tactic != null) {
            setTactic(tactic);
            setTacticFileHandle(tactic);
        }

        this.success = false;
        this.fail = false;
        this.tacticLoaded = false;

        /*GdxFIRAuth.instance()
                .signInWithEmailAndPassword("resteves.studio@gmail.com", "basket".toCharArray())
                .then(gdxFirebaseUser -> Gdx.app.log("LOGGED IN TO", "resteves.studio@gmail.com"))
                .fail((s, throwable) -> { Gdx.app.log("FAILED SIGNIN", s); }); //setFail(true); });*/
    }

    // guarda a tatica localmente
    public void saveLocalData() {
        if (tactic != null && !tactic.isInitPosEmpty() && !tactic.getMovements().isEmpty()) {
            tacticFileHandle.setFilePath(tactic.getName());
            tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());
            tacticFileHandle.writeTacticToJSON();
            //setSuccess(true);
        } else {
            Gdx.app.log("NOT SAVED", "Local");
            //setFail(true);
        }
    }

    // guarda a tatica na cloud
    public void saveCloudData() {
        if (tactic != null && !tactic.isInitPosEmpty() && !tactic.getMovements().isEmpty()) {
            tacticFileHandle.setFilePath(tactic.getName());
            tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());
            tacticFileHandle.writeTacticToJSON();

            if (tacticFileHandle.getFileHandle().exists()) {
                GdxFIRStorage.instance()
                        .upload(tacticFileHandle.getFileHandle().path(), tacticFileHandle.getFileHandle())
                        .after(GdxFIRAuth.instance().getCurrentUserPromise())
                        .then(fileMetadata -> {
                            Gdx.app.log("SAVED TO", "Cloud");
                            //setSuccess(true);
                        })
                        .fail((s, throwable) -> {
                            Gdx.app.log("NOT SAVED TO", "Cloud");
                            //setFail(true);
                        });
            }
        } else {
            //setFail(true);
        }
    }

    // carrega a tatica de um ficheiro local
    public void loadLocalData() {
        tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());
        if (tacticFileHandle.getFileHandle().exists()) {
            setTactic(tacticFileHandle.readTacticFromJSON());

            Gdx.app.log("LOADED FROM", "Local");
            Gdx.app.log("NEW TACTIC", "Name: " + getTactic().getName());
            Gdx.app.log("NEW TACTIC", "Notes: " + getTactic().getNotes());
            Gdx.app.log("NEW TACTIC", "Size: " + getTactic().getSize());

            //setSuccess(true);
            setTacticLoaded(true);
        } else {
            //setFail(true);
        }
    }

    // carrega a tatica de um ficheiro na cloud
    public boolean loadCloudData() {
        tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());
        if (tacticFileHandle.getFileHandle().exists()) {
            GdxFIRStorage.instance()
                    .download(tacticFileHandle.getFileHandle().path(), tacticFileHandle.getFileHandle())
                    .after(GdxFIRAuth.instance().getCurrentUserPromise())
                    .then(fileMetadata -> {
                        setTactic(tacticFileHandle.readTacticFromJSON());
                        Gdx.app.log("LOADED FROM", "Cloud");
                        setTacticLoaded(true);
                        AssetLoader.loaded = true;
                    })

                    .fail((s, throwable) -> {
                        Gdx.app.log("NOT SAVED TO", "Cloud");
                    });
        } else {
            //setFail(true);
        }

        return AssetLoader.loaded;
    }

    // ----- getters -----
    public Tactic getTactic() { return this.tactic; }
    public boolean isFail() { return this.fail; }
    public boolean isSuccess() { return this.success; }
    public boolean wasTacticLoaded() { return this.tacticLoaded; }

    // ----- setters -----
    public void setTactic(Tactic tactic) { this.tactic = tactic; }
    public void setTacticName(String tacticName) { this.tacticFileHandle.setTacticName(tacticName); }
    public void setTacticFileHandle(Tactic tactic) { this.tacticFileHandle = new TacticFileHandle(tactic); }
    public void setFail(boolean fail) { this.fail = fail; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setTacticLoaded(boolean tacticLoaded) { this.tacticLoaded = tacticLoaded; }

}
