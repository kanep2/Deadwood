import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class Board implements Room.BoardListener{
    
    private List<Player> players;
    private List<Room> rooms;        
    private Deck deck; 
    private int current;
    private int day;  
    private int playerCount;

    protected Board(List<Room> r, Deck d, List<Player> p, int pNum) {
        
        rooms = r;
        deck = d;
        players = p;
        current = 0;
        day = 0;
        playerCount = pNum;
    }  
    
    public Player player() { return players.get(current); }
    public List<Room> getRooms() { return rooms; }    
    public List<Player> getPlayers() { return players; }

    public Room getRoom(String roomName) {
        for (Room r : rooms) {
            if (r.getName().equalsIgnoreCase(roomName)) {
                return r; 
            }
        }
        return new Room();
    } 
    public Role getRole(String roleName) {
        for (Room r : rooms) {
            if (r.getRoles() != null) {
                for (Role a : r.getRoles()) {
                    if (a.getName().equalsIgnoreCase(roleName)) {
                        return a;
                    }    
                }
            }
            if (r.getScene() != null) {
                for (Role b : r.getScene().getRoles()) {
                    if (b.getName().equalsIgnoreCase(roleName)) {
                        return b;
                    }                 
                }                 
            }
        }
        return new Role();
    }    
    public void endTurn() {
        players.get(current).resetMoved();
        players.get(current).resetWorked();
        current++;
        if(current >= playerCount) {
            current = 0; 
        }  
        System.out.println("    >Player " + players.get(current).getId() + "'s turn");
    }    
    //looks through all rooms and checks how many still have scenes
    //if one scene is left end day, if all days have been played end game
    public void checkScenes() {
        System.out.println("ALL SYSTEMS GO");
        int scenesLeft = 0;
        for (Room r: rooms) {
            if (r.getScene() != null) {
                scenesLeft++;
            }
        }
        if (scenesLeft == 1) {
            if (day == (4 - ( (playerCount < 4) ?1:0) ) ) {
                endGame();
            } else {
                System.out.println();
                System.out.println("      --Day " + (day+1) + "--");
                endTurn();
                newDay();                
            }
        } 
    }
    // mvoes all players to trailer and wraps ever scene then gives that
    //room a new scene
    public void newDay() {
        day++;
        for (Player p : players) {
            p.goTo(getRoom("trailer"));
        }
        for (Room r : rooms) {
            if (!r.getName().equals("trailer") && !r.getName().equals("office")) {
                if (deck.getScenes().peek() == null) {
                    System.out.print("-Deck is empty");
                } else {
                    if (day > 1) {
                        if (r.getScene() != null) {
                           r.wrapScene();    
                        }       
                    }            
                    Scene s = deck.getScenes().poll();
                    r.setScene(s);
                    for (Role a : r.getRoles()) {
                        a.setScene(s);
                    }
                }
                r.addBoardListener(this);
            }
        }
    }
    public void setListeners() {

    }
    //calculates who had the highest score
    public void endGame() {
        System.out.println("    final day complete");
        System.out.println();
        System.out.println("    scores"); 

        int highscore = 0;
        Player winner = new Player(); 
        for (Player p : players) {
            if (p.getScore() > highscore) {
                winner = p; 
                highscore = p.getScore();
            }
            System.out.println("    player " + p.getId() + ": " + p.getScore());
        }
        System.out.println();
        System.out.println("    player " + winner.getId() + " wins!");
        System.exit(1);
    }

    public static Board build(int pNum) {
        
        if (pNum < 2 || 8 < pNum) {
            System.out.println("must be between 2 and 8 players");
            System.exit(1);
        }
        //build player list and deck
        List<Player> playerList = new LinkedList<Player>();
        for (int i = 1; i <= pNum; i++) {
            playerList.add(Player.build (i, pNum));
        }
        Deck d = Deck.build();
        //parse room nodes and build room list
        List<Room> roomList = new LinkedList<Room>();
        try {
            FileInputStream fin = new FileInputStream("../resources/board.xml");
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fin);
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); ++i) {
                    Node n = nl.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        roomList.add (Room.build ((Element)n));
                    }
                }  
            }
            finally {
                fin.close();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        Board temp = new Board(roomList, d, playerList, pNum);       
        temp.newDay();
        return temp;
    }
}
