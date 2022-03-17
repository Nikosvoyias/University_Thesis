import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class StatCreator {

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
/**
 * Στη συγκεκριμένη κλάση δημιουργούμε το πίνακα που
 * θα φιλοξενήσει τα στοιχεία που θα συλλέγουμε απο το website
 *
 */

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

        s.executeUpdate("DROP TABLE IF EXISTS  Statistics ");

        s.executeUpdate("CREATE TABLE  Statistics  ( "                //Δημιουργία του πίνακα μας
                + " id INT( 11 ) NOT NULL AUTO_INCREMENT ,"
                + " Home VARCHAR(45) NOT NULL,"
                + " Draw VARCHAR(45) NOT NULL,"
                + " Away VARCHAR(45) NOT NULL,"
                + " nGames VARCHAR(45) NOT NULL,"
                + " 1percentage VARCHAR(45) NOT NULL,"
                + " Xpercentage VARCHAR(45) NOT NULL,"
                + " 2percentage VARCHAR(45) NOT NULL,"
                + " Recent VARCHAR(150) NOT NULL,"
                + " 1Delay VARCHAR(45) NOT NULL,"
                + " xDelay VARCHAR(45) NOT NULL,"
                + " 2Delay VARCHAR(45) NOT NULL,"
                + " PRIMARY KEY (id) )" );

        System.out.println("Table created.");
    }
}
