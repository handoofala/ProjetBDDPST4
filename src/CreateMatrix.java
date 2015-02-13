import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class CreateMatrix {
	
	int nbr_teste;
	int tab[][]	;
	String url = "jdbc:mysql://localhost/handoofala";
	String utilisateur = "handoofala";
	String motDePasse = "azerty";
	Statement statement = null;
	ResultSet resultat = null;
	Connection connexion = null;
	ArrayList<Distancebet> liste;
	
	public CreateMatrix(int nbr){
		this.nbr_teste=nbr;
		tab=new int[nbr_teste][nbr_teste];
		liste=new ArrayList<Distancebet>();
	}
	
	public void remplitab(){
		try {
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
			resultat = statement.executeQuery("SELECT id_p1, id_p2, distance FROM table2;");
			while(resultat.next()){
				Distancebet distBet = new Distancebet(resultat.getInt("id_p1"), resultat.getInt("id_p2"), resultat.getDouble("distance"));
				liste.add(distBet);
			}
			System.out.println("All table added");
		}
		catch(SQLException e){
			System.out.println(e);
		}
	}
	
	public void analyse(){
		
	}
}