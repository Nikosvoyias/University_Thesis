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

public class UnanUpdate {

    public static void main(String[] args)throws SQLException {

        Connection conn = null;
        Document doc = null;

        try {

            doc = Jsoup.connect("http://www.betonews.com/table.asp?lang=en&tp=3062").get();
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


        Element table = doc.select("table[width=100%][border=0][cellpadding=3][cellspacing=1]").first() ;
        for (Element row : table.select("tr:gt(0)")){

            Elements td = row.select("td");
            Statement s = conn.createStatement ();


            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();

            PreparedStatement preparedStmt = conn.prepareStatement("update bet.Unanimity set Team1 = ?, Team2 = ?, HT = ?, FT = ?  where date = ? AND Code = ?");


            preparedStmt.setString(1, td.eq(6).text());
            preparedStmt.setString(2, td.eq(10).text());
            preparedStmt.setString(3, td.eq(18).text());
            preparedStmt.setString(4, td.eq(17).text());
            preparedStmt.setString(5, dateFormat.format(date));
            preparedStmt.setString(6, td.eq(2).text());
            preparedStmt.executeUpdate();

            int euReturnValue = preparedStmt.executeUpdate();

            System.out.println(String.format("executeUpdate returned %d", euReturnValue));


            s.close ();
        }
    }
}
