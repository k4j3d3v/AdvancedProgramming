
package TTTGame;

import java.beans.*;
import javax.swing.JPanel;

/**
 *
 * @author luigi
 */

public class TTTCell extends JPanel{
    public static enum TTTState {
    INITIAL, X, O
    }
    public static final String PROPERTY = "state";
    
    private TTTState state;
    
    private PropertyChangeSupport propertySupport;
    private VetoableChangeSupport vetos;
    
    public TTTCell() {
        propertySupport = new PropertyChangeSupport(this);
        vetos = new VetoableChangeSupport(this);
    }
    
    public TTTState getState() {
        return state;
    }
    
    public void setState(TTTState value) {
        TTTState oldState = state;
        try
        {
            vetos.fireVetoableChange(PROPERTY, oldState, state);
            state = value;
            propertySupport.firePropertyChange(PROPERTY, oldState, state);
        }
        catch(PropertyVetoException e)
        {
            e.printStackTrace();
        }
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    
    public void addVetoableChangelistener(VetoableChangeListener l)
    { 
        vetos.addVetoableChangeListener(l);
    }
    
    public void removeVetoableChangelistener(VetoableChangeListener l)
    { 
        vetos.removeVetoableChangeListener(l);
    }
}
