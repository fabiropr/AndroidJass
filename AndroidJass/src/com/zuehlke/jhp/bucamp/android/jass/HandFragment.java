package com.zuehlke.jhp.bucamp.android.jass;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ch.mbaumeler.jass.core.Game;
import ch.mbaumeler.jass.core.card.Card;
import ch.mbaumeler.jass.core.card.CardSuit;
import ch.mbaumeler.jass.core.card.CardValue;
import ch.mbaumeler.jass.core.game.PlayerToken;
import ch.mbaumeler.jass.extended.ui.JassModelObserver;
import ch.mbaumeler.jass.extended.ui.ObserverableMatch.Event;

public class HandFragment extends Fragment implements JassModelObserver {

	private Game game;
	private MainActivity mainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.hand_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mainActivity.getGame().addObserver(this);
		game = mainActivity.getGame();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
	}

	public void updated(Event event, PlayerToken playerToken, Object object) {

		List<Card> cardsInHand = game.getCurrentMatch().getCards(
				mainActivity.getHumanPlayerToken());

		for (int i = 0; i < cardsInHand.size(); i++) {
			final Card card = cardsInHand.get(i);

			Button button = getButtonByIndex(i);
			button.setText(getSuitChar(card.getSuit()) + "\n" + toString(card.getValue()));
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					game.getCurrentMatch().playCard(card);
				}
			});
		}
	}

	private Button getButtonByIndex(int index) {
		switch (index) {
		case 0:
			return button(R.id.button0);
		case 1:
			return button(R.id.button1);
		case 2:
			return button(R.id.button2);
		case 3:
			return button(R.id.button3);
		case 4:
			return button(R.id.button4);
		case 5:
			return button(R.id.button5);
		case 6:
			return button(R.id.button6);
		case 7:
			return button(R.id.button7);
		case 8:
			return button(R.id.button8);

		}
		return null;
	}

	private char getSuitChar(CardSuit cardSuit) {

		switch (cardSuit) {
		case CLUBS:
			return 9827;
		case SPADES:
			return 9824;
		case DIAMONDS:
			return 9830;
		case HEARTS:
			return 9829;
		}
		return '?';

	}

	private String toString(CardValue value) {
		switch (value) {

		case SIX:
			return "6";
		case SEVEN:
			return "7";
		case EIGHT:
			return "8";
		case NINE:
			return "9";
		case TEN:
			return "10";
		case JACK:
			return "J";
		case QUEEN:
			return "Q";
		case KING:
			return "K";
		case ACE:
			return "A";
		default:
			return null;
		}
	}

	private Button button(int id) {
		return (Button) mainActivity.findViewById(id);
	}

}
