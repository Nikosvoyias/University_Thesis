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

public class Tester3 {

    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-france/ligue1").get();
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


                Elements td2 = row.select("td");

                Elements td3 = row.select("td.tnms > span");

                Elements td4 = row.select("td.predict_y");

                Elements td6 = row.select("td.exact_yes.tabonly.scrpred");

                Elements td8 = row.select("td.lResTd > span.ht_scr");

                Elements td9 = row.select("td.lResTd");


                System.out.println(td8.eq(0).text().length());
                System.out.println(td9.eq(0).text().substring(0, td9.eq(0).text().length() - td8.eq(0).text().length()));
                System.out.println(td9.eq(0).text());

            }
        }
    }
}
