/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import java.util.EventObject;
/**
 *
 * @author luigi
 */
public class TTTEndEvent extends EventObject{
    
    public enum EndType { FULL_BOARD, WON };
    private EndType type;
    
    public TTTEndEvent(Object source) {
        super(source);
    }
    
    /**
     * diocane cane
     * @param source
     * @param evtType
     */
    public TTTEndEvent(Object source, EndType evtType) {
        super(source);
        this.type = evtType;
    }
    
}
