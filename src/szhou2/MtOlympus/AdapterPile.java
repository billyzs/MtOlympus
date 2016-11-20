package szhou2.MtOlympus;

import ks.common.games.Solitaire;
import ks.common.view.Widget;

import java.awt.event.MouseEvent;

/**
 * Created by billyzs on 11/19/16.
 */
public class AdapterPile extends AdapterStack{

    public AdapterPile(Solitaire theGame, Widget view) {
        super(theGame, view);
    }

    @Override
    /**
     * Shouldn't do anything when mouse pressed on pile
     */
    public void mousePressed(MouseEvent mouseEvent) {
        //super.mousePressed(mouseEvent);
    }

    /**
     *
     * @param mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
    }
}
