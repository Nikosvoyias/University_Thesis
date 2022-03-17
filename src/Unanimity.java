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
import java.util.ArrayList;
import java.util.Date;

public class Unanimity {
    public static void main(String[] args)throws SQLException {

        Connection conn = null;
        Document doc = null;


        try {

            doc = Jsoup.connect("http://www.betcosmos.com/index.php?page=omofonies").get();
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


        Element table = doc.select("table.prognostika_table").first() ; //Επιλέγουμε το σωστό table απο το website
        for (Element row : table.select("tr")) {  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td");

            if(td.eq(2).hasText()) {        //edw elegxw oti den einai keni i grammi gia na apofugw ta dates


                Statement s = conn.createStatement();


                PreparedStatement preparedStmt = conn.prepareStatement("insert into bet.UnanimityFinal (Code, Team1, Team2, HomeOdd, DrawOdd, AwayOdd, SuggestedPick, HT_FT, HT, FT) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                        " " + " ON DUPLICATE KEY UPDATE " + "Code = VALUES(Code)," + "Team1 = VALUES(Team1)," + "Team2 = VALUES(Team2)," + "HomeOdd = VALUES(HomeOdd)," + "DrawOdd = VALUES(DrawOdd)," + "AwayOdd = VALUES(AwayOdd)," + " SuggestedPick = VALUES(SuggestedPick),"
                        + "HT_FT = VALUES(HT_FT), " + "HT = VALUES(HT)," + "FT = VALUES(FT)," + "id = LAST_INSERT_ID(id)");

                preparedStmt.setString(1, td.eq(2).text());
                preparedStmt.setString(2, td.eq(5).text());
                preparedStmt.setString(3, td.eq(7).text());
                preparedStmt.setString(4, td.eq(4).text());
                preparedStmt.setString(5, td.eq(6).text());
                preparedStmt.setString(6, td.eq(8).text());
                preparedStmt.setString(7, td.eq(9).text());
                preparedStmt.setString(8, td.eq(13).text());
                preparedStmt.setString(9, td.eq(14).text());
                preparedStmt.setString(10, td.eq(15).text());

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
