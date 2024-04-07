package db.connect.JDBC;

import java.sql.*;

public class DBConnect {

    // DB Driver
    String dbDriver = "org.postgresql.Driver";

    // DB URL
    // IP:PORT/스키마
    String dbUrl = "jdbc:postgresql://172.30.1.154:5432/mc_server";

    // DB ID/PW
    String dbUser = "porie";
    String dbPassword = "1224";


    Connection dbconn = null;


    public void dbConnection()
    {
        Connection connection = null;

        try
        {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            dbconn = connection;

            System.out.println("DB Connection [성공]");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public String Query(String nickname) {
        String sql = String.format(
                "select " +
                "s.server_name " +
                "from PLER p inner join servers s " +
                "on p.id = s.pler_id " +
                "where p.username = ?"
        );

        String result = null;

        try {
            PreparedStatement pstmt = dbconn.prepareStatement(sql);
            pstmt.setString(1, nickname);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
                result = rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static void main(String[] args) {
        new DBConnect().dbConnection();
    }


}
