import javax.swing.*;
//created new label so that a string or int can be used to id labels
//and does not display 
class MyPanel extends JPanel {
		
	public MyPanel() {
		super(null);
	    setSize(Deadwood.w,Deadwood.h);
	    setDoubleBuffered(true);	
	    setVisible(true);
		setOpaque(false);
		setFocusable(true);	
	}
}