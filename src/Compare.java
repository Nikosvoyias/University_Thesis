import com.mysql.jdbc.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class Compare {

    public static void main(String[] args) throws SQLException {

            Connection conn = null;
            Document doc = null;


            try {

                doc = Jsoup.connect("http://www.betonews.com/table.asp?lang=en&tp=3062").get();  //πραγματοποιούμε τη σύνδεση με το website που θελουμε
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String userName = "root";
                String password = "root";
                String url = "jdbc:mysql://localhost:3306/bet?characterEncoding=utf8";    // σύνδεση με το localhost
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = (Connection) DriverManager.getConnection(url, userName, password);
                System.out.println("Database connection established");
            } catch (Exception e) {
                System.err.println("Cannot connect to database server");
            }


            Element table = doc.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]").first(); //Επιλέγουμε το σωστό table απο το website
            for (Element row : table.select("tr:gt(0)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
                Elements td = row.select("td");
                Statement s = conn.createStatement();

                //μεσω της preparedstatement δηλωνουμε οτι θα κανουμε update στο πεδιο HT
                PreparedStatement preparedStmt = conn.prepareStatement("update bet.BetOddsResults set HT = ? where Team1 = ? AND Code = ?");

                //το πραγματοποιούμε μεσω της td.eq().text() που βρηκαμε οτι αντιστοιχεί στα πεδία που θέλουμε

                preparedStmt.setString(1, td.eq(18).text());

                preparedStmt.setString(2, td.eq(6).text());
                preparedStmt.setString(3, td.eq(2).text());
                preparedStmt.executeUpdate();

                int euReturnValue = preparedStmt.executeUpdate();

                System.out.println(String.format("executeUpdate returned %d", euReturnValue));


                s.close();


                Statement p = conn.createStatement();
                //μεσω της preparedstatement δηλωνουμε οτι θα κανουμε update στο πεδιο FT

                PreparedStatement preparedStmt1 = conn.prepareStatement("update bet.BetOddsResults set FT = ? where Team1 = ? AND Code = ?");

                preparedStmt1.setString(1, td.eq(17).text());

                preparedStmt1.setString(2, td.eq(6).text());
                preparedStmt1.setString(3, td.eq(2).text());
                preparedStmt1.executeUpdate();

                int euReturnValue1 = preparedStmt1.executeUpdate();
                System.out.println(String.format("executeUpdate returned %d", euReturnValue1));

                p.close();

            }
        }
    }
