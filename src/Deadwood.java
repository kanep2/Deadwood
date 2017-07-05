import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import javax.accessibility.*;


public class Deadwood {
    
    
    public static final int x = 1500, w = 1200, h = 900;
    public static Board board;

    public static void main(String[] args) {
        
        //grab command line argument and parse to int
        int arg1 = 0;
        if (args.length > 0) {
            try {
                arg1 = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("argument " + args[0] + " must be an integer");
                System.exit(1);
            }
        } else {
            System.out.println("must give number of players");   
            System.exit(1);
        }  
        //set up board
        board = Board.build(arg1);    
        //Responder.setBoard(board);

        
        //create controller and views
        Controller controller = new Controller(board);
        RoomView roomview = new RoomView(board.getRooms());
        RoleView roleview = new RoleView(board.getRooms());
        PlayerView playerview = new PlayerView(board.getPlayers());

        roomview.addListener(controller);   
        roleview.addListener(controller);   
        playerview.addListener(controller);

        JFrame window = new JFrame("Deadwood");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(x+2,925);

        window.getLayeredPane().setBorder(BorderFactory.createTitledBorder("Deadwood"));
        window.getLayeredPane().setPreferredSize(new Dimension(w, h));
        window.getLayeredPane().add(controller, new Integer(-1));
        window.getLayeredPane().add(boardPanel(), new Integer(0));
        window.getLayeredPane().add(roomview, new Integer(1));
        window.getLayeredPane().add(roleview, new Integer(2));
        window.getLayeredPane().add(playerview, new Integer(3));


        window.setResizable(false);
        window.setVisible(true);

        System.out.println("    >Player 1's turn");
        System.out.print("> ");
        //parse input from user
        Scanner in = new Scanner(System.in);
        while(!gameOver() && in.hasNext()) {

            String cmd = in.nextLine();
            String[] cmdA = cmd.split(" "); 
            switch(cmdA[0]) {
            case "who": 
                who(); 
                break;
              
            case "where":
                where();
                break;

            case "move": 
                if (cmdA.length > 1) {                  
                    String name = cmdA[1];
                    int i = 2;
                    while (i < cmdA.length) {
                        name = name.concat(" " + cmdA[i]);
                        i++;
                    }
                    move(name);
                } else if (cmdA.length == 1) {
                    System.out.println("adjacent rooms are: ");
                    for (String s : board.player().getRoom().getAdjacent()) {
                        System.out.println("    " + s);
                    }                    
                }              
                break;

            case "work":
                if (cmdA.length > 1) {
                    String s = "";
                    for (int i = 1; i < cmdA.length; i++) {
                        s = s.concat(cmdA[i]);   
                        if (i < cmdA.length-1) {
                            s = s.concat(" ");       
                        }
                    }
                    work(s);
                } 
                //
                else if (cmdA.length == 1) {
                    Room room = board.player().getRoom();
                    if (room.getName().equals("trailer") ||  
                        room.getName().equals("office")) {
                        System.out.println("no roles available");
                    } else if (room.getScene() == null) {
                        System.out.println("room has wrapped"); 
                    } else {
                        System.out.println("    extra roles:");
                        for (Role r : room.getRoles()) {
                            System.out.println("    " + r.getName() +
                                " " + r.getLevel());
                        }
                        System.out.println(); 
                        System.out.println("    star roles:");
                        for (Role r : room.getScene().getRoles()) {
                            System.out.println("    " + r.getName() +
                                " " + r.getLevel());
                        }        
                        System.out.println();
                    }
                }              
                break;

            case "upgrade":
                if (cmdA.length == 3) {
                    String currency = cmdA[1];
                    int level = 0;
                    try {
                        level = Integer.parseInt(cmdA[2]);
                    } catch (NumberFormatException e) {
                        currency = "!";
                    }
                    
                    switch(currency) {
                    case "$":
                        upgrade(level, "dollar");
                        break;
                    case "cr":
                        upgrade(level, "credit");
                        break;
                    default:
                        System.out.println("not a valid entry");
                    }                    
                } else {
                    System.out.println("not a valid entry");
                }

                break;

            case "act":
                if (cmdA.length == 1) {
                   act(); 
                } else {
                    System.out.println("not a valid entry");
                }
                break;
            
            case "rehearse":
                if (cmdA.length == 1) {
                    rehearse();
                } else {
                    System.out.println("not a valid entry");
                }                
                break;

            case "end":
                if (cmdA.length == 1) {
                    end();
                } else {
                    System.out.println("not a valid entry");
                }                  
                break;

            case "quit":
                System.exit(0);
                break;

            default:
                System.out.println("Unknown command \""+cmd+"\"");        
            }          
            System.out.print("> ");
        }    
    }
    
    public static JPanel boardPanel() {
        MyPanel panel = new MyPanel();  
        ImageIcon image = Resources.getInstance().getBoard();
        JLabel label = new JLabel(image, JLabel.CENTER);
        panel.add(label);    
        label.setBounds(0,0,1200,900);
        return panel;
    }

    public static boolean gameOver() {
        return false;    
    }
    public static void who() {
        board.player().who();
    }
    public static void where() { 
        board.player().getRoom().printInfo();
    }
    public static void move(String to) {
        board.player().move(board.getRoom(to));         
    }
    public static void work(String r) {
        board.player().takeRole(board.getRole(r));               
    }
    public static void upgrade(int level, String type) {
        board.player().upgrade(level, type);
    }   
    public static void act() {
        board.player().act();
    }    
    public static void rehearse() {
        board.player().rehearse();  
    }
    public static void end() {
        System.out.println();
        board.endTurn();    
    }
}
