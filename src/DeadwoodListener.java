
//interface implemented by the controller
//every click the user gives becomes one of the calls
public interface DeadwoodListener {
	public void move(String to);
	public void work(String r);
	public void act(String name);
	public void rehearse(int id);
	public void upgrade(int level, String type);
	public void end();
}