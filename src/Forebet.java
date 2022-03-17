import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Forebet {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {
            doc = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-tomorrow").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String userName = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost:3306/bet?characterEncoding=utf8";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = (Connection) DriverManager.getConnection(url, userName, password);
            System.out.println("Database connection established");
        } catch (Exception e) {
            System.err.println("Cannot connect to database server");
        }

        Element table = doc.select("table.schema").first(); //Επιλέγουμε το σωστό table απο το website
        for (Element row : table.select("tr:gt(2)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td > a");
            if(td.eq(0).hasText()){
                Elements td1 = row.select("td > div");
                Elements td2 = row.select("td");
            Statement s = conn.createStatement();
            PreparedStatement preparedStmt = conn.prepareStatement("insert into bet.Forebet (League, Teams, Prob1, ProbX, Prob2, Pick, CorrectScorePick, AvgGoals, Kelly, ProbUnder, ProbOver, PickGoal, HalfProb1, HalfProbX, HalfProb2, PickHT, PickFT, NGprob, GGprob, BTSpick, BTSodds, FINAL_SCORE) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                    " " + " ON DUPLICATE KEY UPDATE " + "League = VALUES(League)," + "Teams = VALUES(Teams),"
                    + "Prob1 = VALUES(Prob1),"  + "ProbX = VALUES(ProbX),"  + "Prob2 = VALUES(Prob2),"  + "Pick = VALUES(Pick),"
                    + "CorrectScorePick = VALUES(CorrectScorePick),"  + "AvgGoals = VALUES(AvgGoals)," + "Kelly = VALUES(Kelly),"  + "ProbUnder = VALUES(ProbUnder),"
                    + "ProbOver = VALUES(ProbOver),"  + "PickGoal = VALUES(PickGoal),"  + "HalfProb1 = VALUES(HalfProb1),"  + "HalfProbX = VALUES(HalfProbX),"  + "HalfProb2 = VALUES(HalfProb2),"
                    + "PickHT = VALUES(PickHT),"  + "PickFT = VALUES(PickFT),"  + "NGprob = VALUES(NGprob),"  + "GGprob = VALUES(GGprob),"  + "BTSpick = VALUES(BTSpick),"
                    + "BTSodds = VALUES(BTSodds),"  + "FINAL_SCORE = VALUES(FINAL_SCORE),"  + "id = LAST_INSERT_ID(id)");
                
                preparedStmt.setString(1, td1.eq(0).text());
                preparedStmt.setString(2, td.eq(0).text());
                preparedStmt.setString(3, td2.eq(1).text());
                preparedStmt.setString(4, td2.eq(2).text());
                preparedStmt.setString(5, td2.eq(3).text());
                preparedStmt.setString(6, td2.eq(4).text());
                preparedStmt.setString(7, td2.eq(5).text());
                preparedStmt.setString(8, td2.eq(6).text());
                preparedStmt.setString(9, td2.eq(9).text());
                preparedStmt.setString(10, "");
                preparedStmt.setString(11, "");
                preparedStmt.setString(12, "");
                preparedStmt.setString(13, "");
                preparedStmt.setString(14, "");
                preparedStmt.setString(15, "");
                preparedStmt.setString(16, "");
                preparedStmt.setString(17, "");
                preparedStmt.setString(18, "");
                preparedStmt.setString(19, "");
                preparedStmt.setString(20, "");
                preparedStmt.setString(21, "");
                preparedStmt.setString(22, "");

            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));
            ResultSet rs = (ResultSet) s.executeQuery("SELECT LAST_INSERT_ID() AS n");
            rs.next();
            rs.getInt(1);

            s.close();

          }
       }
    }
  }

