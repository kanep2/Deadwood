import javax.swing.*;

public class Player {
	
    public interface PlayerListener {
        public void update(Player p, String type);
    }

	private int id;
	private int rank;
	private int credits;
	private int dollars; 
    private Role role;
    private Room room;
    private int[] spot;
    private boolean moved;
    private boolean worked;
    private PlayerListener playerListener;

    public Player() { id = 0; }	

    private Player(int pId, int r, int cred) {
		
		id = pId;
        rank = r;    
/*        credits = cred;
        dollars = 0; */
        credits = 100;
        dollars = 100;
        moved = false;
        worked = false;
        spot = new int[3];
        spot[2] = id-1;
    }
    
    public int getId() { return id; }
    public int getRank() { return rank; }    
    public int getCredits() { return credits; }
    public int getDollars() { return dollars; }    
    public boolean getWorked() { return worked; }
    public Role getRole() { return role; }
    public Room getRoom() { return room; }
    public int[] getSpot() { return spot; }
    public void setRank(int newRank) { rank = newRank; }
    public void addCred(int amount) { credits += amount; }    
    public void addDol(int amount) { dollars += amount; }
    public void clearRole() { role = null; }
    public void resetMoved() { moved = false; }     
    public void resetWorked() { worked = false; }
    public void hasMoved() { moved = true; }
    public void hasWorked() { worked = true; }    
    public void goTo(Room dest) { room = dest; }
    public int getScore() { return (dollars + credits + (5 * rank)); }
    public void addPlayerListener(PlayerListener l) { playerListener = l; }
    public void setSpot(int[] s) { spot = s; playerListener.update(this, "move"); }  

    public void who() {
        System.out.println("player " + id + " rank: " + 
            rank + " ($" + dollars + ", " + 
            credits + "cr)"); 
        if (role != null) {
            System.out.println("role: " + role.getName() + 
                " level: " + role.getLevel() +
                " rehearsal: " + role.getR());
        }        
    }
    public void move(Room dest) {
        if (canMove(dest)) {
            room.clearSpot(spot[2]);
            room = dest;
            setSpot(room.getSpot());
            moved = true;
            if (!room.isVisible()) {
                room.reveal();
            }
            System.out.println("player " + id + " has moved to " + room.getName());            
        }        
    }     
    public void takeRole(Role r) {
        if (canTakeRole(r)) {
            System.out.println(spot[2]);
            room.clearSpot(spot[2]);
            role = r; 
            r.setPlayer(this);
            worked = true;            
            playerListener.update(this, "work");
        }       
    }
    public void rehearse() {
        if (canWork()) {
            if (role.rehearse()) {
                worked = true;
            } 
        }
    }
    public void act() {
        if (canWork()) {   
            if (dieRoll()) {
                role.succeed();
                worked = true;  
                room.takeSuccess();
            } else {
                role.fail();
                worked = true;  
            }         
        }
    }
    public boolean canMove(Room dest) {
        if (!worked && role == null) {
            if (!moved) {
                if (room.getAdjacent().contains(dest.getName())) {
                    return true;
                } else { System.out.println("room is not adjacent to current location"); }
            } else { System.out.println("player has already moved this turn"); }            
        } else { System.out.println("cannot move during shooting"); }        
        return false;
    }
    public boolean canTakeRole(Role r) {
        if (role == null) {
            if (r.getScene() != null) {
                if (r.getScene() == room.getScene()) {
                    if (!r.isTaken()) {
                        if (r.getLevel() <= rank) { 
                            return true;
                        } else { System.out.println("player " + id + " is not a high enough rank for role"); }
                    } else { System.out.println("role is already taken"); }                        
                } else { System.out.println("role not in current room"); }                 
            } else { System.out.println("no active scene found"); } 
        } else { System.out.println("player " + id + " is already in a role"); }
        return false;
    }     
    public boolean canWork() {
        if (!worked)  {
            if (role != null) {
                return true;     
            } else { System.out.println("player " + id + " is not in a role"); }              
        } else { System.out.println("player has already worked this turn"); }        
        return false;
    }
    public boolean dieRoll() {
        int  dieRoll = 1 + (int)(Math.random()*6);
        return (dieRoll + role.getR() >= role.getScene().getBudget());
    }
    public boolean pay(int amount, String type) {
        if (type.equals("dollar")) {
            if (dollars >= amount) {
                dollars -= amount;
                return true;
            }         
        } else {
            if (credits >= amount) {
                credits -= amount;
                return true;
            }   
        }
        return false;
    }
    //upgrade using dollars
    public void upgrade(int r, String type) {
        if (room.getName().equals("office")) {    
                if (rank < r && r < 7) {
                    if (  pay(room.getCost(r, type), type )){
                        setRank(r);
                        System.out.println("player " + id + " upgraded to rank " + rank);
                    } else { System.out.println("player does not have enough dollars for that upgrade "); }                 
                } else { System.out.println("not a valid upgrade"); }      
        } else { System.out.println("not in office"); }        
    }
    //builds player differently based on the total number of players
	public static Player build(int id, int players){
	    
	    switch(players) {
 
        case 2:       
        case 3: 
        case 4:{   
            return new Player(id, 1, 0);   
        }
        case 5: {
            return new Player(id, 1, 2); 
        }
        case 6: {
            return new Player(id, 1, 4);    
        }
        case 7:       
        case 8: {   
            return new Player(id, 2, 0);   
        }
        default:
            System.out.println("Oops!");
            return null;
        }
	} 
}