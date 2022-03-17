import com.mysql.jdbc.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Goals {
    public static void main(String[] args)throws SQLException {

        Connection conn = null;
        Document doc = null;


        try {

            doc = Jsoup.connect("http://www.statistiko.com/index.php?page=statistika_stoixima&stats=ODDS_1X2&data=SCR").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            String userName = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost:3306/bet?characterEncoding=utf8";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = (Connection) DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }


        Element table = doc.select("table.site_table").first() ; //Επιλέγουμε το σωστό table απο το website
        for (Element row : table.select("tr:gt(2)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td");
            System.out.println(td.eq(0).text());
            System.out.println(td.eq(1).text());
            System.out.println(td.eq(2).text());
            System.out.println(td.eq(3).text());
            System.out.println(td.eq(4).text());
            System.out.println(td.eq(5).text());
            System.out.println(td.eq(6).text());
            System.out.println(td.eq(7).text());
            System.out.println(td.eq(8).text());
            System.out.println(td.eq(9).text());
            System.out.println(td.eq(10).text());
            System.out.println(td.eq(11).text());
            System.out.println(td.eq(12).text());
            System.out.println(td.eq(13).text());
            System.out.println(td.eq(14).text());
            System.out.println(td.eq(15).text());
            System.out.println(td.eq(16).text());

        }
    }
}
