package szhou2.MtOlympus;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Pile;
import ks.common.model.Stack;

/**
 * Given a src stack that has part of it being dragged, and a tgt stack upon which the mouse is released
 * determine if the move is valid, and perform doMove and undo accordingly.
 * Created by billyzs on 11/17/16.
 */

public class MoveStackToStack extends ks.common.model.Move {
    Stack src;
    Stack selected;
    Stack tgt;
    int count; // number of cards moved
    boolean mDeckUsed = false;

    // constructor
    public MoveStackToStack(Stack s, Stack t, Stack sel){
        src = s;
        tgt = t;
        selected = sel;
        count = sel.count();
    }

    public Stack getSrc() {
        return src;
    }

    public Stack getTgt() {
        return tgt;
    }

    @Override
    public boolean valid(Solitaire game) {
        // sel draggable, tgt = src ->true
        // sel draggable, tgt =! src - canAddTo
        // sel not draggable -> false
        // draggable && ((tgt==src)||canAddTo)
        return ((MtOlympus) game).isDraggable((Column) selected)
                && (((MtOlympus) game).canAddTo(selected, tgt)
                || tgt == src);

    }


    @Override
    public boolean doMove(Solitaire game) {
        if(valid(game)){
            if (tgt instanceof Column){
                tgt.push(selected); // push builds from bottom up
            }
            else if (tgt instanceof Pile){
                while(!selected.empty()){
                    tgt.add(selected.get());

                }
                game.updateScore(+count);
            }
            if(src.empty() && !((MtOlympus)game).mDeck.empty()) {
                src.add(((MtOlympus) game).mDeck.get());
                mDeckUsed = true;
                game.updateNumberCardsLeft(-1);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean undo(Solitaire game) {

        if(mDeckUsed){
            ((MtOlympus)game).mDeck.add(src.get());
            game.updateNumberCardsLeft(+1);
        }
        for(int n=0; n<count; n++){
            src.add(tgt.get());
        }
        return true;
    }
}

