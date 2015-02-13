import java.util.Date;

public class DataReceived {
	private int personne;
	private int x;
	private int y;
	private Date date;
	
	public DataReceived(int p, int x, int y, Date date){
		this.personne = p;
		this.x = x;
		this.y = y;
		this.date = date;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getId(){
		return this.personne;
	}
	
	public Date getDate(){
		return this.date;
	}
}
