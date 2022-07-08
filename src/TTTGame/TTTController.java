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
import javax.swing.border.EmptyBorder;

/**
 *
 * @author luigi
 */
public class TTTController extends JLabel implements VetoableChangeListener, 
        TTTResetListener, TTTEndListener{ 
    
   
    private TTTCell.TTTState lastMove;
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
        setText(state.toString());
        setBorder(new EmptyBorder(10,10,0,0));//top,left,bottom,right

        lastMove = null;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {

        TTTCell.TTTState oldV = (TTTCell.TTTState) pce.getOldValue();
        TTTCell.TTTState newV = (TTTCell.TTTState) pce.getNewValue();
        if(oldV != TTTCell.TTTState.INITIAL || newV == lastMove)
            throw new PropertyVetoException("Not "+newV+" turn!", pce);
       
        lastMove = newV;
        board.state = "X".equals(newV.toString())? GameState.O_TURN : GameState.X_TURN;     
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
