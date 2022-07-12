/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import java.util.EventObject;

/**
 *  Class representing the game end events.
 * @author luigi
 */
public class TTTEndEvent extends EventObject {
    
    public TTTEndEvent(Object source) {
        super(source);
    }
    
}
