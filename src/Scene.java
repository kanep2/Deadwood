import org.w3c.dom.*;
import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.*;

public class Scene {
  	
    public interface Listener {
        public void update();
    } 

    private String name; 
    private int id;  	
    private int budget;
    private List<Role> roles;
    private ImageIcon image;

    protected Scene(String s, String i, String b, List<Role> p, ImageIcon img) {
	name = s;
        id = Integer.parseInt(i);
        budget = Integer.parseInt(b);
        for (Role r : p){
	      r.setScene(this);
        }
        roles = p;	
        image = img;
    }

  	public String getName() { return name; }
  	public int getId() { return id; }
  	public int getBudget() { return budget; }
  	public List<Role> getRoles() { return roles; }
    public ImageIcon getImage() { return image; }

	  //pays out staring roles for completed scene
	  //created list of sorted die rolls
	  //loops through players until all die rewarded
	  public void reward() {
        System.out.println();
        System.out.println("bonus payout");
        System.out.println();
        int j  =  budget;
        
        LinkedList<Integer> rolls = new LinkedList<Integer>();
        for (int i = 0; i < j; i++) {
            int dieRoll = 1 + (int)(Math.random()*6);
            rolls.add(dieRoll);
        }
        Collections.sort(rolls);
        while (rolls.size() > 0) {
            for (Role r : roles) {
                if (rolls.size() > 0) {
                    if (r.getPlayer() != null) {
                        int roll = rolls.pollLast();
                        System.out.println("player " + r.getPlayer().getId() + 
                            " receives $" + roll);
                        r.getPlayer().addDol(roll);                            
                    }
                } 
            }
        }
	  }
	  //parse card node 
	  public static Scene build(Element n) {
    	 	List<Role> p = new LinkedList<Role>();
    		NodeList partList = n.getElementsByTagName("part");
    		for (int i = partList.getLength()-1; i >= 0; i--) {    
    		    Node part = partList.item(i);
    		    if (part.getNodeType() == Node.ELEMENT_NODE) {
    				    p.add (Role.build ((Element)part, "star"));
    			  }  
    		}
        String idString = "0";
        NodeList num = n.getElementsByTagName("scene");
  	  	Node s = num.item(0);
  	  	if (s.getNodeType() == Node.ELEMENT_NODE) {
      			Element tempE = (Element)s;
      			idString = tempE.getAttribute("number");				
  		  }
  	  	return new Scene(n.getAttribute("name"), 
            idString, 
            n.getAttribute("budget"), 
            p,
            Resources.getInstance().getNextScene());
    }
}
