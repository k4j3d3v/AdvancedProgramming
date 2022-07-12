/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import java.util.EventListener;

/**
 * Event listener invoked when the game is to reset.
 * @author luigi
 */
public interface TTTResetListener extends EventListener{
    
    public void reset();
}
