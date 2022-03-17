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

public class ForebetDAILYJAN17 {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;
        Document doc1 = null;
        Document doc2 = null;
        Document doc3 = null;

        try {

            doc = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-today").get();
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
        for (Element row : table.select("tr:gt(2)").not("tr#gfinished")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td > a");

            if(td.eq(0).hasText()){

                Elements td1 = row.select("td > div");
                Elements td2 = row.select("td");

                Elements td3 = row.select("td.tnms > span");

                Elements td4 = row.select("td.predict_y");

                Elements td6 = row.select("td.exact_yes.tabonly.scrpred");

                Elements td8 = row.select("td.lscr_td.lResTdSmall > span.ht_scr");

                Elements td9 = row.select("td.lscr_td.lResTdSmall > span.lscrsp");
                Elements td10 = row.select("td.bigOnly > span.odds2");

                Statement s = conn.createStatement();

                PreparedStatement preparedStmt = conn.prepareStatement("insert into bet.ForebetDaily (League, Teams, Date, Prob1, ProbX, Prob2, Pick, preOddsPick1x2, aftOddsPick1x2, SuccessPick, CorrectScorePick, SuccessCorrectScorePick,  AvgGoals, ProbUnder, ProbOver, PickGoal, preOddsUO, aftOddsUO, SuccessPickGoal, HalfProb1, HalfProbX, HalfProb2, PickHT,  PickFT, preOddsHtFt, aftOddsHtFt, SuccessPickHT, SuccessPickFT, NGprob, GGprob, BTSpick, SuccessBTSpick, BTSodds, HT_FINAL_SCORE, FT_HT_FINAL_SCORE) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" +
                        " " + " ON DUPLICATE KEY UPDATE " + "League = VALUES(League)," + "Teams = VALUES(Teams)," + "Date = VALUES(Date),"
                        + "Prob1 = VALUES(Prob1),"  + "ProbX = VALUES(ProbX),"  + "Prob2 = VALUES(Prob2),"  + "Pick = VALUES(Pick)," + "preOddsPick1x2 = VALUES(preOddsPick1x2),"+ "aftOddsPick1x2 = VALUES(aftOddsPick1x2)," + "SuccessPick = VALUES(SuccessPick),"
                        + "CorrectScorePick = VALUES(CorrectScorePick)," + "SuccessCorrectScorePick = VALUES(SuccessCorrectScorePick)," + "AvgGoals = VALUES(AvgGoals),"  + "ProbUnder = VALUES(ProbUnder),"
                        + "ProbOver = VALUES(ProbOver),"  + "PickGoal = VALUES(PickGoal)," + "preOddsUO = VALUES(preOddsUO),"+ "aftOddsUO = VALUES(aftOddsUO)," +  "SuccessPickGoal = VALUES(SuccessPickGoal)," + "HalfProb1 = VALUES(HalfProb1),"  + "HalfProbX = VALUES(HalfProbX),"  + "HalfProb2 = VALUES(HalfProb2),"
                        + "PickHT = VALUES(PickHT),"  + "PickFT = VALUES(PickFT)," + "preOddsHtFt = VALUES(preOddsHtFt)," + "aftOddsHtFt = VALUES(aftOddsHtFt)," +"SuccessPickHT = VALUES(SuccessPickHT)," + "SuccessPickFT = VALUES(SuccessPickFT)," + "NGprob = VALUES(NGprob)," + "GGprob = VALUES(GGprob),"  + "BTSpick = VALUES(BTSpick),"  + "SuccessBTSpick = VALUES(SuccessBTSpick),"
                        + "BTSodds = VALUES(BTSodds),"  + "HT_FINAL_SCORE = VALUES(HT_FINAL_SCORE),"  + "FT_HT_FINAL_SCORE = VALUES(FT_HT_FINAL_SCORE),"  + "id = LAST_INSERT_ID(id)" );


                preparedStmt.setString(1, td1.eq(0).text());
                preparedStmt.setString(2, td.eq(0).text());
                preparedStmt.setString(3, td3.eq(0).text());
                preparedStmt.setString(4, td2.eq(1).text());
                preparedStmt.setString(5, td2.eq(2).text());
                preparedStmt.setString(6, td2.eq(3).text());
                preparedStmt.setString(7, td2.eq(4).text());
                preparedStmt.setString(8, td10.eq(0).text());
                preparedStmt.setString(9, "-");
                preparedStmt.setString(10, td4.eq(0).text());
                preparedStmt.setString(11, td2.eq(5).text());
                preparedStmt.setString(12, td6.eq(0).text());
                preparedStmt.setString(13, td2.eq(6).text());

                preparedStmt.setString(14, "");
                preparedStmt.setString(15, "");
                preparedStmt.setString(16, "");
                preparedStmt.setString(17, "");
                preparedStmt.setString(18, "");
                preparedStmt.setString(19, "");
                preparedStmt.setString(20, "");
                preparedStmt.setString(21, "");
                preparedStmt.setString(22, "");
                preparedStmt.setString(23, "");
                preparedStmt.setString(24, "");
                preparedStmt.setString(25, "");
                preparedStmt.setString(26, "");
                preparedStmt.setString(27, "");
                preparedStmt.setString(28, "");
                preparedStmt.setString(29, "");
                preparedStmt.setString(30, "");
                preparedStmt.setString(31, "");
                preparedStmt.setString(32, "");
                preparedStmt.setString(33, "");
                preparedStmt.setString(34, td8.eq(0).text());
                preparedStmt.setString(35, td9.eq(0).text());


                int euReturnValue = preparedStmt.executeUpdate();

                System.out.println(String.format("executeUpdate returned %d", euReturnValue));
                ResultSet rs = (ResultSet) s.executeQuery("SELECT LAST_INSERT_ID() AS n");
                rs.next();
                rs.getInt(1);

                s.close();

            }

        }

        try {

            doc1 = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-today/predictions-under-over-goals").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table1 = doc1.select("table.schema").first(); //Επιλέγουμε το σωστό table απο το website
        for (Element row : table1.select("tr:gt(1)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td > a");


            if (td.eq(0).hasText()) {

                Elements td2 = row.select("td");

                Elements td4 = row.select("td.predict_y");
                Elements td10 = row.select("td.bigOnly > a.odds2");

                Statement p = conn.createStatement();


                PreparedStatement preparedStmt1 = conn.prepareStatement("update bet.ForebetDaily set ProbUnder = ?, ProbOver = ?, PickGoal = ?, preOddsUO = ? , SuccessPickGoal = ? where Teams = ?");


                preparedStmt1.setString(1, td2.eq(1).text());
                preparedStmt1.setString(2, td2.eq(2).text());
                preparedStmt1.setString(3, td2.eq(3).text());
                preparedStmt1.setString(4, td10.eq(0).text());
                preparedStmt1.setString(5, td4.eq(0).text());
                preparedStmt1.setString(6, td.eq(0).text());

                int euReturnValue = preparedStmt1.executeUpdate();

                System.out.println(String.format("executeUpdate returned %d", euReturnValue));
                ResultSet rs = (ResultSet) p.executeQuery("SELECT LAST_INSERT_ID() AS n");
                rs.next();
                rs.getInt(1);

                p.close();

            }
        }

        try {

            doc2 = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-today/predictions-ht-ft").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table2 = doc2.select("table.schema").first(); //Επιλέγουμε το σωστό table απο το website
        for (Element row : table2.select("tr:gt(1)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td > a");


            if(td.eq(0).hasText()){


                Elements td2 = row.select("td");

                Elements td4 = row.select("td.predict_y");
                Elements td10 = row.select("td.bigOnly > a.odds2");


                Statement d = conn.createStatement();

                PreparedStatement preparedStmt2 = conn.prepareStatement("update bet.ForebetDaily set HalfProb1 = ?, HalfProbX = ?, HalfProb2 = ?, PickHT = ?, PickFT = ?, preOddsHtFt = ?, SuccessPickHT = ?, SuccessPickFT = ?  where Teams = ?");

                preparedStmt2.setString(1, td2.eq(1).text());
                preparedStmt2.setString(2, td2.eq(2).text());
                preparedStmt2.setString(3, td2.eq(3).text());
                preparedStmt2.setString(4, td2.eq(4).text());
                preparedStmt2.setString(5, td2.eq(5).text());
                preparedStmt2.setString(6, td10.eq(0).text());
                preparedStmt2.setString(7, td4.eq(0).text());
                preparedStmt2.setString(8, td4.eq(1).text());
                preparedStmt2.setString(9, td.eq(0).text());


                int euReturnValue = preparedStmt2.executeUpdate();

                System.out.println(String.format("executeUpdate returned %d", euReturnValue));
                ResultSet rs = (ResultSet) d.executeQuery("SELECT LAST_INSERT_ID() AS n");
                rs.next();
                rs.getInt(1);

                d.close();

            }

        }

        try {

            doc3 = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-today/predictions-both-to-score").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table3 = doc3.select("table.schema").first(); //Επιλέγουμε το σωστό table απο το website
        for (Element row : table3.select("tr:gt(1)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td > a");


            if(td.eq(0).hasText()){

                Elements td2 = row.select("td");
                Elements td4 = row.select("td.predict_y");


                Statement a = conn.createStatement();


                PreparedStatement preparedStmt3 = conn.prepareStatement("update bet.ForebetDaily set NGprob = ?, GGprob = ?, BTSpick = ?, SuccessBTSpick = ?, BTSodds = ? where Teams = ?");

                preparedStmt3.setString(1, td2.eq(1).text());
                preparedStmt3.setString(2, td2.eq(2).text());
                preparedStmt3.setString(3, td2.eq(3).text());
                preparedStmt3.setString(4, td4.eq(0).text());
                preparedStmt3.setString(5, td2.eq(4).text());
                preparedStmt3.setString(6, td.eq(0).text());

                int euReturnValue = preparedStmt3.executeUpdate();

                System.out.println(String.format("executeUpdate returned %d", euReturnValue));
                ResultSet rs = (ResultSet) a.executeQuery("SELECT LAST_INSERT_ID() AS n");
                rs.next();
                rs.getInt(1);

                a.close();
            }
        }
    }
}