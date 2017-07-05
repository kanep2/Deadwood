import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

public class Controller extends MyPanel implements DeadwoodListener {

    private Board board;
    private JLabel endL;
    
    public Controller(Board b) {
	    super();
	    setSize(1600,Deadwood.h);
	     
	    board = b;
	   
	    JButton endBtn = new JButton("End Turn");
	    
	    endBtn.setBounds(Deadwood.x - (Deadwood.x-Deadwood.w)
	    	, 750 
	    	, Deadwood.x-Deadwood.w
	    	, 150);
	    add(endBtn);
		endBtn.addMouseListener(
	  		new MouseAdapter() {
	    		public void mouseClicked(MouseEvent e) {
	      			end();
	    		}
	  		});		
	    
	    /*endL = new JLabel();
	    add(endL);
	    endL.setBounds(Deadwood.w, 0, 1600-Deadwood.w, Deadwood.h);
		endL.addMouseListener(
	  		new MouseAdapter() {
	    		public void mouseClicked(MouseEvent e) {
	      			end();
	    		}
	  		});		    */
		


    }
    public void move(String to){
    	board.player().move(board.getRoom(to));
    }	
	public void work(String r){
		board.player().takeRole(board.getRole(r));  
	}
	public void act(String name){		
		if (board.player().getRoom().getName().equals(name)) {
			board.player().act();  
		}
	}	
	public void rehearse(int id){
		if (board.player().getId() == id) {
			board.player().rehearse();  
		}
	}
	public void upgrade(int level, String type){
        board.player().upgrade(level, type);
	}
	public void end(){
		System.out.println();
        board.endTurn();   
	}
}