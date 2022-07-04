/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTTGame;

import Listener.TTTEndEvent;
import Listener.TTTEndListener;
import Listener.TTTResetListener;
import java.beans.*;
import javax.swing.JLabel;

/**
 *
 * @author luigi
 */
public class TTTController extends JLabel implements VetoableChangeListener, 
        TTTResetListener, TTTEndListener{ 
    
   
    private TTCell.TTTState lastMove;
   
    public TTTController() {
        super();
        reset();
//        super.setText("START GAME");
//        lastMove = null;
//        propertySupport = new PropertyChangeSupport(this);
    }
    
    @Override
    public final void reset()
    {   
        super.setText("START GAME");
        lastMove = null;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {

        TTCell.TTTState oldV = (TTCell.TTTState) pce.getOldValue();
        TTCell.TTTState newV = (TTCell.TTTState) pce.getNewValue();
        if(oldV != TTCell.TTTState.INITIAL || newV == lastMove)
            throw new PropertyVetoException("Not "+newV+" turn!", pce);
       
        lastMove = newV;
        String turn = "X".equals(newV.toString())? "O" : "X";
        setText("Next move: "+ turn);
    }

    @Override
    public void onEnd(TTTEndEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
