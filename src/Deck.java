import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

// This is a demonstration of reading from an XML document.  
public class Deck {
    
    private Queue<Scene> scenes;

    protected Deck(Queue<Scene> q) {
        scenes = q;
    }  

    public Queue<Scene> getScenes() { return scenes; }
    public Scene nextCard() { return scenes.poll(); }
    
    public static Deck build() {
        ArrayList<Scene> s = new ArrayList<Scene>();
        Queue<Scene> q = new LinkedList<Scene>();
        try {
           
            //create scene list
            FileInputStream fin = new FileInputStream("../resources/cards.xml");
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
                        s.add (Scene.build ((Element)n));
                    }
                }
                //put list into a randomly ordered queue
                Random rand = new Random();
                while (s.size() > 0) {
                    int j = rand.nextInt(s.size());  
                    q.add(s.get(j));
                    s.remove(j);
                }
            }
            finally {
                fin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Deck(q);
    }
}
