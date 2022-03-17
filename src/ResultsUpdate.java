
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Connection;

public class ResultsUpdate {

    /**
     * Στην κλάση αυτη πραγματοποιείται
     * η ανανέωση των πεδίων HT και FT
     * μετα το πέρας των αγώνων
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.betonews.com/table.asp?lang=en&tp=3062").get();  //πραγματοποιούμε τη σύνδεση με το website που θελουμε
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


        Element table = doc.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]").first() ; //Επιλέγουμε το σωστό table απο το website
        for (Element row : table.select("tr:gt(0)")){  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
            Elements td = row.select("td");
            Statement s = conn.createStatement ();


            PreparedStatement preparedStmt = conn.prepareStatement("update bet.BetOddsResults set HT = ?, FT = ?, TotalA = ?, TotalB = ?, HomeGoals_1H = ?, AwayGoals_1H = ?, TotalGoals1H = ?, HomeGoals_2H = ?, AwayGoals_2H = ?, TotalGoals2H = ? where Team1 = ? AND Code = ?");

            String string1 = td.eq(18).text();
            String[] parts1 = string1.split("-", -1);

            String part3 = parts1[0];
            String part4 = parts1[1];

            int a = Integer.parseInt("0" + part3);
            int b = Integer.parseInt("0" + part4);
            int totalA = a + b;


            String e = Integer.toString(totalA);

            String string = td.eq(17).text();
            String[] parts = string.split("-", -1);
            String part1 = parts[0];
            String part2 = parts[1];

            int c = Integer.parseInt("0" + part1);
            int d = Integer.parseInt("0" + part2);
            int totalB = (c + d) - totalA;

            c -= a;
            d -= b;

            String k = Integer.toString(c);
            String l = Integer.toString(d);

            String f = Integer.toString(totalB);

            int totalAteam =  c + a;
            int totalBteam =  d + b;

            String TOTALA = Integer.toString(totalAteam);
            String TOTALB = Integer.toString(totalBteam);

            preparedStmt.setString(1, td.eq(18).text());
            preparedStmt.setString(2, td.eq(17).text());
            preparedStmt.setString(3, TOTALA);
            preparedStmt.setString(4, TOTALB);
            preparedStmt.setString(5, part3);
            preparedStmt.setString(6, part4);
            preparedStmt.setString(7, e);
            preparedStmt.setString(8, k);
            preparedStmt.setString(9, l);
            preparedStmt.setString(10, f);
            preparedStmt.setString(11, td.eq(6).text());
            preparedStmt.setString(12, td.eq(2).text());
            preparedStmt.executeUpdate();

            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));
            s.close ();
        }
    }
}
