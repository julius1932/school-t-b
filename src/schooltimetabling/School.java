/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Junta
 */
public class School {

    private String name;
    private String emal;
    private static String theSchl = "timeTableH";
    private static String ipAddress;

    static {
        setIpAddress(new DataBaseConfig().getDbIp());
        createDataBase("theschools");
    }

    public School() {
    }

    /**
     * @return the ipAddress
     */
    public static String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param aIpAddress the ipAddress to set
     */
    public static void setIpAddress(String aIpAddress) {
        ipAddress = aIpAddress;
    }

    private static void createDataBase(String db) {
        try {
            String url = "jdbc:mysql://" + ipAddress + ":3306/?";
            String userName = "junta";//root
            String password = "julius1932";//julius
            Connection conn = DriverManager.getConnection(url, userName, password);
            Statement statmnt = conn.createStatement();
            int respond = statmnt.executeUpdate("CREATE  DATABASE IF NOT EXISTS " + db);
            System.out.println("Database created ");
        } catch (SQLException ex) {
            Logger.getLogger(School.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection connecting() throws Exception {
        String url = "jdbc:mysql://" + ipAddress + ":3306/";
        String driverName = "com.mysql.jdbc.Driver";
        String dbName = "theschools";
        String userName = "junta";
        String password = "julius1932";
        Class.forName(driverName).newInstance();
        return DriverManager.getConnection(url + dbName, userName, password);
    }

    public static List<School> getDataFormDb() {
        List<School> schls = new ArrayList<>();
        try {
            Connection con = connecting();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM schools ");
            while (resultSet.next()) {
                System.out.println("uuuuuuuuuuuuuuuuuu");
                School sch = new School();
                sch.setName(resultSet.getString("name"));
                sch.setEmal(resultSet.getString("email"));
                schls.add(sch);
            }

        } catch (Exception ex) {
            Logger.getLogger(School.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return schls;
        }
    }

    public static void insertSingleRow(String schl, String eml) {
        String qry = "INSERT INTO schools(name,email) VALUES('" + schl + "','" + eml + "')";
        System.out.println(qry);
        try (Connection con = connecting(); Statement statement = con.createStatement()) {
            statement.executeUpdate(qry);
            School.createDataBase(schl);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Duplicate entry . Data already in the system ");
            } else {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the emal
     */
    public String getEmal() {
        return emal;
    }

    /**
     * @param email the emal to set
     */
    public void setEmal(String email) {
        this.emal = email;
    }

    /**
     * @return the theSchl
     */
    public static String getTheSchl() {
        return School.theSchl;
    }

    /**
     * @param theSchl the theSchl to set
     */
    public static void setTheSchl(String theSchl) {
        School.theSchl = theSchl;
    }

}
