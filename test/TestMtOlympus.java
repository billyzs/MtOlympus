package src.mtOlympus;

import ks.client.gamefactory.GameWindow;
import ks.common.model.*;
import ks.launcher.Main;


import java.awt.event.MouseEvent;

/**
 * Created by billyzs on 11/17/16.
 */
public class TestMtOlympus extends KSTestCase {
    // game under test
    MtOlympus game;
    GameWindow gw;
    public static int numCalled=0;

    protected void setUp(){
        game = new MtOlympus();
        gw = Main.generateWindow(game, 5); //rand seed = 5
    }
    protected void tearDown(){
        gw.setVisible(false);
        gw.dispose();
    }
    public Column generateTestCol(int[] rank, int[] suit){
        Column ret = new Column("testCol".concat(Integer.toString(numCalled++)));
        assert(suit.length == rank.length);
        for(int i=0; i < suit.length; i++){
            ret.add(new Card(rank[i],suit[i]));
        }
        return ret;
    }
    public void testMoveBuildFoundation(){
        // piles should have been set up with the correct cards
        for(int i = 0; i < game.numPile; i++){
            assertEquals(new Card(i/8+1, (i%8)/2+1), game.piles[i].peek());
        }
        // deck should have 104 - 16 cards
        assertEquals(game.maxPoints-game.numPile, game.mDeck.count());
        assertEquals(game.maxPoints - game.numPile, game.getNumLeft().getValue());
    }
    public void testDealCards(){
        MouseEvent press = createPressed(game, game.deckView, 0, 0);
        game.deckView.getMouseManager().handleMouseEvent(press);
        assertEquals(game.maxPoints - game.numPile - game.numCol, game.getNumLeft().getValue());
        for (Column c : game.columns){
            assertEquals(1, c.count());
        }
        MouseEvent undoPress = this.createRightClick(game, game.deckView, 0, 0);
        for (Column c : game.columns){
            assertEquals(1, c.count());
        }
    }
    // ModelFactory can be used to init a game
    // createPress()
    public void testMoveColToCol(){
        // test the move classes independently from controllers
        int[] toRank = {11, 9, 7, 5};
        int[] toSuit = {4, 4 ,4, 4};
        int[] fromRank = {3, 1};
        int[] fromSuit = {4, 4};

        Column fromCol = generateTestCol(fromRank, fromSuit);
        Column toCol = generateTestCol(toRank, toSuit);
        Column falseCol = new Column("For false cases");

        Stack emptyStack = new Stack("empty stack");
        // Do helper methods work?
        {
            falseCol.add(new Card(1, 3));
            assertTrue(game.isDescendingBy(fromCol, 2));
            assertTrue(game.isDescendingBy(falseCol, 1));
            assertTrue(game.isDescendingBy(falseCol, 2));
            assertTrue(game.isDescendingBy(toCol, 2));
            assertFalse(game.isDescendingBy(toCol, 3));
            assertTrue(game.isDescendingBy(emptyStack, 2));
            assertTrue(game.isDescendingBy(emptyStack, 1));

            assertTrue(game.canAddTo(fromCol, toCol));
            assertFalse(game.canAddTo(falseCol, toCol));
            assertFalse(game.canAddTo(falseCol, emptyStack));
            assertTrue(game.isDraggable(falseCol));
            assertTrue(game.isDraggable(fromCol));
            assertTrue(game.isDraggable(toCol));
            falseCol.add(new Card(2, 4));
            assertFalse(game.canAddTo(falseCol, toCol));
            assertFalse(game.canAddTo(falseCol, emptyStack));
            assertFalse(game.isDraggable(falseCol));
            assertTrue(game.isDraggable(toCol));
            assertTrue(game.isDraggable(fromCol));
        }
        // Does move work for col to col?
        Move m = new MoveStackToStack(fromCol, toCol, fromCol);
        assertTrue(m.valid(game));
        assertTrue(m.doMove(game));
        toCol.add(new Card(3,4));
        assertEquals(toCol,((MoveStackToStack) m).getTgt());
        // assertTrue(fromCol.empty());

        // does undo work for col to col?
        m.undo(game);
        fromCol.add(toCol.get());
        assertEquals(toCol, ((MoveStackToStack) m).getTgt());
        assertEquals(fromCol, ((MoveStackToStack) m).getSrc());

        // TODO does move work for col to pile?
        Pile testPile = new Pile();
        testPile.add(new Card(3, 4));
        assertTrue(game.canAddTo(toCol, testPile));
        assertFalse(game.canAddTo(testPile, toCol));
        assertFalse(game.canAddTo(fromCol, testPile));

        MoveStackToStack colPileMove = new MoveStackToStack(toCol, testPile,toCol);
        assertTrue(colPileMove.doMove(game));

        for(int i=11; i>2; i-=2){
            Card c = testPile.peek((i-11)/2+4);
            assertTrue(i == c.getRank());
            assertTrue(4 == c.getSuit());

        }
        // TODO does undo work for col to pile?
        assertTrue(colPileMove.undo(game));
        assertEquals(3, testPile.get().getRank());
    }

}
