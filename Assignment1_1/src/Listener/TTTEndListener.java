/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;
import java.util.EventListener;
/**
 * Event listener invoked when the game is going to end.
 * @author luigi
 */
public interface TTTEndListener extends EventListener{
    
    public void onEnd(TTTEndEvent evt);
}
