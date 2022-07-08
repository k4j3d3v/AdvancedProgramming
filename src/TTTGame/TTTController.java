/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTTGame;

import Listener.TTTEndEvent;
import Listener.TTTEndListener;
import Listener.TTTResetListener;
import Listener.TTTWonEvent;
import java.beans.*;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author luigi
 */
public class TTTController extends JLabel implements VetoableChangeListener, 
        TTTResetListener, TTTEndListener{ 
    
   
    private TTCell.TTTState lastMove;
    private final TTTBoard board;
    public TTTController(TTTBoard b) {
        super();
        this.board = b;
        reset();

    }
    
    @Override
    public final void reset()
    {   
        GameState state = board.state;
        super.setText(state.toString());

        lastMove = null;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {

        TTCell.TTTState oldV = (TTCell.TTTState) pce.getOldValue();
        TTCell.TTTState newV = (TTCell.TTTState) pce.getNewValue();
        if(oldV != TTCell.TTTState.INITIAL || newV == lastMove)
            throw new PropertyVetoException("Not "+newV+" turn!", pce);
       
        lastMove = newV;
        //String turn = "X".equals(newV.toString())? "O"  : "X";
        board.state = "X".equals(newV.toString())? GameState.O_TURN : GameState.X_TURN;
        TTTBoard board = (TTTBoard) SwingUtilities.getRoot(this);
     
        setText(board.state.toString());
    }

    @Override
    public void onEnd(TTTEndEvent evt) {
        if(evt instanceof TTTWonEvent)
        {
            String winner = ((TTTWonEvent) evt).getWinner();
            board.state = "X".equals(winner)? GameState.WIN_X : GameState.WIN_O; 
            this.setText(board.state.toString());
        }
        else
        {
           board.state = GameState.GAME_END;
           this.setText(GameState.GAME_END.toString());
        }
        
    }

    
}
