package src.mtOlympus;
import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.MultiDeck;
import ks.common.model.Move;

/**
 * Deal out up to 9 cards to the end of each column.
 * If there are less than 9 cards left in the deck, deal to the first n columns from the left.
 * Update the number of cards left in the deck accordingly
 * Created by billyzs on 11/16/16.
 */
public class MoveDealCards extends Move{
    // name, constructor, entry point, attributes
    protected Column[] columns;
    protected MultiDeck mDeck;

    public MoveDealCards(MultiDeck d, Column[] cols){
        columns = cols;
        mDeck = d;
    }

    /**
     * No point dealing 0 cards, so return false when mDeck is empty
     * true otherwise
     * @param theGame the current game
     * @return boolean
     */
    public boolean valid(Solitaire theGame){
        return !mDeck.empty();
    }
    public boolean doMove(Solitaire theGame){
        // validate
        if (valid(theGame)){
            int n = Integer.min(columns.length, mDeck.count());
            for (int i=0; i<n; i++){
                columns[i].add(mDeck.get());
            }
            // update count
            theGame.updateNumberCardsLeft(-n);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * undo not allowed, so do nothing and return true
     * @param theGame the game currently being played
     * @return boolean
     */
    public boolean undo(Solitaire theGame){
        return true;
    }
}
