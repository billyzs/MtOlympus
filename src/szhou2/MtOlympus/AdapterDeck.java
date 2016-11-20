package szhou2.MtOlympus;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.MultiDeck;
import ks.common.view.DeckView;
import ks.common.model.Column;
import ks.common.model.Move;

/**
 * Created by billyzs on 11/17/16.
 */
public class AdapterDeck extends SolitaireReleasedAdapter{
    // attributes
    protected DeckView deckView;

    // constructor
    public AdapterDeck(Solitaire theGame, DeckView dv){
        super(theGame);
        deckView = dv;
    }

    // entry point
    public void mousePressed(java.awt.event.MouseEvent me){
        // deal cards when mouse pressed on deckView
        MultiDeck mDeck = (MultiDeck) deckView.getModelElement();

        //TODO better way of gettng specific models?
        Column[] cols = new Column[9];
        for (int i=0; i < 9; i++){
            cols[i] = (Column) theGame.getModelElement("Column".concat(Integer.toString(i)));
        }

        Move mdc = new MoveDealCards(mDeck, cols);
        if (mdc.doMove(theGame)){
            theGame.pushMove(mdc);
            // remember to refesh!
            theGame.refreshWidgets();
        }
    }

}
