package src.mtOlympus;


import ks.common.games.Solitaire;
import ks.common.model.*;
import ks.common.view.ColumnView;
import ks.common.model.Column;
import ks.common.view.*;


import java.awt.event.MouseEvent;

/** AdapterColumn should handle dragging a column onto another, and dragging one card onto a pile
 * Created by billyzs on 11/17/16.
 */
public class AdapterColumn extends AdapterStack{
    // attribute inherited from super

    /**
     * SolitaireMouseMotionAdapter constructor comment.
     *
     * @param theGame
     */
    // constructor

    public AdapterColumn(Solitaire theGame, Widget w) {
        super(theGame, w);
    }

    // entry point: should at least override mousePressed and mouseReleased, not sure about mouseDragged


    /*@Override
    public void mouseDragged(MouseEvent mouseEvent) {
        super.mouseDragged(mouseEvent);
        Widget w = game.getContainer().getActiveDraggingObject();
        w.redraw();
    }*/

    /**
     * Should coordinate events at the beginning of a drag event.
     * Just set part of a column free for dragging.
     * For just a press event, no move is necessary?
     * @param me MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent me) {

        super.mousePressed(me);
        /*Container c = game.getContainer();

        //TODO delete this block after extending AdapterStack
        {
            // First get the container
            // Container c = game.getContainer();
            Column currentCol = (Column) currentView.getModelElement();
            // return if no cards to be chosen
            if (currentCol.empty()) {
                System.out.println("AdapterColumn::mousePressed: selected an empty column");
                return;
            }
            // there are cards in currentCol. Get the CV and col that are selected.



            // if we get here, the user has selected a col that contains cards

            Widget w = c.getActiveDraggingObject();
            if (w != Container.getNothingBeingDragged()) {
                System.err.println("AdapterColumn::mousePressed(): Unexpectedly encountered a Dragging object during mouse press");
                return;
            }
        }*/
        /*c = game.getContainer();
        w = c.getActiveDraggingObject();*/


        // Pretty sure this alters the currentView and its element. Need to append the selected cards back.
        ColumnView selectedCV = ((ColumnView) currentView).getColumnView(me);
        Column selectedCol = (Column) selectedCV.getModelElement();
        if(!((MtOlympus) game).isDraggable(selectedCol)){
            ((Stack) currentView.getModelElement()).push(selectedCol);
            System.out.println("AdapterColumn::mousePressed: selected an nondraggable column");
            c.releaseDraggingObject();
            return;
        }

        // tell the container that the selected cards in currentCV is being dragged, and where in that widget the user clicked
        c.setActiveDraggingObject(selectedCV, me);

        // tell container the source of the dragging object
        c.setDragSource(currentView);
        currentView.redraw();
        // selectedCV.redraw(); // needed?
        // c.repaint();
        // game.refreshWidgets();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
/*
        Container c = game.getContainer();
        Widget w = c.getActiveDraggingObject();
        *//*Container c = game.getContainer();

        // return if there's no card being dragged
        Widget w = c.getActiveDraggingObject();
        if (w == Container.getNothingBeingDragged()){
            System.err.printf("AdapterColumn::mouseReleased: Nothing being dragged atm");
            c.releaseDraggingObject();
            return;
        }*//*
        // handle release
        Stack src = (Stack) c.getDragSource().getModelElement();
        Stack sel = (Stack) w.getModelElement();
        Stack tgt = ((MtOlympus) game).canAddTo(sel, (Stack) currentView.getModelElement()) ?
                (Stack) currentView.getModelElement()
                : src;

        MoveStackToStack move = new MoveStackToStack(src, tgt, sel);
        if(move.doMove(game)){
            // upon successful completion, update the widgets that changed
            game.pushMove(move);
            game.refreshWidgets();
            c.releaseDraggingObject();
            c.repaint();
        }
        else{ // shouldn't really happen
            System.err.printf("AdapterColumn::mouseReleased: Neither performed the move nor put the dragged obj back");
            c.releaseDraggingObject();
            c.repaint();
        }*/
    }
}
