package com.zuehlke.jhp.bucamp.android.jass;

import static ch.mbaumeler.jass.core.card.CardSuit.DIAMONDS;
import static ch.mbaumeler.jass.core.card.CardSuit.HEARTS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ch.mbaumeler.jass.core.Game;
import ch.mbaumeler.jass.core.Match;
import ch.mbaumeler.jass.core.card.Card;
import ch.mbaumeler.jass.core.card.CardSuit;
import ch.mbaumeler.jass.core.card.CardValue;
import ch.mbaumeler.jass.core.game.Ansage;
import ch.mbaumeler.jass.core.game.PlayedCard;
import ch.mbaumeler.jass.core.game.PlayerToken;
import ch.mbaumeler.jass.extended.ui.JassModelObserver;
import ch.mbaumeler.jass.extended.ui.ObserverableMatch.Event;

public class TableFragment extends Fragment implements JassModelObserver,
		OnDragListener {

	private Game game;
	private MainActivity mainActivity;
	private Map<PlayerToken, TextView> map;

	boolean hasDropped = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.table_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mainActivity.getGame().addObserver(this);
		game = mainActivity.getGame();

		List<PlayerToken> all = game.getPlayerRepository().getAll();

		map = new HashMap<PlayerToken, TextView>();
		map.put(all.get(0), findTextView(R.id.player1));
		map.put(all.get(1), findTextView(R.id.player2));
		map.put(all.get(2), findTextView(R.id.player3));
		map.put(all.get(3), findTextView(R.id.player4));

		findTextView(R.id.player1Name)
				.setText(mainActivity.getName(all.get(0)));
		findTextView(R.id.player2Name)
				.setText(mainActivity.getName(all.get(1)));
		findTextView(R.id.player3Name)
				.setText(mainActivity.getName(all.get(2)));
		findTextView(R.id.player4Name)
				.setText(mainActivity.getName(all.get(3)));

		mainActivity.findViewById(R.id.tableFragment).setOnDragListener(this);
	}

	private TextView findTextView(int id) {
		return (TextView) mainActivity.findViewById(id);
	}

	private TextView getTextView(int id) {
		return (TextView) mainActivity.findViewById(id);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
	}

	public void updated(Event arg0, PlayerToken arg1, Object arg2) {
		Match currentMatch = game.getCurrentMatch();
		Ansage ansage = currentMatch.getAnsage();
		if (ansage != null) {
			TextView trumpfText = getTextView(R.id.trumpf);
			trumpfText.setText(CardUtil.getResourceFor(ansage));
			trumpfText.setTextColor(getResources().getColor(getColor(ansage)));
		}
		List<PlayedCard> cardsOnTable = currentMatch.getCardsOnTable();

		getTextView(R.id.player1).setText("");
		getTextView(R.id.player2).setText("");
		getTextView(R.id.player3).setText("");
		getTextView(R.id.player4).setText("");

		for (PlayedCard playedCard : cardsOnTable) {
			TextView textView = map.get(playedCard.getPlayer());
			textView.setText(CardUtil.toCardString(playedCard.getCard()));
			textView.setText(CardUtil.toCardString(playedCard.getCard()));
			textView.setTextColor(CardUtil
					.color(playedCard.getCard().getSuit()));
		}
	}

	private int getColor(Ansage ansage) {
		return (ansage.isTrumpf(HEARTS) || ansage.isTrumpf(DIAMONDS)) ? R.color.red
				: R.color.black;
	}

	public boolean onDrag(View table, DragEvent event) {
		Drawable enterShape = getResources().getDrawable(
				R.drawable.shape_droptarget);
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			// Do nothing
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			table.setBackgroundDrawable(enterShape);
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			table.setBackgroundDrawable(normalShape);
			break;
		case DragEvent.ACTION_DROP:
			// play card --> TODO: ugly implementation!
			String cardString = event.getClipData().getItemAt(0).getText()
					.toString();
			Card card = deserializeCard(cardString);

			if (!game.getCurrentMatch().isCardPlayable(card)) {
				Toast.makeText(table.getContext(), "Card is not playable!",
						Toast.LENGTH_SHORT).show();
				View cardView = (View) event.getLocalState();
				cardView.setVisibility(View.VISIBLE);
				table.setBackgroundDrawable(normalShape);
				hasDropped = false;
			} else {
				game.getCurrentMatch().playCard(card);
				table.setBackgroundDrawable(normalShape);
				hasDropped = true;
			}
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			table.setBackgroundDrawable(normalShape);
			if (!hasDropped) {
				View view1 = (View) event.getLocalState();
				view1.setVisibility(View.VISIBLE);
			}
		default:
			break;
		}
		return true;
	}

	private Card deserializeCard(String string) {
		String[] split = string.split("_");
		return new Card(CardSuit.valueOf(split[0]), CardValue.valueOf(split[1]));

	}

}
