package db.connect.JDBC;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.sql.*;
import java.util.*;

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

            System.out.println(getCheckGLEE("planguage6"));
        }
        catch (Exception e)
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

    public String getCheckGLEE(String nickname) {
        String userQuery = "select id from pl_user where LOWER(username) = LOWER(?)";
        String serverQuery = "select s.server_name, p.username from servers s inner join pler p on s.pler_id = p.id where s.pler_id between 90 and 92 and s.status IN('live','err')";
        String gleeQuery = "select glee1, glee2, glee3 from pl_user where LOWER(username) = LOWER(?)";

        try {
            PreparedStatement userStatement = dbconn.prepareStatement(userQuery);
            userStatement.setString(1, nickname);
            ResultSet userResult = userStatement.executeQuery();
            if (!userResult.next())
                return null;

            PreparedStatement serverStatement = dbconn.prepareStatement(serverQuery);
            ResultSet serverResult = serverStatement.executeQuery();
            List<Data> serverDataList = new ArrayList<>();
            while (serverResult.next()) {
                serverDataList.add(new Data(serverResult.getString(1), serverResult.getString(2)));
            }

            PreparedStatement gleeStatement = dbconn.prepareStatement(gleeQuery);
            gleeStatement.setString(1, nickname);
            ResultSet gleeResult = gleeStatement.executeQuery();
            gleeResult.next();
            boolean glee1 = gleeResult.getString(1).equals("t");
            boolean glee2 = gleeResult.getString(2).equals("t");
            boolean glee3 = gleeResult.getString(3).equals("t");

            for (Data serverData : serverDataList) {
                if ((serverData.getGlee().equals("GLEE1") && glee1) || (serverData.getGlee().equals("GLEE2") && glee2) || (serverData.getGlee().equals("GLEE3") && glee3)) {
                    return "send " + nickname + " " + serverData.getServer_name();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void main(String[] args) {
        new DBConnect().dbConnection();
    }


}

@AllArgsConstructor
@Getter
class Data {
    private String server_name;
    private String glee;
}