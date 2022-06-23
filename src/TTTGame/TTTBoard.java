/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTTGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.border.Border;
/**
 *
 * @author luigi
 */
public class TTTBoard extends JFrame implements PropertyChangeListener{
    
    TTCell[][] matrix;
    private static final int size = 3;
    TTTController ctlLbl;
    private int move = 0;
    private enum winPattern { COL, ROW, DIAG, ANTIDIAG };
    /**
     * Creates new form TTTBoard
     */
    public TTTBoard() {
        initComponents();
        matrix = new TTCell[size][size];
    }
    
    private void setGrid()
    {
        GridLayout experimentLayout = new GridLayout(size+1,size);
        this.setLayout(experimentLayout);
               
        ctlLbl = new TTTController();                          
              
        for(int i=0; i< size; i++)
        {
            for(int j=0; j < size; j++)
            {
                    matrix[i][j] = new TTCell();
                    matrix[i][j].addPropertyChangeListener(this);
                    matrix[i][j].addVetoableChangeListener(ctlLbl);
                    this.add(matrix[i][j]);
                }
        }

        this.add(ctlLbl); 

        this.setVisible(true);

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TTTBoard board = new TTTBoard();
                board.setGrid();
            }
        });
    }
    
    private void setWinner(TTCell winnerCell, winPattern pat, int pos)
    {
        String winner = winnerCell.getState().toString();
        System.out.println("Game over. Winner is "+ winner);
        ctlLbl.setText("Winner is "+ winner);
        
        switch(pat){
        
            case COL:
            {   
                for(int i=0, j = pos; i < size; i++)
                    matrix[i][j].setBtnWin();
                break;
            }
            case ROW:
            {     
                for(int i=pos, j=0; j < size;j++)
                    matrix[i][j].setBackground(Color.green);
                break;
            }
        
            case DIAG:
            {     
                for(int i=0,j=0; j < size;i++,j++)
                    matrix[i][j].setBackground(Color.green);
                break;
            }
        
            case ANTIDIAG:
            {     
                for(int i=0,j=size-1; j < size;i++,j--)
                    matrix[i][j].setBackground(Color.green);
                break;
            }
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        
        System.out.println("Property changed, the new value is: "+pce.getPropertyName()+": " + pce.getNewValue());
        move++;
        System.out.println("#Moves: "+move);
        if(move >= 5)
        {
            //rows check
            for(int i=0,j=0; i< size; i++)
            {
                if(matrix[i][j].getState()!= TTCell.TTTState.INITIAL 
                && matrix[i][j].getState() == matrix[i][j+1].getState() 
                && matrix[i][j+1].getState() == matrix[i][j+2].getState())
                    setWinner(matrix[i][j],winPattern.ROW, i);
                    //System.out.println("Game over. Winner is "+matrix[i][j].getState());

            }
            //column check
             for(int j=0,i=0; i< size; i++)
            {
                if(matrix[j][i].getState()!= TTCell.TTTState.INITIAL 
                && matrix[j][i].getState() == matrix[j+1][i].getState() 
                && matrix[j+1][i].getState() == matrix[j+2][i].getState())
                    setWinner(matrix[i][j],winPattern.COL, i);

                    //System.out.println("Game over. Winner is "+matrix[i][j].getState());

            }
            //diagonal check
            for(int j=0,i=0; i< size-1; i++,j++)
            {
                if(matrix[j][i].getState()!= TTCell.TTTState.INITIAL 
                && matrix[j][i].getState() == matrix[j+1][i+1].getState())
                {
                    if(i==size-2)
                        setWinner(matrix[i][j],winPattern.DIAG, 0);
                        //System.out.println("Game over. Winner is "+matrix[i][j].getState());
                }
                else
                    i=size;
                
            }
            //anti-diagonal check
            for(int i=0,j=size-1; j > 0; i++,j--)
            {
                System.out.println("i= "+i+" j="+j);
                if(matrix[i][j].getState()!= TTCell.TTTState.INITIAL 
                && matrix[i][j].getState() == matrix[i+1][j-1].getState())
                {
                    if(i==1)
                    {
                        setWinner(matrix[i][j],winPattern.ANTIDIAG, 0);
                    }
                }
                else
                    i=0;
                
            }
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    }
}