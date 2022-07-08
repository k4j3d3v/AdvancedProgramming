/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import TTTGame.TTCell;

/**
 *
 * @author luigi
 */
public class TTTWonEvent extends TTTEndEvent{
    
    public TTTWonEvent(Object src) {
        super(src);
    }
    
    public String getWinner()
    {
        return ((TTCell)this.source).getState().toString();
    }
}
