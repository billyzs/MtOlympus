package src.mtOlympus;

/**
 * Created by billyzs on 11/16/16.
 */

import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;

import java.awt.*;

public class MtOlympus extends Solitaire{

    public final int numCol = 9;
    public final int numPile = 16;
    public final int maxPoints = 104;

    //TODO why have local variables like these AND model?
    protected MultiDeck mDeck;
    protected Column[] columns = new Column[numCol];;
    protected Pile[] piles = new Pile[numPile];

    protected DeckView deckView;
    protected ColumnView[] columnViews = new ColumnView[numCol];
    protected PileView[] pileViews = new PileView[numPile];
    protected IntegerView scoreView; // # of cards in the foundation
    protected IntegerView cardsLeftView; // # of cards left in the multideck

    /**
     * returns true if all cards in a stack differs by a set common difference
     * note that an empty stack or a stack with only one card will return true for all diff
     * @param s the stack
     * @param diff common difference, - diff means ascending
     * @return
     */
    public boolean isDescendingBy(Stack s, int diff){
        if (s.empty() || s.count() == 1){
            return true;
        }
        for(int i=0; i<s.count()-1; i++){
            if(!(s.peek(i).getRank() - s.peek(i+1).getRank() == diff)){ return false; }
        }
        return true;
    }
    public boolean isDraggable(Column col){
        return isDescendingBy(col, 2) && col.sameSuit();
    }
    /**
     * helper method to determine if a Stack can be added to another
     * put it here to avoid creating a new class
     * @param from: the stack to be added to another stack
     * @param to: the stack to receive new cards
     * @return
     */
    public boolean canAddTo(Stack from, Stack to){
        // Column builds down
        if (from instanceof Column){
            if (to instanceof Column) {
                return (to.empty()) && isDraggable((Column) from) || // a draggable col can always add to an empty stack
                        (from.sameSuit() // from stack is of the same suit
                                // && isDescendingBy(from, 2) // from stack is descending by 2
                                && from.peek(0).sameSuit(to.peek()) // top card in from is same suit
                                && from.peek(0).compareTo(to.peek()) == -2); // and 2 less than last card in to
            }
            // Pile builds up
            else if (to instanceof Pile){
                return (!to.empty() && isDraggable((Column) from)
                        && from.peek().sameSuit(to.peek())
                        && from.peek().compareTo(to.peek()) == 2
                );
            }
        }
        else{
            System.err.println("canAddTo encountered obj it can't handle");
        }
        return false;
    }

    /**
     * calls default constructor
     */
    public MtOlympus(){
        super();
        //TODO need to allocate memory here?
        // columns = new Column[numCol];
        // piles =
        // columnViews = new ColumnView[numCol];
        // pileViews = new PileView[numPile];
    }

    /**
     * Game is won when all 52*2 cards have been moved to the foundation
     * @return
     */
    public boolean hasWon(){
        return getScoreValue() == maxPoints;
    }
    public String getName(){
        return "Mount Olympus";
    }
    public void initialize(){
        // initialize model, currentView and conrollers
        ininializeModel(getSeed()); // has to do order by rank for initial dealing of 16 cards to work


        // model.addElement(mDeck);
        initializeView();
        initializeController();


        // deal all 16 aces and deuces to the foundation
        // deal 1 card each to 9 columns
        // set up score and cards left
        // this.updateScore(16);

        // this.updateNumberCardsLeft(maxPoints - numPile); // 104 - 16
    }
    private void ininializeModel(int seed){

        // initialize the 9 columns
        for (int i = 0; i < numCol; i++){
            columns[i] = new Column("Column".concat(Integer.toString(i)));
            model.addElement(columns[i]);
        }
        // initialize the 16 Piles in foundation
        for (int i = 0; i < numPile; i++){
            piles[i] = new Pile("Pile".concat(Integer.toString(i)));
            model.addElement(piles[i]);
        }
        // initialize the multideck
        mDeck = new MultiDeck("mDeck", 2);
        mDeck.create(Deck.OrderByRank);
        MoveBuildFoundation mbf = new MoveBuildFoundation(mDeck, piles);
        if(mbf.doMove(this)) {
            mDeck = mbf.getmDeck();
            mDeck.shuffle(seed);
        }
        addModelElement(mDeck);
        // initialize score
        // this.updateScore(+16); // dealt 16 cards to foundation
        // this.updateNumberCardsLeft(maxPoints - numPile); // 88
    }
    private void initializeView(){

        CardImages ci = getCardImages();
        int cw = ci.getWidth();
        int ch = ci.getHeight();
        int d1 = 10; // x and y spacing between cards
        int colH = 10 * ci.getOverlap() + ch; // col holds 10 cards initially
        deckView = new DeckView(mDeck);
        deckView.setBounds(d1, d1, cw, ch);
        addViewWidget(deckView);

        int pvX = 2*d1 + cw; //top left X of start of pileViews
        int pvY = d1; // top left Y of start of pileViews

        // initialize pileViews
        for(int i=0; i < numPile/2; i++){
            pileViews[i] = new PileView(piles[i]);
            pileViews[i].setBounds(pvX+i*(d1+cw), pvY, cw, ch);
            pileViews[i+8] = new PileView(piles[i+8]);
            pileViews[i+8].setBounds(pvX+i*(d1+cw), pvY+ch+d1, cw, ch);
            addViewWidget(pileViews[i]);
            addViewWidget(pileViews[i+8]);
        }

        // initialize columnViews
        int cvX = d1;
        int cvY = d1 + 2*(ch+d1);
        for(int i=0; i < numCol; i++){
            int yOffset = (5*Math.abs(4-i));
            columnViews[i] = new ColumnView(columns[i]);
            columnViews[i].setBounds(cvX+i*(d1+cw), cvY+yOffset, cw, colH);
            addViewWidget(columnViews[i]);
        }

        // initialize cardsLeftView and scoreView

        int ivX = d1;
        int ivY = 2*d1 + ch;
        cardsLeftView = new IntegerView(getNumLeft());
        cardsLeftView.setBounds(ivX, ivY, cw, ch/2+d1);
        addViewWidget(cardsLeftView);
        scoreView = new IntegerView(getScore());
        scoreView.setBounds(ivX, ivY+ch/2+d1+1, cw, ch/2+d1);
        addViewWidget(scoreView);
    }
    private void initializeController(){

        // adapter for deck
        deckView.setMouseAdapter(new AdapterDeck(this, deckView));
        // adapter for columns
        for(ColumnView cv : columnViews){
            cv.setMouseAdapter(new AdapterColumn(this, cv));
        }
        for(PileView pv : pileViews){
            pv.setMouseAdapter(new AdapterPile(this, pv));
        }
        // adapter for piles (should they handle adding cards or should column adapter do that?)
        // adapters for IntegerViews
        scoreView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        cardsLeftView.setMouseAdapter(new SolitaireReleasedAdapter(this));
    }
    public static void main(String[] args){
        //TODO
        GameWindow gw = Main.generateWindow(new MtOlympus(), 5);
        gw.setBounds(new Rectangle(0, 0, 800, 600));
        gw.setVisible(true);
    }
}
