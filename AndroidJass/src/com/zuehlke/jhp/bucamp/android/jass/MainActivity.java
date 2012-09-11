package com.zuehlke.jhp.bucamp.android.jass;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import ch.mbaumeler.jass.core.JassEngine;
import ch.mbaumeler.jass.core.Match;
import ch.mbaumeler.jass.core.game.PlayerToken;
import ch.mbaumeler.jass.extended.ai.simple.SimpleStrategy;
import ch.mbaumeler.jass.extended.ai.simple.SimpleStrategyEngine;
import ch.mbaumeler.jass.extended.ui.ObservableGame;

public class MainActivity extends Activity {

	private ObservableGame game;
	private SimpleStrategy strategy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		game = new ObservableGame(new JassEngine().createJassGame());
		strategy = new SimpleStrategyEngine().create();
	}

	public ObservableGame getGame() {
		
		return game;
	}
	
	public PlayerToken getHumanPlayerToken() {
		return game.getPlayerRepository().getAll().get(0);
	}

	public void refreshClicked(View view) {
		game.notifyObservers();
		Match match = game.getCurrentMatch();
		if (match.getActivePlayer() != getHumanPlayerToken()) {
			if (match.getAnsage() == null) {
				match.setAnsage(strategy.getAnsage(match));
			}
			game.getCurrentMatch().playCard(strategy.getCardToPlay(match));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
