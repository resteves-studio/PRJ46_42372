package com.gdx.tdl.map.tct;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.GdxFIRStorage;
import com.badlogic.gdx.Gdx;
import com.gdx.tdl.util.AssetLoader;

public class SaveLoad {
    TacticFileHandle tacticFileHandle;

    private boolean success, fail, tacticLoaded;

    public SaveLoad() {
        if (TacticSingleton.getInstance().getTactic() != null)
            this.tacticFileHandle = new TacticFileHandle();

        this.success = false;
        this.fail = false;
        this.tacticLoaded = false;
    }

    // guarda a tatica localmente
    public void saveLocalData() {
        if (TacticSingleton.getInstance().getTactic() != null && !TacticSingleton.getInstance().getTactic().isInitPosEmpty() && !TacticSingleton.getInstance().getTactic().getMovements().isEmpty()) {
            tacticFileHandle.setFilePath(TacticSingleton.getInstance().getTactic().getName());
            tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());
            tacticFileHandle.writeTacticToJSON();
        } else {
            Gdx.app.log("NOT SAVED", "Local");
        }
    }

    // guarda a tatica na cloud
    public void saveCloudData() {
        if (TacticSingleton.getInstance().getTactic() != null && !TacticSingleton.getInstance().getTactic().isInitPosEmpty() && !TacticSingleton.getInstance().getTactic().getMovements().isEmpty()) {
            tacticFileHandle.setFilePath(TacticSingleton.getInstance().getTactic().getName());
            tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());
            tacticFileHandle.writeTacticToJSON();

            if (tacticFileHandle.getFileHandle().exists()) {
                GdxFIRStorage.instance()
                    .upload(tacticFileHandle.getFileHandle().path(), tacticFileHandle.getFileHandle())
                    .after(GdxFIRAuth.instance().getCurrentUserPromise())
                    .then(fileMetadata -> {
                        Gdx.app.log("SAVED TO", "Cloud");
                    })
                    .fail((s, throwable) -> {
                        Gdx.app.log("NOT SAVED TO", "Cloud");
                    });
            }
        }
    }

    // carrega a tatica de um ficheiro local
    public void loadLocalData(String fileName) {
        tacticFileHandle.setFilePath(fileName);
        tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());

        if (tacticFileHandle.getFileHandle().exists()) {
            TacticSingleton.getInstance().setTactic(tacticFileHandle.readTacticFromJSON());

            Gdx.app.log("LOADED FROM", "Local");
            Gdx.app.log("NEW TACTIC", "Name: " + TacticSingleton.getInstance().getTactic().getName());
            Gdx.app.log("NEW TACTIC", "Notes: " + TacticSingleton.getInstance().getTactic().getNotes());
            Gdx.app.log("NEW TACTIC", "Size: " + TacticSingleton.getInstance().getTactic().getSize());

            setTacticLoaded(true);
        }
    }

    // carrega a tatica de um ficheiro na cloud
    public boolean loadCloudData(String fileName) {
        tacticFileHandle.setFilePath(fileName);
        tacticFileHandle.setFileHandle(tacticFileHandle.getFilePath());

        GdxFIRStorage.instance()
            .download(tacticFileHandle.getFileHandle().path(), tacticFileHandle.getFileHandle())
            .after(GdxFIRAuth.instance().getCurrentUserPromise())
            .then(fileMetadata -> {
                TacticSingleton.getInstance().setTactic(tacticFileHandle.readTacticFromJSON());
                Gdx.app.log("LOADED FROM", "Cloud");
                setTacticLoaded(true);
                AssetLoader.loaded = true;
            })
            .fail((s, throwable) -> {
                Gdx.app.log("NOT LOADED FROM", "Cloud");
            });

        return AssetLoader.loaded;
    }

    // ----- getters -----
    public boolean isFail() { return this.fail; }
    public boolean isSuccess() { return this.success; }
    public boolean wasTacticLoaded() { return this.tacticLoaded; }

    // ----- setters -----
    public void setTacticName(String tacticName) { this.tacticFileHandle.setTacticName(tacticName); }
    public void setFail(boolean fail) { this.fail = fail; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setTacticLoaded(boolean tacticLoaded) { this.tacticLoaded = tacticLoaded; }

}
