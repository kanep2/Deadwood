import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class RoleView extends MyPanel implements Room.RoleListener {

	private DeadwoodListener listener;
	private ArrayList<MyLabel> labels;

	public RoleView(List<Room> rooms) {
		super();
	
		labels = new ArrayList<MyLabel>();
		//create labels for roles
		for (Room r : rooms) {
			String n = r.getName();
			if (!n.equals("office") && !n.equals("trailer")) {
				//extra roles
				for (Role role : r.getRoles()) {
					final String extraName = role.getName();
					MyLabel extraLabel = new MyLabel(extraName);
					add(extraLabel);
					labels.add(extraLabel);
					extraLabel.setBounds(role.getArea()[0]
						,role.getArea()[1]
						,role.getArea()[2]
						,role.getArea()[3]);
					extraLabel.addMouseListener(
		      			new MouseAdapter() {
			        		public void mouseClicked(MouseEvent e) {
			          			listener.work(extraName);
			        		}
			      		});				 
				}				
				//starring roles
				for (Role role : r.getScene().getRoles()) {
					final String starName = role.getName();
					MyLabel starLabel = new MyLabel(starName);
						/*,role.getArea()[0]
						,role.getArea()[1]
						,role.getArea()[2]
						,role.getArea()[3]);*/
					add(starLabel);
					starLabel.setBounds(role.getArea()[0] + r.getArea()[0]
						,role.getArea()[1] + r.getArea()[1]
						,0
						,0);	
						/*,role.getArea()[2]
						,role.getArea()[3]);*/
					starLabel.addMouseListener(
		      			new MouseAdapter() {
			        		public void mouseClicked(MouseEvent e) {
			          			listener.work(starName);
			        		}
			      		});					
				}
				r.addRoleListener(this);
			}
		}
	}
	
	public void addListener(DeadwoodListener l) {
		listener = l;
	}
	private MyLabel getLabel(String name) {
		for (MyLabel j : labels) {
			if (j.getText().equals(name)) {
				return j;
			}
		}
		return null;
	}
	//public void update(Role r) {

	//}
	public void roleReveal(Role r) {
		MyLabel label = getLabel(r.getName());
		int x = label.getX();
		int y = label.getY();
		label.setBounds(x
			, y
			, label.getW()
			, label.getH());
	}

}