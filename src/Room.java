import org.w3c.dom.*;
import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/**/
public class Room {

    public interface BoardListener {
        public void checkScenes();
    }    
    public interface RoomListener {
        public void update(Room r);
        public void roomReveal(Room r);
    }
    public interface RoleListener {
    //public void update(Role r);
        public void roleReveal(Role r);
    }

    private String name;
    private List<String> adjacent; 
    private int[] area;
    private boolean[] spotTaken;

    public Room() {  
    }

    protected Room(String rname, List<String> adj, String[] a) {
        name = rname;
        adjacent = adj; 
        area = new int[4];
        area[0] = Integer.parseInt(a[0]);
        area[1] = Integer.parseInt(a[1]);
        area[2] = Integer.parseInt(a[2]);
        area[3] = Integer.parseInt(a[3]);
        spotTaken = new boolean[8];
    }
    
    public String getName() { return name; }
    public List<String> getAdjacent() { return adjacent; }
    public int[] getArea() { return area; }
    public Scene getScene() { return null; }
    public int getCost(int r, String type) { return 0; }    
    public List<Role> getRoles() { return null; }
    public Role getRole(String name) { return null; }
    public int[] getTakeSpot() { return null; }
    public boolean isVisible() { return true; }
    public int getT() { return 0; }
    public void takeSuccess() {}
    public void setScene(Scene s) {}
    public void wrapScene() {} 
    public void printInfo() {}  
    public void reveal() {}
    public void addBoardListener(BoardListener l) {}
    public void addRoleListener (RoleListener l) {}
    public void addRoomListener(RoomListener l) {}
    public List<int[]> getUpgrades() { return null; }
    
    //returns the coordinates of the next available
    //spot in the room
    public int[] getSpot() {
        int spot = 0;
        while (spotTaken[spot] == true) {
            spot++;
        }
        spotTaken[spot] = true;
        int[] location = new int[3];
        location[0] = area[0] + (spot * 45);
        location[1] = area[1] + 121;  
        location[2] = spot;     
         System.out.println("@!@#!@#!@#!@#!#!@#!@#");
        for (int i : location) {
            System.out.println(i);
        }
        return location;
    }
    public void clearSpot(int s) {
        spotTaken[s] = false;
    }
    public static Room build(Element n) {
        //parse room neighbors    
        List<String> adjList = new LinkedList<String>();
        String roomName = "";
        NodeList nl = n.getElementsByTagName("neighbor");
        for (int i = 0; i < nl.getLength(); ++i) {
            Node m = nl.item(i);
            if (m.getNodeType() == Node.ELEMENT_NODE) {
                Element x = (Element)m;
                adjList.add (x.getAttribute("name"));
            }
        }
        //parse area
        String[] areaInfo = new String[4];
        NodeList aList = n.getElementsByTagName("area");
        Node area = aList.item(0);
        if (area.getNodeType() == Node.ELEMENT_NODE) {
            Element aElement = (Element)area;
            areaInfo[0] = aElement.getAttribute("x");  
            areaInfo[1] = aElement.getAttribute("y"); 
            areaInfo[2] = aElement.getAttribute("w"); 
            areaInfo[3] = aElement.getAttribute("h");              
        }
        
        switch(n.getNodeName()) {
        case "set": {      
            //parse number of takes
            ArrayList<int[]> tempA = new ArrayList<int[]>();
            NodeList takeList = n.getElementsByTagName("take");
            for (int i = 0; i < takeList.getLength(); i ++) {
                int[] takeInfo = new int[4]; 
                Node take = takeList.item(i);      
                NodeList areaList = take.getChildNodes();
                Node areaNode = areaList.item(0);
                if (areaNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element areaElement = (Element)areaNode;    
                    takeInfo[0] = Integer.parseInt(areaElement.getAttribute("x"));  
                    takeInfo[1] = Integer.parseInt(areaElement.getAttribute("y")); 
                    takeInfo[2] = Integer.parseInt(areaElement.getAttribute("w")); 
                    takeInfo[3] = Integer.parseInt(areaElement.getAttribute("h")); 
                    tempA.add(takeInfo);      
                }                
            }
            int takeNum = tempA.size();
            //parse roles
            List<Role> partList = new LinkedList<Role>();
            NodeList pList = n.getElementsByTagName("part");
            for (int i = 0; i < pList.getLength(); ++i) {
                Node part = pList.item(i);
                if (part.getNodeType() == Node.ELEMENT_NODE) {
                    partList.add (Role.build ((Element)part, "extra"));
                }
            }            
            return new Set(n.getAttribute("name"), adjList, takeNum, partList, areaInfo, tempA);   
        }
        case "trailer": {
            return new Trailer("trailer", adjList, areaInfo);
        }  
        case "office": {
            List<int[]> upInfo = new LinkedList<int[]>();
            NodeList upList = n.getElementsByTagName("upgrade");
            for (int i = 0; i < upList.getLength(); i ++) {
                int[] u = new int[6];
                Node upgrade = upList.item(i);
                if (upgrade.getNodeType() == Node.ELEMENT_NODE) {
                    Element upElement = (Element)upgrade;    
                    String s = upElement.getAttribute("currency");
                    if (s.equals("dollar")) {
                        u[0] = 1;
                    } else {
                        u[0] = 0;
                    }
                    u[1] = Integer.parseInt(upElement.getAttribute("level"));   
                }  
                NodeList areaList = upgrade.getChildNodes();
                Node areaNode = areaList.item(1);               
                if (areaNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element areaElement = (Element)areaNode;    
                    u[2] = Integer.parseInt(areaElement.getAttribute("x"));  
                    u[3] = Integer.parseInt(areaElement.getAttribute("y")); 
                    u[4] = Integer.parseInt(areaElement.getAttribute("w")); 
                    u[5] = Integer.parseInt(areaElement.getAttribute("h"));      
                }                
                upInfo.add(u);
            }
            return new Casting("office", adjList, areaInfo, upInfo);
        }
        default:
            System.out.println("Oops!");
            return null;
        }
    }
}

