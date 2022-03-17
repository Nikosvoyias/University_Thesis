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

public class Statistics {

    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.statistiko.com/index.php?page=statistika_stoixima&stats=ODDS_1X2&data=1X2").get();  //πραγματοποιούμε τη σύνδεση με το website που θελουμε
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

        Element table = doc.select("table.site_table").first() ;
        for (Element row : table.select("tr:gt(2)")) {
            Elements td = row.select("td");

            Statement s = conn.createStatement ();



            PreparedStatement preparedStmt = conn.prepareStatement("insert into bet.Statistics (Home, Draw, Away, nGames, 1percentage, Xpercentage, 2percentage, Recent, 1Delay, xDelay, 2Delay) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                    " " + " ON DUPLICATE KEY UPDATE " + "Home = VALUES(Home)," + "Draw = VALUES(Draw)," + "Away = VALUES(Away),"  + "nGames = VALUES(nGames)," + "1percentage = VALUES(1percentage),"  + "Xpercentage = VALUES(Xpercentage)," +"2percentage = VALUES(2percentage)," + "Recent = VALUES(Recent),"
                    + "1Delay = VALUES(1Delay), " + "xDelay = VALUES(xDelay)," + "2Delay = VALUES(2Delay)," + "id = LAST_INSERT_ID(id)");


            String string = td.eq(0).text();
            String[] parts = string.split("_", -1);

            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];

            preparedStmt.setString(1, part1);
            preparedStmt.setString(2, part2);
            preparedStmt.setString(3, part3);
            preparedStmt.setString(4, td.eq(1).text());
            preparedStmt.setString(5, td.eq(2).text());
            preparedStmt.setString(6, td.eq(3).text());
            preparedStmt.setString(7, td.eq(4).text());
            preparedStmt.setString(8, td.eq(5).text());
            preparedStmt.setString(9, td.eq(6).text());
            preparedStmt.setString(10, td.eq(7).text());
            preparedStmt.setString(11, td.eq(8).text());

            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));
            ResultSet rs = (ResultSet) s.executeQuery("SELECT LAST_INSERT_ID() AS n");
            rs.next();
            rs.getInt(1);

            s.close ();

        }

    }
}
