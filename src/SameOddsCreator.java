import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SameOddsCreator {

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

        s.executeUpdate("DROP TABLE IF EXISTS  Compare1vs2 ");

        s.executeUpdate("CREATE TABLE  Compare1vs2 ( "                //Δημιουργία του πίνακα μας
                + " id INT( 11 ) NOT NULL AUTO_INCREMENT ,"
                + " HomeOdd VARCHAR(45) NOT NULL,"
                + " HomeN VARCHAR(45) NOT NULL,"
                + " 1Percentage VARCHAR(45) NOT NULL,"
                + " AwayOdd VARCHAR(45) NOT NULL,"
                + " AwayN VARCHAR(45) NOT NULL,"
                + " 2Percentage VARCHAR(45) NOT NULL,"
                + " PRIMARY KEY (id) )" );


        System.out.println("Table created.");

    }
}
