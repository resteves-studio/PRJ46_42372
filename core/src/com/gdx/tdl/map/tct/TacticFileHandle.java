package com.gdx.tdl.map.tct;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TacticFileHandle {
    private FileHandle fileHandle;
    private Tactic tactic;
    private String fileName;

    TacticFileHandle(Tactic tactic) {
        this.tactic = tactic;
        setFilePath(tactic.getName());
        setFileHandle(this.fileName);
    }

    void writeTacticToJSON() {
        // initial positions
        JSONObject initialPositionsObject = new JSONObject();
        for (int i = 0; i < tactic.getInitialPosLength(); i++) {
            JSONArray initPos = new JSONArray();
            initPos.add(tactic.getInitialPos(i).x);
            initPos.add(tactic.getInitialPos(i).y);

            initialPositionsObject.put(i, initPos);
        }
        JSONObject initialPositions = new JSONObject();
        initialPositions.put("initialPositions", initialPositionsObject);

        // movements
        JSONObject movementsObject = new JSONObject();
        for (int i = 0; i < tactic.getSize(); i++) {
            JSONArray key = new JSONArray();
            key.add(tactic.getEntryKey(i)[0]);
            key.add(tactic.getEntryKey(i)[1]);

            JSONArray value = new JSONArray();
            value.add(tactic.getEntryValue(i).x);
            value.add(tactic.getEntryValue(i).y);

            JSONArray move = new JSONArray();
            move.add(key);
            move.add(value);

            movementsObject.put(i, move);
        }
        JSONObject movements = new JSONObject();
        movements.put("movements", movementsObject);

        // name
        JSONObject name = new JSONObject();
        name.put("name", tactic.getName());

        // notes
        JSONObject notes = new JSONObject();
        notes.put("notes", tactic.getNotes());

        // num frames
        JSONObject nFrames = new JSONObject();
        nFrames.put("nFrames", tactic.getNFrames());

        // tactic list
        JSONArray tacticList = new JSONArray();
        tacticList.add(initialPositions);
        tacticList.add(movements);
        tacticList.add(name);
        tacticList.add(notes);
        tacticList.add(nFrames);

        // file writing
        fileHandle.writeString(tacticList.toJSONString(), false);
        Gdx.app.log("SAVED", "TRUE");
    }

    Tactic readTacticFromJSON() {
        Tactic loadedTactic = new Tactic();

        // parser
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(fileHandle.readString());

            JSONArray tacticList = (JSONArray) obj;

            // initial positions
            JSONObject initialPositions = (JSONObject) tacticList.get(0);
            JSONObject initialPositionsObject = (JSONObject) initialPositions.get("initialPositions");
            for (int i = 0; i < initialPositionsObject.size(); i++) {
                JSONArray pos = (JSONArray) initialPositionsObject.get(String.valueOf(i));

                //assert pos != null; // evita npe
                float x = Float.parseFloat(String.valueOf(pos.get(0)));
                float y = Float.parseFloat(String.valueOf(pos.get(1)));

                loadedTactic.addInitialPos(i, new Vector2(x, y));
            }

            // movements
            JSONObject movements = (JSONObject) tacticList.get(1);
            JSONObject movementsObject = (JSONObject) movements.get("movements");
            for (int i = 0; i < movementsObject.size(); i++) {
                JSONArray move = (JSONArray) movementsObject.get(String.valueOf(i));

                JSONArray key = (JSONArray) move.get(0);
                int keyX = Integer.parseInt(String.valueOf(key.get(0)));
                int keyY = Integer.parseInt(String.valueOf(key.get(1)));

                JSONArray value = (JSONArray) move.get(1);
                float valueX = Float.parseFloat(String.valueOf(value.get(0)));
                float valueY = Float.parseFloat(String.valueOf(value.get(1)));

                Integer[] movementsKey = new Integer[] {keyX, keyY};
                Vector2 movementsValue = new Vector2(valueX, valueY);

                loadedTactic.addToMovements(i, movementsKey, movementsValue);
            }

            // name
            JSONObject name = (JSONObject) tacticList.get(2);
            loadedTactic.setName(String.valueOf(name.get("name")));

            // notes
            JSONObject notes = (JSONObject) tacticList.get(3);
            loadedTactic.setNotes(String.valueOf(notes.get("notes")));

            // num frames
            JSONObject nFrames = (JSONObject) tacticList.get(4);
            loadedTactic.setNFrames(Integer.parseInt(String.valueOf(nFrames.get("nFrames"))));
        }
        catch (ParseException e) { e.printStackTrace(); }

        return loadedTactic;
    }

    public String getFilePath() { return this.fileName; }
    public FileHandle getFileHandle() { return this.fileHandle; }

    public void setFilePath(String fileName) { this.fileName = Gdx.files.getLocalStoragePath() + fileName + ".json"; }
    public void setTacticName(String tacticName) { this.tactic.setName(tacticName); }
    public void setFileHandle(String path) { this.fileHandle = Gdx.files.local(path); }
}