//******************************************
class Trailer extends Room {
    public Trailer(String rname, List<String> adj, String[] a) {
        super (rname, adj, a);    
    }
    public void printInfo() {
        System.out.println(getName());
    }
}

//******************************************
class Casting extends Room {
    private static int[] dollarCost = new int[] {4,10,18,28,40};
    private static int[] creditCost = new int[] {5,10,15,20,25};
    private List<int[]> upgrades; 
    
    public Casting(String rname, List<String> adj, String[] a, List<int[]> l) {
        super (rname, adj, a);    
        upgrades = l;
    }
    public List<int[]> getUpgrades() { return upgrades; }

    public void printInfo() {
        System.out.println("office");
        System.out.println("    ---Dollar cost to upgrade:");
        for (int i = 0; i < 5; i++) {
            System.out.println("    rank: " + (i+2) + " cost: " + dollarCost[i]);
        }
        System.out.println();
        System.out.println("    ---Credit cost to upgrade:");
        for (int i = 0; i < 5; i++) {
            System.out.println("    rank: " + (i+2) + " cost: " + creditCost[i]);
        }
        System.out.println();
    }
    public int getCost(int r, String type) {
        if (type.equals("dollar")) {
            return dollarCost[r-2];
        }
        else {
            return creditCost[r-2]; 
        }
    }
}

//******************************************
class Set extends Room {

    private Scene scene;
    private int takes;  
    private int t; 
    private List<Role> roles;
    private boolean visible = false;
    private BoardListener bListener;
    private RoomListener roomListener;
    private RoleListener roleListener;
    private ArrayList<int[]> takeArea;

    public Set(String rname 
        ,List<String> adj 
        ,int takeNum 
        ,List<Role> p 
        ,String[] a
        ,ArrayList<int[]> takeInfo) {
        
        super (rname, adj, a);
        takes = takeNum;
        t = takes; 
        roles = p; 
        takeArea = takeInfo;
        //responder = new Responder();
    }
    
    public Scene getScene() { return scene; }
    public List<Role> getRoles() { return roles; }
    public int getTakes() { return takes; }
    public int getT() { return t; }    
    public ArrayList<int[]> getTakeArea() { return takeArea; }
    public boolean isVisible() { return visible; }
    public void addBoardListener (BoardListener l) { bListener = l; }
    public void addRoleListener (RoleListener l) { roleListener = l; }
    public void addRoomListener(RoomListener l) { roomListener = l; }
    public int[] getTakeSpot() { return takeArea.get(t-1); }
    

/*    public void reveal() { 
        visible = true; 
        roomListener.roomReveal(this); 
        for (Role r : scene.getRoles()) {
            roleListener.roleReveal(r);
        }
    }*/
    public void setScene(Scene s) { 
        scene = s; 
        visible = false; 
    }
    //removes shot counter, and wraps scene if last one
    public void takeSuccess() {
        t--;
        if (t < 1) {
            System.out.println(scene.getName() + " has wrapped production");
            reward();
            wrapScene();
            bListener.checkScenes();
        } else {
            roomListener.update(this);
            System.out.println("takes remaining: " + t);
        }
    }
    //removes relevent associates between players roles scenes and rooms
    public void wrapScene() {
        //extra roles
        for (Role r : roles) {
            Player p = r.getPlayer();
            if (p != null) {
                p.clearRole();
                p.setSpot(getSpot()); 
            }            
            r.clearPlayer();
            r.clearScene();
            r.clearR();
        }
        //star roles
        for (Role r : scene.getRoles()) {
            if (r.getPlayer() != null) {
                r.getPlayer().clearRole();
            }
        }      
        t = takes;    
        scene = null;
        roomListener.update(this); 
    }    
    //if there is a player in a staring role pay out extras and stars
    private void reward() { 
        boolean found = false;
        for (Role r : scene.getRoles()) {
            if (r.getPlayer() != null) {
                found = true; 
            }
        }
        if (found) {
            scene.reward();
            System.out.println();
            for (Role r : roles) {
                if (r.getPlayer() != null) {
                    r.getPlayer().addDol(r.getLevel());
                    System.out.println("player " + r.getPlayer().getId() + 
                        " receives $" + r.getLevel());
                }    
            }
        }
    }
    public void printInfo() {
        if (scene != null) {
            System.out.println(getName() + " shooting " + scene.getName() + 
            " scene " + scene.getId());        
            System.out.println("budget: " + scene.getBudget());
            System.out.println("takes left: " + t);  
        } else {
            System.out.println(getName() + " (wrapped)");
        }
    }
}




