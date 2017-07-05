import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class RoomView extends MyPanel implements Room.RoomListener {

	private DeadwoodListener listener;
	private ArrayList<MyLabel> rooms;	
	private ArrayList<MyLabel> takes;

	public RoomView(List<Room> rs) {
		super();

		rooms = new ArrayList<MyLabel>();	
		takes = new ArrayList<MyLabel>();	
		Resources res = Resources.getInstance();
		//create labels for scenes takes and upgrades
		for (Room r : rs) {
			final String name = r.getName();
			//upgrades 	
			if (name.equals("office")) {
				for (int[] i : r.getUpgrades()) {
					JLabel upgradeLabel = new JLabel();
					add(upgradeLabel);
					upgradeLabel.setBounds(i[2] , i[3] , i[4] , i[5]);		
					final String type;
					if(i[0] == 1) {
						type = "dollar";
					} else {
						type = "credit";
					}
					final int level = i[1];
					upgradeLabel.addMouseListener(
		      			new MouseAdapter() {
			        		public void mouseClicked(MouseEvent e) {
			          			listener.upgrade(level, type);
			        		}
			      		});
				} 	
			}			
			//scenes
			System.out.println(name);
			MyLabel roomLabel = new MyLabel(name);
			add(roomLabel);
			rooms.add(roomLabel);
			System.out.println("! ! ! !");
			System.out.println(r.getArea()[0] + " " 
				+ r.getArea()[1] + " " 
				+ r.getArea()[2] + " " 
				+ r.getArea()[3] + " ");
			System.out.println("! ! ! !");	
			roomLabel.setBounds(r.getArea()[0], r.getArea()[1]
				, r.getArea()[2], r.getArea()[3]);
			if (r.getScene() != null) {
				roomLabel.setIcon(r.getScene().getImage());			
				roomLabel.setDisabledIcon(Resources.getInstance().getCardBack());
				roomLabel.setEnabled(false);
			}			
			roomLabel.addMouseListener(
      			new MouseAdapter() {
	        		public void mouseClicked(MouseEvent e) {
	          			listener.move(name);
	        		}
	      		});
			//takes
			MyLabel takeLabel = new MyLabel(name);
			add(takeLabel);
			takes.add(takeLabel);	
			if (r.getScene() != null) {
				takeLabel.setBounds(r.getTakeSpot()[0]
					,r.getTakeSpot()[1]
					,47
					,47);				
			}
			takeLabel.setIcon(res.getShot());
			takeLabel.addMouseListener(
      			new MouseAdapter() {
	        		public void mouseClicked(MouseEvent e) {
	          			listener.act(name);
	        		}
	      		});
			//add listener to rooms
			r.addRoomListener(this); 			
		}
	}
	
	public void addListener(DeadwoodListener l) {
		listener = l;
	}
	private MyLabel getRoom(String name) {
		for (MyLabel j : rooms) {
			if (j.getName().equals(name)) {
				return j;
			}
		}
		return null;
	}
	private MyLabel getTake(String name) {
		for (MyLabel j : takes) {
			if (j.getName().equals(name)) {
				return j;
			}
		}
		return null;
	}
	public void update(Room r) {
		MyLabel room = getRoom(r.getName());
		MyLabel take = getTake(r.getName());
		if (r.getScene() != null) {
			System.out.println( "yo???");
			room.setIcon(r.getScene().getImage());			
			take.setBounds(r.getTakeSpot()[0]
				,r.getTakeSpot()[1]
				,47
				,47); 		
		} else {
			System.out.println( "ummmmmmm");
			room.setIcon(null);
			take.setBounds(0,0,0,0);
		}
	}
	public void roomReveal(Room r) {
		MyLabel room = getRoom(r.getName());
		room.setEnabled(true);
	}
}