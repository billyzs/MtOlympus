package szhou2.MtOlympus;
import ks.tests.*;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.launcher.Main;

import java.awt.event.MouseEvent;


/**
 * Created by billyzs on 11/19/16.
 */
public class TestAdapters extends KSTestCase {
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
    public void testAdapters(){
        game.columns[0].add(new Card(3, 1));
        MouseEvent selCol0 = createPressed(game, game.columnViews[0], 3, 3);
        game.columnViews[0].getMouseManager().handleMouseEvent(selCol0);
        assertEquals(game.columnViews[0].getName() ,game.getContainer().getDragSource().getName());
        MouseEvent selPile0 = createReleased(game, game.pileViews[0], 3, 3);
        game.pileViews[0].getMouseManager().handleMouseEvent(selPile0);
        assertEquals(3, game.piles[0].peek().getRank());
        assertFalse(game.columns[0].empty()); // a card should have been dealt to col0


        game.columns[1].add(new Card(5,2));
        game.columns[1].add(new Card(6,3));
        MouseEvent selCol1 = createPressed(game, game.columnViews[1], 3, 3);
        game.columnViews[1].getMouseManager().handleMouseEvent(selCol1);
        assertEquals(null ,game.getContainer().getDragSource()); //shouldn't be draggable
        MouseEvent selPile1 = createReleased(game, game.pileViews[1], 3, 3);
        game.pileViews[1].getMouseManager().handleMouseEvent(selPile1);
        assertEquals(new Card(5,2), game.columns[1].peek(0));
        assertEquals(new Card(6,3), game.columns[1].peek());

        MouseEvent pressP1 = createPressed(game, game.pileViews[1], 3, 3);
        game.pileViews[1].getMouseManager().handleMouseEvent(pressP1);
        assertEquals(new Card(1, 1), game.piles[1].peek());

        // AdapterCol

        game.columns[4].add(new Card(3, 1));
        game.columns[2].add(new Card(5,1));
        game.columnViews[4].getMouseManager().handleMouseEvent(createPressed(game, game.columnViews[4], 0, 0));
        MouseEvent selC2 = createReleased(game, game.columnViews[2], 3, 3);
        game.columnViews[2].getMouseManager().handleMouseEvent(selC2);
        assertEquals(new Card(3, 1), game.columns[2].peek());

    }

}
