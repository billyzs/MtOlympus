package szhou2.MtOlympus;

import ks.common.games.Solitaire;
import ks.common.model.Stack;
import ks.common.view.Container;
import ks.common.view.Widget;
import java.awt.event.MouseEvent;

/**
 * Created by billyzs on 11/19/16.
 */
public abstract class AdapterStack extends java.awt.event.MouseAdapter{
    protected Widget currentView;// the associated currentView object
    protected Solitaire game = null; // the current game
    protected Container c = null;
    protected Widget w = null;
    protected MoveStackToStack move = null;

    public AdapterStack(Solitaire theGame, Widget view){
        currentView = view;
        game = theGame;
        c = theGame.getContainer();
        w = c.getActiveDraggingObject();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        c = game.getContainer();
        w = c.getActiveDraggingObject();
        // First get the container
        // Container c = game.getContainer();
        Stack currentStack = (Stack) currentView.getModelElement();
        // return if no cards to be chosen
        if (currentStack.empty()){
            System.out.println("szhou2.MtOlympus.AdapterStack::mousePressed: selected an empty column");
            return;
        }
        // Widget w = c.getActiveDraggingObject();
        if (w != Container.getNothingBeingDragged()){
            System.err.println("szhou2.MtOlympus.AdapterStack::mousePressed(): Unexpectedly encountered a Dragging object during mouse press");
            return;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
        c = game.getContainer();
        w = c.getActiveDraggingObject();
        // Container c = game.getContainer();

        // return if there's no cards being dragged
        // Widget w = c.getActiveDraggingObject();
        if (w == Container.getNothingBeingDragged()){
            System.err.println("szhou2.MtOlympus.AdapterStack::mouseReleased: Nothing being dragged atm");
            c.releaseDraggingObject(); //need to to this otherwise we lose card
            return;
        }
        Stack src = (Stack) c.getDragSource().getModelElement();
        Stack sel = (Stack) w.getModelElement();
        Stack tgt = ((MtOlympus) game).canAddTo(sel, (Stack) currentView.getModelElement()) ?
                (Stack) currentView.getModelElement()
                : src;
        move = new MoveStackToStack(src, tgt, sel);
        if(move.doMove(game)){
            // upon successful completion, update the widgets that changed
            game.pushMove(move);
            game.refreshWidgets();
            c.releaseDraggingObject();
            c.repaint();
        }
        else{ // shouldn't really happen
            System.err.printf("szhou2.MtOlympus.AdapterStack::mouseReleased: Neither performed the move nor put the dragged obj back");
            // c.releaseDraggingObject();
            c.repaint();
        }
    }
}
