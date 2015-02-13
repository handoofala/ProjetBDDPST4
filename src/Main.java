import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Statement;

import java.util.Date;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		/* Connexion à la base de données */
		String url = "jdbc:mysql://localhost/handoofala";
		String utilisateur = "handoofala";
		String motDePasse = "azerty";
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultat = null;
		ArrayList<DataReceived> dataReceived = new ArrayList<DataReceived>();
		double distance;
		boolean BDDisEmpty = false;
		int second = 5; //Modifier cette valeur pour modifier l'intervalle de temps de traitement EN SECONDE
				
		try {
			/*Init de la connection*/
		    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
		    System.out.println("Connexion réussie!");
		    statement = (Statement) connexion.createStatement();//Création du statement...
		    System.out.println("Statement create");
		    /* Ici, nous placerons nos requêtes vers la BDD */
		    
		    /* Exécution d'une requête de lecture */
		    resultat = statement.executeQuery("SELECT id_perso, DATE_ADD(datetime, INTERVAL "+second+" SECOND) AS datetimeSup, DATE_SUB(datetime, INTERVAL "+second+" SECOND) AS datetimeInf FROM table1 WHERE id_perso=1;");
		    resultat.next();
		    
		    while(!BDDisEmpty){
			    /*DatetimeSup*/
			    Timestamp timestamp = resultat.getTimestamp("datetimeSup");
			    Date datetimeFirst = new java.util.Date(timestamp.getTime());
			    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    String datetimeOrigin = df.format(datetimeFirst);
			    
			    /*DatetimeInf*/
			    Timestamp timestamp2 = resultat.getTimestamp("datetimeInf");
			    Date datetimeFirst2 = new java.util.Date(timestamp2.getTime());
			    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    String datetimeOrigin2 = df2.format(datetimeFirst2);
			    
			    resultat.close();
			    System.out.println(datetimeOrigin);
			    System.out.println(datetimeOrigin2);
			    
			    resultat = statement.executeQuery( "SELECT * FROM table1 WHERE datetime BETWEEN '"+datetimeOrigin2+"' AND '"+datetimeOrigin+"';" );
			    System.out.println("Request create and executed");
			    		    
			    /* Récupération des données du résultat de la requête de lecture */
			    while ( resultat.next() ) {
			        int idUtilisateur = resultat.getInt( "id_perso" );
			        int x = resultat.getInt( "x" );
			        int y = resultat.getInt( "y" );
			        Date dateTime = resultat.getDate("datetime");
			        DataReceived thisLine = new DataReceived(idUtilisateur, x, y, dateTime); 
			        dataReceived.add(thisLine);
			        System.out.println(idUtilisateur+"::"+x+"::"+y+"::"+dateTime);
			    }
			    resultat.close();
			    while(dataReceived.size() > 1){
			    	DataReceived firstElement = dataReceived.get(0);
			    	for(int compt = 1; compt < dataReceived.size(); compt++){
			    		if(firstElement.getId() != dataReceived.get(compt).getId()){
				    		distance = distanceBetween2(firstElement, dataReceived.get(compt));
				    		int statut = statement.executeUpdate( "INSERT INTO table2 VALUES('"+firstElement.getId()+"','"+dataReceived.get(compt).getId()+"', '"+distance+"', '"+datetimeOrigin+"');" );
				    		if(statut == 1){
				    			System.out.println("Insertion complete");
				    		}
				    		else{
				    			System.out.println("Nothing was added");
				    		}
			    		}
			    	}
			    	dataReceived.remove(0);
			    }
			    System.out.println("Il n'y a plus qu'une seule ligne ! On repasse l'algo pour le datetimeOrigin suivant");
			    resultat = statement.executeQuery("SELECT id_perso, DATE_ADD(datetime, INTERVAL "+second+" SECOND) AS datetimeSup, DATE_SUB(datetime, INTERVAL "+second+" SECOND) AS datetimeInf FROM table1 WHERE datetime = '"+datetimeOrigin+"' LIMIT 1;");

			    if(!resultat.next()){
			    	System.out.println("On n'a plus de données ! on a fini nos calculs");
			    	BDDisEmpty = true;
			    }
			}
		    
		} catch ( SQLException e ) {
			System.out.println(e);
		    /* Gérer les éventuelles erreurs ici */
		} finally {
			/*Connexion close*/
		    if ( connexion != null )
		        try {
		            /* Fermeture de la connexion */
		            connexion.close();
				    System.out.println("Connexion fermée!");
		        } catch ( SQLException ignore ) {
		        	System.out.println(ignore);
		            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
		        }
		    /*Statement close*/
		    if ( statement != null )
		        try {
		            /* Fermeture de la connexion */
		            statement.close();
				    System.out.println("Statement fermée!");
		        } catch ( SQLException ignore ) {
		        	System.out.println(ignore);
		            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
		        }
		    
		    /*Resultat close*/
		    if ( resultat != null )
		        try {
		            /* Fermeture de la connexion */
		            resultat.close();
				    System.out.println("Resultat fermée!");
		        } catch ( SQLException ignore ) {
		        	System.out.println(ignore);
		            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
		        }
		}	
	}
	
	public static double distanceBetween2(DataReceived first, DataReceived second){
		return Math.sqrt(Math.pow((first.getX()-second.getX()), 2)+Math.pow((first.getY()-second.getY()), 2));
	}

}
