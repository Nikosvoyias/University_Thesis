import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
public class OddsUpdate {

    public static void main(String[] args)throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.betcosmos.com/index.php?page=kouponi_stoixima").get();  //πραγματοποιούμε τη σύνδεση με το website που θελουμε
        } catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            String userName = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost:3306/bet?characterEncoding=utf8";    // σύνδεση με το localhost
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = (Connection) DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }


        Element table = doc.select("table.kouponi_table").first() ; //Επιλέγουμε το σωστό table απο το website
        for (Element row : table.select("tr:gt(0)")){  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td");
            Statement s = conn.createStatement ();


            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();

            PreparedStatement preparedStmt = conn.prepareStatement("update bet.BetOddsResults set HomeOdd = ?, DrawOdd = ?, AwayOdd = ?, U = ?, O = ?, G = ?, NG = ?, 0to1 = ?, 2to3 = ?, 4to6 = ?  where date = ? AND Code = ?");

            preparedStmt.setString(1, td.eq(4).text());
            preparedStmt.setString(2, td.eq(6).text());
            preparedStmt.setString(3, td.eq(8).text());
            preparedStmt.setString(4, td.eq(12).text());
            preparedStmt.setString(5, td.eq(13).text());
            preparedStmt.setString(6, td.eq(14).text());
            preparedStmt.setString(7, td.eq(15).text());
            preparedStmt.setString(8, td.eq(16).text());
            preparedStmt.setString(9, td.eq(17).text());
            preparedStmt.setString(10, td.eq(18).text());
            preparedStmt.setString(11, dateFormat.format(date));
            preparedStmt.setString(12, td.eq(2).text());
            preparedStmt.executeUpdate();

            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));

            s.close ();
        }
    }
}
