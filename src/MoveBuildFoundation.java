package src.mtOlympus;
import ks.common.games.Solitaire;
import ks.common.model.*;

/** Move Aces and Deuces of all suites to foundation (16 cards total)
 * Created by billyzs on 11/16/16.
 */
public class MoveBuildFoundation extends Move {
    // attributes
    protected Pile[] piles;

    public MultiDeck getmDeck() {
        return mDeck;
    }

    protected MultiDeck mDeck;

    // constructor
    public MoveBuildFoundation(MultiDeck d, Pile[] p){
        super();
        assert p.length == 16;
        piles = p;
        mDeck = d;
    }

    /**
     * Move is valid if Piles are all empty, mDeck is full
     * @param theGame the current game
     * @return boolean
     */
    @Override
    public boolean valid(Solitaire theGame){
        boolean out = (mDeck.count() == 104);
        for (Pile p : piles){
            out &= p.empty();
        }
        return out;
    }


    /**
     * Deal those 16 cards.
     * mDeck is sorted AS, AD ... KH, KS, AS, AD ... KH, KS.
     */
    @Override
    public boolean doMove(Solitaire theGame){
        // deal out the cards
        if (valid(theGame)){
            // have to invert the stack...
            MultiDeck nDeck = new MultiDeck(mDeck.getName(), 2);
            Deck d1 = new Deck("d1");
            Deck d2 = new Deck("d2");
            d1.create(Deck.OrderByRank);
            d2.create(Deck.OrderByRank);

            while(!d1.empty() && !d2.empty()){
                nDeck.add(d1.get());
                nDeck.add(d2.get());
            }
            for (Pile p : piles){
                p.add(nDeck.get());
            }
            mDeck = nDeck;
            theGame.updateNumberCardsLeft(
                    ((MtOlympus) theGame).maxPoints
                            - ((MtOlympus) theGame).numPile
            );
            theGame.updateScore(+((MtOlympus) theGame).numPile);
            theGame.pushMove(this);
            theGame.refreshWidgets();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * undo not needed
     */
    @Override
    public boolean undo(Solitaire theGame){
        return true;
    }
}
