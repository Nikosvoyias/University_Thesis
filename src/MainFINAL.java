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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFINAL {
    public static void main(String[] args) throws SQLException, ParseException {


        Connection conn = null;
        Document doc = null;
        Document doc1 = null;


        try {

            doc = Jsoup.connect("http://www.betonews.com/table.asp?lang=en&tp=3062").get();
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


        Element table = doc.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]").first();
        for (Element row : table.select("tr:gt(0)")) {

            Elements td = row.select("td");

            Statement s = conn.createStatement();

            PreparedStatement preparedStmt = conn.prepareStatement("insert into bet.BetOddsResultsFinal (Competition, date, KickOffTime, Code, Team1, Team2, HomeOdd, DrawOdd," +
                    " AwayOdd, HT, FT, U, O, G, NG, 0to1, 2to3, 4to6, TotalA, TotalB, HomeGoals_1H, AwayGoals_1H, TotalGoals1H, HomeGoals_2H, AwayGoals_2H, TotalGoals2H) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                    " " + " ON DUPLICATE KEY UPDATE " + "Competition = VALUES(Competition)," + "date = VALUES(date),"
                    + "KickOffTime = VALUES(KickOffTime)," + "Code = VALUES(Code)," + "Team1 = VALUES(Team1)," + "Team2 = VALUES(Team2)," + "HomeOdd = VALUES(HomeOdd)," + "DrawOdd = VALUES(DrawOdd),"
                    + "AwayOdd = VALUES(AwayOdd), " + "HT = VALUES(HT)," + "FT = VALUES(FT)," + "U = VALUES(U)," + "O = VALUES(O)," + "G = VALUES(G)," + "NG = VALUES(NG),"
                    + "0to1 = VALUES(0to1)," + "2to3 = VALUES(2to3)," + "4to6 = VALUES(4to6)," + "TotalA = VALUES(TotalA)," + "TotalB = VALUES(TotalB)," + "HomeGoals_1H = VALUES(HomeGoals_1H)," + "AwayGoals_1H = VALUES(AwayGoals_1H)," + "TotalGoals1H = VALUES(TotalGoals1H)," + "HomeGoals_2H = VALUES(HomeGoals_2H),"
                    + "AwayGoals_2H = VALUES(AwayGoals_2H)," + "TotalGoals2H = VALUES(TotalGoals2H)," + "id = LAST_INSERT_ID(id)");


            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();

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

            int totalAteam = c + a;
            int totalBteam = d + b;

            String TOTALA = Integer.toString(totalAteam);
            String TOTALB = Integer.toString(totalBteam);

            preparedStmt.setString(1, td.eq(0).text());
            preparedStmt.setString(2, dateFormat.format(date));
            preparedStmt.setString(3, td.eq(1).text());
            preparedStmt.setString(4, td.eq(2).text());
            preparedStmt.setString(5, td.eq(6).text());
            preparedStmt.setString(6, td.eq(10).text());
            preparedStmt.setString(7, td.eq(4).text());
            preparedStmt.setString(8, td.eq(8).text());
            preparedStmt.setString(9, td.eq(12).text());
            preparedStmt.setString(10, td.eq(18).text());
            preparedStmt.setString(11, td.eq(17).text());
            preparedStmt.setString(12, "");
            preparedStmt.setString(13, "");
            preparedStmt.setString(14, "");
            preparedStmt.setString(15, "");
            preparedStmt.setString(16, "");
            preparedStmt.setString(17, "");
            preparedStmt.setString(18, "");
            preparedStmt.setString(19, TOTALA);
            preparedStmt.setString(20, TOTALB);
            preparedStmt.setString(21, part3);
            preparedStmt.setString(22, part4);
            preparedStmt.setString(23, e);
            preparedStmt.setString(24, k);
            preparedStmt.setString(25, l);
            preparedStmt.setString(26, f);

            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));
            ResultSet rs = (ResultSet) s.executeQuery("SELECT LAST_INSERT_ID() AS n");
            rs.next();
            rs.getInt(1);

            s.close();

        }

        try {

            doc1 = Jsoup.connect("http://www.betcosmos.com/index.php?page=kouponi_stoixima").get();  //πραγματοποιούμε τη σύνδεση με το website που θελουμε
        } catch (IOException e) {
            e.printStackTrace();
        }

            Element table1 = doc1.select("table.kouponi_table").first() ; //Επιλέγουμε το σωστό table απο το website
            for (Element row : table1.select("tr:gt(0)")){  // η for εξασφαλιζει οτι με τις αντιστοιχες επαναλήψεις θα περαστούν ολα τα στοιχεία του πινακα στη βαση μας
                Elements td = row.select("td");
                Statement p = conn.createStatement ();


                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();

                PreparedStatement preparedStmt1 = conn.prepareStatement("update bet.BetOddsResultsFinal set HomeOdd = ?, DrawOdd = ?, AwayOdd = ?, U = ?, O = ?, G = ?, NG = ?, 0to1 = ?, 2to3 = ?, 4to6 = ?  where date = ? AND Code = ?");


                preparedStmt1.setString(1, td.eq(4).text());
                preparedStmt1.setString(2, td.eq(6).text());
                preparedStmt1.setString(3, td.eq(8).text());
                preparedStmt1.setString(4, td.eq(12).text());
                preparedStmt1.setString(5, td.eq(13).text());
                preparedStmt1.setString(6, td.eq(14).text());
                preparedStmt1.setString(7, td.eq(15).text());
                preparedStmt1.setString(8, td.eq(16).text());
                preparedStmt1.setString(9, td.eq(17).text());
                preparedStmt1.setString(10, td.eq(18).text());
                preparedStmt1.setString(11, dateFormat.format(date));
                preparedStmt1.setString(12, td.eq(2).text());
                preparedStmt1.executeUpdate();

                int euReturnValue = preparedStmt1.executeUpdate();

                System.out.println(String.format("executeUpdate returned %d", euReturnValue));


                p.close ();
            }
        }
    }
