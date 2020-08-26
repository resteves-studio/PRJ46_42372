package com.gdx.tdl;

import com.badlogic.gdx.Game;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.ScreenEnum;
import com.gdx.tdl.util.sgn.StageManager;

import pl.mk5.gdx.fireapp.GdxFIRApp;

public class MainTDL extends Game {
	@Override
	public void create () {
		AssetLoader.load();
		GdxFIRApp.inst().configure();

		StageManager.getInstance().initialize(this);
		StageManager.getInstance().showScreen(ScreenEnum.LOGIN);
	}

	@Override
	public void render () { super.render(); }

	@Override
	public void dispose () {
		super.dispose();
		AssetLoader.dispose();
	}
}
