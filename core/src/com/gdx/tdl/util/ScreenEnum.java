package com.gdx.tdl.util;

import com.gdx.tdl.util.map.AbstractScreen;
import com.gdx.tdl.map.scr.CourtScreen;
import com.gdx.tdl.util.sgn.AbstractStage;
import com.gdx.tdl.sgn.scr.CreateUserScreen;
import com.gdx.tdl.sgn.scr.SignInScreen;
import com.gdx.tdl.sgn.scr.UpdatePasswordScreen;

public enum ScreenEnum {
    LOGIN {
        public AbstractStage getStage(Object... params) { return new SignInScreen(); }
        public AbstractScreen getScreen(Object... params) { return null; }
    },

    CREATE {
        public AbstractStage getStage(Object... params) { return new CreateUserScreen(); }
        public AbstractScreen getScreen(Object... params) { return null; }
    },

    UPD_PWD {
        public AbstractStage getStage(Object... params) { return new UpdatePasswordScreen(); }
        public AbstractScreen getScreen(Object... params) { return null; }
    },

    COURT {
        public AbstractStage getStage(Object... params) { return null; }
        public AbstractScreen getScreen(Object... params) { return new CourtScreen(); }
    };

    public abstract AbstractScreen getScreen(Object... params);
    public abstract AbstractStage getStage(Object... params);
}
