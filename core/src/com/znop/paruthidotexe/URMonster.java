package com.znop.paruthidotexe;

import com.badlogic.gdx.Game;
import com.znop.paruthidotexe.screens.InGame;

public class URMonster extends Game {

	@Override
	public void create() {
		setScreen(new InGame());
	}
}
