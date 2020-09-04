package com.gdx.tdl.map.tct;

public class TacticSingleton {
    private static TacticSingleton instance = null;

    public Tactic tactic;

    private TacticSingleton() {
        tactic = new Tactic();
    }

    public static TacticSingleton getInstance() {
        if (instance == null)
            instance = new TacticSingleton();

        return instance;
    }

    public Tactic getTactic() { return tactic; }
    public void setTactic(Tactic tactic) { this.tactic = tactic; }
}
