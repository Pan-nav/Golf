package gg.panav.golf;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;


public class MySQL {

    private final static Golf main = Golf.instance;

    public static MySQL database;
    public Connection connection;

    public void connect() throws SQLException {

        String HOST = main.getConfig().getString("MYSQL.HOST");
        int PORT = main.getConfig().getInt("MYSQL.PORT");
        String USERNAME = main.getConfig().getString("MYSQL.USERNAME");
        String DATABASE = main.getConfig().getString("MYSQL.DATABASE");
        String PASSWORD = main.getConfig().getString("MYSQL.PASSWORD");
        database = this;
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&autoReconnect=true",
                USERNAME,
                PASSWORD);

        createTable();
    }

    public boolean isConnected() {
        return connection != null;
    }

    public final Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS GOLF (ID int AUTO_INCREMENT, Player varchar(16), Score int(255), PRIMARY KEY(ID));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerScore(Player player) throws SQLException {

        PreparedStatement checkPlayerStmt = connection.prepareStatement("SELECT * FROM GOLF WHERE Player = ?;");
        checkPlayerStmt.setString(1, player.getName());
        ResultSet checkPlayerResult = checkPlayerStmt.executeQuery();

        if (checkPlayerResult.next()) {
            // Player name is in the table, update the score column
            int score = checkPlayerResult.getInt("Score") + 1;
            PreparedStatement updateScoreStmt = connection.prepareStatement("UPDATE GOLF SET Score = ? WHERE Player = ?;");
            updateScoreStmt.setInt(1, score);
            updateScoreStmt.setString(2, player.getName());
            updateScoreStmt.executeUpdate();
            player.sendMessage(main.getConfig().getString("Messages.UpdateScore").replace("{score}", String.valueOf(score)));
        } else {
            // Player name is not in the table, insert a new row with default score of 1
            PreparedStatement insertPlayerStmt = connection.prepareStatement("INSERT INTO GOLF (Player, Score) VALUES (?, ?);");
            insertPlayerStmt.setString(1, player.getName());
            insertPlayerStmt.setInt(2, 1);
            insertPlayerStmt.executeUpdate();
            player.sendMessage(main.getConfig().getString("Messages.FirstScore").replace("{score}", "1"));
        }

        return;
    }

    public String getScore(Player player) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOLF WHERE Player = ?;");
        statement.setString(1, player.getName());
        ResultSet set = statement.executeQuery();

        String message;

        if (set.next()){
            int score = set.getInt("Score");
            message = ChatColor.AQUA + player.getName() + " has score " + score;
        } else {
            message = ChatColor.AQUA + main.getConfig().getString("Messages.ScoreDeny");
        }

        return message;
    }


}
