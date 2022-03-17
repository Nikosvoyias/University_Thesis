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

public class ForebetHTFT {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-tomorrow/predictions-htft").get();
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
        for (Element row : table.select("tr:gt(1)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td > a");

            if(td.eq(0).hasText()){

                Elements td1 = row.select("td > div");

                Elements td2 = row.select("td");

                Statement s = conn.createStatement();

                PreparedStatement preparedStmt = conn.prepareStatement("update bet.Forebet set HalfProb1 = ?, HalfProbX = ?, HalfProb2 = ?, PickHT = ?, PickFT = ? where League = ? AND Teams = ?");


                preparedStmt.setString(1, td2.eq(1).text());
                preparedStmt.setString(2, td2.eq(2).text());
                preparedStmt.setString(3, td2.eq(3).text());
                preparedStmt.setString(4, td2.eq(4).text());
                preparedStmt.setString(5, td2.eq(5).text());
                preparedStmt.setString(6, td1.eq(0).text());
                preparedStmt.setString(7, td.eq(0).text());

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
