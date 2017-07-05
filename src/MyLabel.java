import javax.swing.*;
//created new label so that a string or int can be used to id labels
//and does not display 
class MyLabel extends JLabel {
	
	private String name; 
	private int id;
	private int w, h, x, y;
	
	public MyLabel(int i) {
		super();
		id = i;
	}		
	public MyLabel(String n) {
		super();
		name = n;
	}
	public MyLabel(String n, int width, int height) {
		super();
		name = n;
		w = width;
		h = height;
	}	
	public MyLabel(String n, int xCo, int yCo, int width, int height) {
		super();
		name = n;
		x = xCo;
		y = yCo;
		w = width;
		h = height;
	}	
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getW() {
		return w;
	}
	public int getH() {
		return h;
	}

}
