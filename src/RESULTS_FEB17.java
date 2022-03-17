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


public class RESULTS_FEB17 {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;
        Document doc1 = null;
        Document doc2 = null;
        Document doc3 = null;

        try {

            doc = Jsoup.connect("http://www.forebet.com/en/football-predictions-from-yesterday").get();
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
                Elements td10 = row.select("td.bigOnly > a.odds");

                Statement s = conn.createStatement();


                PreparedStatement preparedStmt = conn.prepareStatement("update bet.ForebetDaily set aftOddsPick1x2 = ?, SuccessPick = ?, SuccessCorrectScorePick = ?, HT_FINAL_SCORE = ?, FT_HT_FINAL_SCORE = ?  where Teams = ?");

                preparedStmt.setString(1, td10.eq(0).text());
                preparedStmt.setString(2, td4.eq(0).text());
                preparedStmt.setString(3, td6.eq(0).text());
                preparedStmt.setString(4, td8.eq(0).text());
                preparedStmt.setString(5, td9.eq(0).text());
                preparedStmt.setString(6, td.eq(0).text());

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
