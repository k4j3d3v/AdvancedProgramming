/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTTGame;

import java.beans.*;
import java.io.Serializable;
import javax.swing.JLabel;

/**
 *
 * @author luigi
 */
public class TTTController extends JLabel implements VetoableChangeListener {
    
   
    private TTCell.TTTState lastMove;
    
    public TTTController() {
        super();
        reset();
//        super.setText("START GAME");
//        lastMove = null;
//        propertySupport = new PropertyChangeSupport(this);
    }
    
    public final void reset()
    {   
        super.setText("START GAME");
        lastMove = null;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
        System.out.println("Vetoable change! "+ pce.getPropertyName() + " "
        + pce.getNewValue());
        TTCell.TTTState old = (TTCell.TTTState) pce.getOldValue();
        TTCell.TTTState newV = (TTCell.TTTState) pce.getNewValue();
        System.out.println("lastMove: "+lastMove + " Cell "+pce.getSource().hashCode()+
                ":\n old value: "+old +", new value:"+newV);
        if(old != TTCell.TTTState.INITIAL || newV == lastMove)
            throw new PropertyVetoException("Not "+newV+" turn!", pce);
       
        lastMove = newV;
        String turn = "X".equals(newV.toString())? "O" : "X";
        setText("Next move: "+ turn);
    }
    
}
