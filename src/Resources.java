import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;


public class Resources {

    private ImageIcon board; 
    private ImageIcon cardBack;
    private ImageIcon[] scenes;
    private ImageIcon shot;
    private int sPos = 0;
    private ImageIcon[][] players;
    private int pPos = 0;
    static Resources instance;


    private Resources() {
        

        scenes = new ImageIcon[40]; 
        players = new ImageIcon[8][6]; 
        try {
            //get board image
            board = new ImageIcon (
                ImageIO.read(
                new File("../resources/board.jpg")));       
            //get card images
            for(int i=0; i < 40; i++) {
                if (i < 9) {
                    scenes[i] = new ImageIcon (
                        ImageIO.read(
                        new File(String.format("../resources/cards/0%d.png", i+1))));
                } else {
                    scenes[i] = new ImageIcon (
                        ImageIO.read(
                        new File(String.format("../resources/cards/%d.png", i+1))));                    
                }
            }
            //get card back
            cardBack = new ImageIcon (
                ImageIO.read(
                new File("../resources/cardback.png"))); 
            //get shot back
            shot = new ImageIcon (
                ImageIO.read(
                new File("../resources/shot.png")));             
            //get dice images
            String[] s = new String[] {"b", "c", "g", "o", "p", "r", "v", "y"};
            for(int i= 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    players[i][j] = new ImageIcon (
                        ImageIO.read(
                        new File(String.format("../resources/dice/%s%d.png", s[i] , j+1 ))));        
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }     
    }

    public ImageIcon getBoard() {
        return board;
    }
    public ImageIcon getNextScene() {
        return scenes[sPos++];
    }
    public ImageIcon getCardBack() {
        return cardBack;
    }
    public ImageIcon getShot() {
        return shot;
    }
    public ImageIcon[][] getPlayers() {
        return players;
    }
    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();          
        }        
        return instance;
    }
}
