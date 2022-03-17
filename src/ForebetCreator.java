import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ForebetCreator {
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

        s.executeUpdate("DROP TABLE IF EXISTS ForebetDaily ");

        s.executeUpdate("CREATE TABLE ForebetDaily( "                //Δημιουργία του πίνακα μας
                + " id INT( 11 ) NOT NULL AUTO_INCREMENT ,"
                + " League VARCHAR(45) NOT NULL,"
                + " Teams VARCHAR(45) NOT NULL UNIQUE,"
                + " Date VARCHAR(45) NOT NULL,"
                + " Prob1 VARCHAR(45) NOT NULL,"
                + " ProbX VARCHAR(45) NOT NULL,"
                + " Prob2 VARCHAR(45) NOT NULL,"
                + " Pick VARCHAR(45) NOT NULL,"
                + " SuccessPick VARCHAR(45) NOT NULL,"
                + " CorrectScorePick VARCHAR(45) NOT NULL,"
                + " SuccessCorrectScorePick VARCHAR(45) NOT NULL,"
                + " AvgGoals VARCHAR(45) NOT NULL,"
                + " Kelly VARCHAR(45) NOT NULL,"
                + " ProbUnder VARCHAR(45) NOT NULL,"
                + " ProbOver VARCHAR(45) NOT NULL,"
                + " PickGoal VARCHAR(45) NOT NULL,"
                + " SuccessPickGoal VARCHAR(45) NOT NULL,"
                + " HalfProb1 VARCHAR(45) NOT NULL,"
                + " HalfProbX VARCHAR(45) NOT NULL,"
                + " HalfProb2 VARCHAR(45) NOT NULL,"
                + " PickHT VARCHAR(45) NOT NULL,"
                + " PickFT VARCHAR(45) NOT NULL,"
                + " SuccessPickHT VARCHAR(45) NOT NULL,"
                + " SuccessPickFT VARCHAR(45) NOT NULL,"
                + " NGprob VARCHAR(45) NOT NULL,"
                + " GGprob VARCHAR(45) NOT NULL,"
                + " BTSpick VARCHAR(45) NOT NULL,"
                + " SuccessBTSpick VARCHAR(45) NOT NULL,"
                + " BTSodds VARCHAR(45) NOT NULL,"
                + " HT_FINAL_SCORE VARCHAR(45) NOT NULL,"
                + " FT_HT_FINAL_SCORE VARCHAR(45) NOT NULL,"
                + " PRIMARY KEY (id, Teams) )" );

        System.out.println("Table created.");


    }
}
