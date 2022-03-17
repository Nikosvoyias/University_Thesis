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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SameOddsStat {

    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.statistiko.com/index.php?page=statistika_se_asous_dipla").get();
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

        Element table = doc.select("table.site_table").first(); //Επιλέγουμε το σωστό table απο το website
        for (Element row : table.select("tr:gt(2)")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td");



            Statement s = conn.createStatement();


            PreparedStatement preparedStmt = conn.prepareStatement("insert into bet.Compare1vs2 (HomeOdd, HomeN, 1Percentage, AwayOdd, AwayN, 2Percentage) " + " VALUES (?, ?, ?, ?, ?, ?)" +
                    " " + " ON DUPLICATE KEY UPDATE " + "HomeOdd = VALUES(HomeOdd)," + "HomeN = VALUES(HomeN)," + "1Percentage = VALUES(1Percentage)," + "AwayOdd = VALUES(AwayOdd)," + "AwayN = VALUES(AwayN)," + "2Percentage = VALUES(2Percentage)," + "id = LAST_INSERT_ID(id)");

            preparedStmt.setString(1, td.eq(0).text());
            preparedStmt.setString(2, td.eq(1).text());
            preparedStmt.setString(3, td.eq(2).text());
            preparedStmt.setString(4, td.eq(4).text());
            preparedStmt.setString(5, td.eq(5).text());
            preparedStmt.setString(6, td.eq(6).text());
            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));
            ResultSet rs = (ResultSet) s.executeQuery("SELECT LAST_INSERT_ID() AS n");
            rs.next();
            rs.getInt(1);

            s.close();
        }
    }
}
