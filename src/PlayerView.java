import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class PlayerView extends MyPanel implements Player.PlayerListener {

	private DeadwoodListener listener;
	private ImageIcon[][] pImages;
	private MyLabel[] players;  

	//gets player images, creates player labels and
	//sets those labs to their rank 1 image and places it
	//in the trailer
	public PlayerView(List<Player> pList) {
		super();

		int i = 0;
		players = new MyLabel[pList.size()];
		pImages = Resources.getInstance().getPlayers();

		for (Player p : pList) {
			final int id = p.getId();
			MyLabel label = new MyLabel(p.getId());
			players[i++] = label;
			add(label);
			//label.setBounds(1020 + (p.getId()*50), 280, 40, 40);		
			int [] spot = p.getRoom().getSpot();
			label.setBounds(spot[0], spot[1], 40, 40);
			label.setIcon(pImages[p.getId()-1][0]);
			label.addMouseListener(
      			new MouseAdapter() {
	        		public void mouseClicked(MouseEvent e) {
	          			System.out.println("yo");
	          			listener.rehearse(id);        		
	        		}
	      		});

			p.addPlayerListener(this); 
		}
	}
	public void addListener(DeadwoodListener l) {
		listener = l;
	}

	public void update(Player p, String type) {
		MyLabel player = players[p.getId()-1];
		if (type.equals("move")) {
			int[] a = p.getSpot();
			player.setBounds(a[0], a[1], 40, 40); 
		} else if (type.equals("work")) {
			int offsetX = 3, offsetY = 3;
			if (p.getRole().isStar()) {
				offsetX = p.getRoom().getArea()[0];
				offsetY = p.getRoom().getArea()[1];
			}
			int roleX = p.getRole().getArea()[0];
			int roleY = p.getRole().getArea()[1];
			player.setBounds(roleX + offsetX, roleY + offsetY, 40, 40);
		} else if (type.equals("upgrade")) {
			player.setIcon(pImages[p.getId()-1][p.getRank()-1]);
		}
	}
}