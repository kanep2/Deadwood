import org.w3c.dom.*;
import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Role {


    private int level;	
    private int r;
    private String name;
    private Player player; 
    private Scene scene;
    private int[] area;
    public boolean isStar;

    public Role() {    
    }

    protected Role(String n, String lvl, String[] a, boolean s) {
        
        level = Integer.parseInt(lvl);
        r = 0;
        name = n;    
        area = new int[4];
        area[0] = Integer.parseInt(a[0]);
        area[1] = Integer.parseInt(a[1]);
        area[2] = Integer.parseInt(a[2]);
        area[3] = Integer.parseInt(a[3]);
        isStar = s;
    }

    public int getLevel() { return level; }
    public int getR() { return r; }
    public String getName() { return name; }
    public Player getPlayer() { return player; }
    public Scene getScene() { return scene; }
    public int[] getArea() { return area; }
    public boolean isStar() { return isStar; }
    public boolean isTaken() { return (player != null); }
    public void clearR() { r = 0; } 
    public void clearScene() { scene = null; }
    public void clearPlayer() { player = null; }
    public void setScene(Scene s) { scene = s; }
    public void succeed() {}
    public void fail() {}
    
    public void setPlayer(Player p) { 
        player = p; 
        System.out.println("player " + player.getId() + 
            " is now working role " + name);
    }    
    public boolean rehearse() { 
        if (r < (scene.getBudget() - 1)) {
            r++; 
            System.out.println("player " + player.getId() + 
                " has " + r + " rehearsal counters");   
            return true;
        } else { 
            System.out.println("player " + player.getId() + 
                " has rehearsed the maximum amount possible"); 
            return false;
        }  
    }
    public static Role build(Element n, String type) {
     
    	String roleName = n.getAttribute("name");
        String lvl = n.getAttribute("level");
        String[] areaInfo = new String[4];
        NodeList areaList = n.getElementsByTagName("area");
        Node area = areaList.item(0);
        if (area.getNodeType() == Node.ELEMENT_NODE) {
            Element areaElement = (Element)area;
            areaInfo[0] = areaElement.getAttribute("x");  
            areaInfo[1] = areaElement.getAttribute("y");
            areaInfo[2] = areaElement.getAttribute("w"); 
            areaInfo[3] = areaElement.getAttribute("h"); 
        }        

        switch(type) {

      	case "star": {
            return new Star(roleName, lvl, areaInfo);
      	}      	
        case "extra": {
      	 	 return new Extra(roleName, lvl, areaInfo);
      	} 
        default:
        	  System.out.println("Oops!");
        	  return null;
      	}
    }
}

class Star extends Role{

    public Star(String n, String lvl, String[] a) {
        super (n, lvl, a, true);
    }
    public void succeed() {    
        getPlayer().addCred(2); 
        System.out.println("success! Player " + getPlayer().getId() + 
            " recevies 2 credits");
    } 
    public void fail() {
        System.out.println("attempt failed");
    }
}

class Extra extends Role{
    
    public Extra(String n, String lvl, String[] a) {
        super (n, lvl, a, false);
    }
    public void succeed() {    
        getPlayer().addCred(1);
        getPlayer().addDol(1); 
        System.out.println("success! Player " + 
            getPlayer().getId() + " recevies 1 credit and 1 dollar");
    }
    public void fail() {
        getPlayer().addDol(1);
        System.out.println("attempt failed. Player " + 
            getPlayer().getId() + " recevies 1 dollar");
    }   
}
