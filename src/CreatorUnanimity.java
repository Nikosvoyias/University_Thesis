import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class CreatorUnanimity {

    public static void main(String[] args) throws SQLException {
        Connection conn = null;


        try
        {
            String userName = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost:3306/bet?characterEncoding=utf8";         // σύνδεση με το localhost
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = (Connection) DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }

        Statement s = conn.createStatement ();

        s.executeUpdate("DROP TABLE IF EXISTS  UnanimityFinal ");

        s.executeUpdate("CREATE TABLE  UnanimityFinal  ( "                //Δημιουργία του πίνακα μας
                + " id INT( 11 ) NOT NULL AUTO_INCREMENT ,"
                + " Code VARCHAR(45) NOT NULL,"
                + " Team1 VARCHAR(45) NOT NULL,"
                + " Team2 VARCHAR(45) NOT NULL,"
                + " CONSTRAINT Teams UNIQUE(Team1, Team2),"
                + " HomeOdd VARCHAR(45) NOT NULL,"
                + " DrawOdd VARCHAR(45) NOT NULL,"
                + " AwayOdd VARCHAR(45) NOT NULL,"
                + " SuggestedPick VARCHAR(45) NOT NULL,"
                + " HT_FT VARCHAR(45) NOT NULL,"
                + " HT VARCHAR(45) NOT NULL,"
                + " FT VARCHAR(45) NOT NULL,"
                + " PRIMARY KEY (id) )" );


        System.out.println("Table created.");


    }
}
