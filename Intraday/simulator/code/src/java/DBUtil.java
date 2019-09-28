

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nishant.vibhute
 */
public class DBUtil {

    static Connection con;

    public static Connection getConnection() {
        try {
            String DATABASE_URL = "jdbc:mysql://localhost:3306/intraday_simulator";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DATABASE_URL, "root", "root");
            return con;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
