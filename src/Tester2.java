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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tester2 {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.forebet.com/en/football-tips-and-predictions-for-italy/serie-b/bothtoscore").get();
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
            if(td.eq(0).hasText()) {

                Elements td1 = row.select("td");
                Elements td4 = row.select("td.predict_y");


                System.out.println(td4.eq(0).text());
            }
        }
    }
}
