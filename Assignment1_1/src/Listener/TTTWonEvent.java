/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import TTTGame.TTTCell;

/**
 * Class representing the game end for Won.
 * @author luigi
 */
public class TTTWonEvent extends TTTEndEvent{
    
    public TTTWonEvent(Object src) {
        super(src);
    }
    
    public String getWinner()
    {
        return ((TTTCell)this.source).getState().toString();
    }
}
