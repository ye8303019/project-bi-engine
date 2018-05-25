package com.patsnap.insights.trickydata.manager;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午5:51
 */
@Service
public class RedShiftFactory {

    private static final String dbURL = "jdbc:redshift://my-bi-instance.ch9gnteiixzh.us-east-1.redshift.amazonaws.com:5439/bi";
    private static final String MasterUsername = "caoliang";
    private static final String MasterPassword = "Caoliang123";

    public void getInstance() {
        Connection conn = null;
        Statement stmt = null;
        try {
            //Dynamically load driver at runtime.
            //Redshift JDBC 4.1 driver: com.amazon.redshift.jdbc41.Driver
            //Redshift JDBC 4 driver: com.amazon.redshift.jdbc4.Driver
            Class.forName("com.amazon.redshift.jdbc.Driver");

            //Open a connection and define properties.
            System.out.println("Connecting to database...");
            Properties props = new Properties();

            //Uncomment the following line if using a keystore.
            //props.setProperty("ssl", "true");
            props.setProperty("user", MasterUsername);
            props.setProperty("password", MasterPassword);
            conn = DriverManager.getConnection(dbURL, props);

            //Try a simple query.
            System.out.println("Listing system tables...");
            stmt = conn.createStatement();
            String sql;
            sql = "select * from information_schema.tables;";
            ResultSet rs = stmt.executeQuery(sql);

            //Get the data from the result set.
            while (rs.next()) {
                //Retrieve two columns.
                String catalog = rs.getString("table_catalog");
                String name = rs.getString("table_name");

                //Display values.
                System.out.print("Catalog: " + catalog);
                System.out.println(", Name: " + name);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            //For convenience, handle all errors here.
            ex.printStackTrace();
        } finally {
            //Finally block to close resources.
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ex) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Finished connectivity test.");
    }
}
