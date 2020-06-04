package com.gdx.tdl.map.tct;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe que regista cada frame indicada pelo utilizador
 *
 * Serao guardadas os movimentos que os jogadores podem realizar (run, drible, pass, screen) na frame atual
 * Serao guardadas os movimentos que os jogadores podem receber (pass, screen) na frame atual
 * Serao guardadas as posicoes dos jogadores (TODO atuais e anteriores? so atuais? so anteriores?)
 * Dicionario com par chave/valor <movimento, posicao>
 *
 * Quando for pedida a reproducao da jogada, colocar jogadores na posicao inicial,
 * percorrer a lista das frames criadas, aplicando os movimentos de cada jogador
 * (TODO criar intervalo de tempo entre cada frame? Deixar jogadores chegar ao fim do movimento para introduzir o tempo)
 * (TODO criar intervalos de tempo para movimentos? No passe deixar que recetor chegue ao target?
 */
public class Tactic {
    HashMap<Integer, Map.Entry<Integer[], Vector2>> movements;
    public Vector2[] initialPos;
    String name;
    int nFrames;

    public Tactic() {
        DateFormat dateFormat = new SimpleDateFormat("ddMMHHmm");
        String strDate = dateFormat.format(new Date(TimeUtils.millis()));

        this.name = "tactic" + strDate;
        this.movements = new HashMap<>();
        this.initialPos = new Vector2[10];
        this.nFrames = -1;
    }

    public Tactic(String name) {
        this();
        this.name = name;
    }

    public void addToMovements(int p, Integer[] moves, Vector2 pos) {
        Gdx.app.log("LOG", "Player " + p);
        Gdx.app.log("LOG", "Movement [" + moves[0] + " | " + moves[1] + "]");
        Gdx.app.log("LOG", "Position (" + pos.x + ", " + pos.y + ")");
        movements.put(p, new AbstractMap.SimpleEntry<>(moves, pos));
    }

    public void addInitialPos(int p, Vector2 pos) {
        Gdx.app.log("LOG", "Player " + p);
        Gdx.app.log("LOG", "Init Position (" + pos.x + ", " + pos.y + ")");
        initialPos[p] = pos;
    }

    // getters
    HashMap<Integer, Map.Entry<Integer[], Vector2>> getMovements() { return movements; }
    Map.Entry<Integer[], Vector2> getEntry(int player) { return movements.get(player); }
    public Vector2 getEntryValue(int player) { return getEntry(player).getValue(); }
    public Integer[] getEntryKey(int player) { return getEntry(player).getKey(); }
    String getName() { return this.name; }
    public int getSize() { return movements.size(); }
    public int getNFrames() { return nFrames; }

    // setters
    public void setName(String name) { this.name = name; }
    public void setToBegin() { this.nFrames = 0; }
    public void setNFrames(int nFrames) { this.nFrames = nFrames; }
    public void cleanMovements() { movements.clear(); }
    public void cleanInitPos() { for (Vector2 pos : initialPos) pos = null; }
}
